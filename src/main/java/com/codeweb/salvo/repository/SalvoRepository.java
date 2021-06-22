package com.codeweb.salvo.repository;

import com.codeweb.salvo.models.Salvo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalvoRepository extends JpaRepository<Salvo, Long> {
}
