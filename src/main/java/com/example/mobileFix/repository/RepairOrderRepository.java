package com.example.mobileFix.repository;

import com.example.mobileFix.model.RepairOrder;
import com.example.mobileFix.model.User;
import com.example.mobileFix.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepairOrderRepository extends JpaRepository<RepairOrder, Long> {

    // Todas las órdenes de un cliente específico (USER)
    List<RepairOrder> findByCustomer(User customer);

    // Todas las órdenes asignadas a un técnico (TECH)
    List<RepairOrder> findByAssignedTech(User tech);

    // Filtrar por estado
    List<RepairOrder> findByStatus(Status status);

    // Filtrar por cliente y estado
    List<RepairOrder> findByCustomerAndStatus(User customer, Status status);

    // Filtrar por técnico asignado y estado
    List<RepairOrder> findByAssignedTechAndStatus(User tech, Status status);
}
