package com.codeweb.salvo.repository;

import com.codeweb.salvo.models.Ship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipRepository extends JpaRepository<Ship, Long> {
}
