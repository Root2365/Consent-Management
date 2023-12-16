package com.example.hospital_manager.repo;

import com.example.hospital_manager.entity.HospitalAddr;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HospitalAddrRepo extends JpaRepository<HospitalAddr,Integer> {
    Optional<HospitalAddr> findByName(String name);

    Optional<HospitalAddr> findById(String id);
    boolean existsByName(String name);
    boolean existsById(String id);


}
