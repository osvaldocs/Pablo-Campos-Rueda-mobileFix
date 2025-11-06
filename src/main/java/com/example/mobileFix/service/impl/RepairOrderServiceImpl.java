package com.example.mobileFix.service.impl;

import com.example.mobileFix.model.*;
import com.example.mobileFix.repository.*;
import com.example.mobileFix.service.RepairOrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RepairOrderServiceImpl implements RepairOrderService {

    @Autowired
    private RepairOrderRepository repairOrderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    // ✅ ADMIN: obtiene todas las órdenes
    @Override
    public List<RepairOrder> getAllOrders() {
        return repairOrderRepository.findAll();
    }

    // ✅ USER: obtiene sus propias órdenes
    @Override
    public List<RepairOrder> getOrdersByCustomer(Long customerId) {
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
        return repairOrderRepository.findByCustomer(customer);
    }

    // ✅ TECH: obtiene órdenes asignadas
    @Override
    public List<RepairOrder> getOrdersByAssignedTech(Long techId) {
        User tech = userRepository.findById(techId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Technician not found"));
        return repairOrderRepository.findByAssignedTech(tech);
    }

    // ✅ Filtro opcional por estado
    @Override
    public List<RepairOrder> getOrdersByStatus(Status status) {
        return repairOrderRepository.findByStatus(status);
    }

    // ✅ Obtener una orden por ID
    @Override
    public RepairOrder getOrderById(Long id) {
        return repairOrderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Repair order not found"));
    }

    // ✅ Crear una nueva orden (solo USER)
    @Override
    @Transactional
    public RepairOrder createOrder(Long customerId, Long deviceId, String issueDescription) {
        if (issueDescription == null || issueDescription.length() < 10) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Issue description must have at least 10 characters");
        }

        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found"));

        RepairOrder order = new RepairOrder();
        order.setCustomer(customer);
        order.setDevice(device);
        order.setIssueDescription(issueDescription);
        order.setStatus(Status.PENDING);

        // 👇 No seteamos fechas manualmente, JPA lo hace con @PrePersist
        return repairOrderRepository.save(order);
    }

    // ✅ Asignar técnico (solo ADMIN)
    @Override
    @Transactional
    public RepairOrder assignTech(Long orderId, Long techId) {
        RepairOrder order = getOrderById(orderId);

        if (order.getStatus() != Status.PENDING) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Only PENDING orders can be assigned");
        }

        User tech = userRepository.findById(techId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Technician not found"));

        if (tech.getRole() != Role.TECH) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not a technician");
        }

        order.setAssignedTech(tech);
        // 👇 No hace falta setUpdatedAt, @PreUpdate lo cubre
        return repairOrderRepository.save(order);
    }

    // ✅ Cambiar estado (TECH o ADMIN)
    @Override
    @Transactional
    public RepairOrder updateStatus(Long orderId, Status newStatus, String techNotes) {
        RepairOrder order = getOrderById(orderId);

        if (!isValidTransition(order.getStatus(), newStatus)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status transition");
        }

        order.setStatus(newStatus);

        if (techNotes != null && !techNotes.isBlank()) {
            order.setTechNotes(techNotes.trim());
        }

        return repairOrderRepository.save(order);
    }

    // 🔸 Helper para validar flujo de estados
    private boolean isValidTransition(Status current, Status next) {
        return switch (current) {
            case PENDING -> next == Status.IN_PROGRESS || next == Status.CANCELED;
            case IN_PROGRESS -> next == Status.READY || next == Status.CANCELED;
            case READY -> next == Status.DELIVERED || next == Status.CANCELED;
            default -> false;
        };
    }

    // ✅ Eliminar orden (USER o ADMIN)
    @Override
    @Transactional
    public void deleteOrder(Long orderId, Long requesterId, boolean isAdmin) {
        RepairOrder order = getOrderById(orderId);

        if (isAdmin) {
            repairOrderRepository.deleteById(orderId);
            return;
        }

        if (!order.getCustomer().getId().equals(requesterId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete your own orders");
        }

        if (order.getStatus() != Status.PENDING) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Only PENDING orders can be deleted");
        }

        repairOrderRepository.deleteById(orderId);
    }
}
