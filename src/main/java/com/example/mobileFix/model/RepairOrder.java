package com.example.mobileFix.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "repair_orders")
public class RepairOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Usuario que crea la orden (cliente)
    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id")
    private User customer;

    // Dispositivo que se repara
    @ManyToOne(optional = false)
    @JoinColumn(name = "device_id")
    private Device device;

    // Descripción del problema
    @NotBlank(message = "Issue description is required")
    @Column(nullable = false, length = 500)
    private String issueDescription;

    // Estado de la reparación
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;

    // Técnico asignado (puede ser null al crear)
    @ManyToOne
    @JoinColumn(name = "assigned_tech_id")
    private User assignedTech;

    // Notas técnicas (opcionales)
    @Column(length = 1000)
    private String techNotes;

    // Auditoría
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // 🔸 Constructores
    public RepairOrder() {}

    public RepairOrder(User customer, Device device, String issueDescription) {
        this.customer = customer;
        this.device = device;
        this.issueDescription = issueDescription;
        this.status = Status.PENDING;
    }

    // 🔸 Callbacks para auditoría
    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getAssignedTech() {
        return assignedTech;
    }

    public void setAssignedTech(User assignedTech) {
        this.assignedTech = assignedTech;
    }

    public String getTechNotes() {
        return techNotes;
    }

    public void setTechNotes(String techNotes) {
        this.techNotes = techNotes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }


}
