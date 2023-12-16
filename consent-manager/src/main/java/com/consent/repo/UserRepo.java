package com.consent.repo;

import com.consent.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {
    Optional<User>findByServiceName(String name);
    boolean existsByServiceName(String name);
    @Override
    boolean existsById(Integer id);
}
