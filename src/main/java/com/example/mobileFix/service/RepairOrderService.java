package com.example.mobileFix.service;

import com.example.mobileFix.model.RepairOrder;
import com.example.mobileFix.model.Status;
import java.util.List;

public interface RepairOrderService {

    List<RepairOrder> getAllOrders(); // ADMIN: todas
    List<RepairOrder> getOrdersByCustomer(Long customerId); // USER: propias
    List<RepairOrder> getOrdersByAssignedTech(Long techId); // TECH: asignadas
    List<RepairOrder> getOrdersByStatus(Status status); // filtro opcional

    RepairOrder getOrderById(Long id);

    RepairOrder createOrder(Long customerId, Long deviceId, String issueDescription);

    RepairOrder assignTech(Long orderId, Long techId); // solo ADMIN

    RepairOrder updateStatus(Long orderId, Status newStatus, String techNotes); // TECH/ADMIN

    void deleteOrder(Long orderId, Long requesterId, boolean isAdmin); // USER o ADMIN
}
