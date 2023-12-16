package com.alibou.security.repository;

import java.util.Optional;

import com.alibou.security.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {

  Optional<Patient> findByEmail(String email);
  boolean existsByEmail(String email);

  boolean existsById(String id);
}
