package com.example.mobileFix.repository;

import com.example.mobileFix.model.User;
import com.example.mobileFix.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Buscar por username (útil para login)
    Optional<User> findByUsername(String username);

    // Buscar por email (útil para validación de unicidad)
    Optional<User> findByEmail(String email);

    // Filtrar usuarios por rol (ej.: listar todos los técnicos)
    List<User> findByRole(Role role);
}

