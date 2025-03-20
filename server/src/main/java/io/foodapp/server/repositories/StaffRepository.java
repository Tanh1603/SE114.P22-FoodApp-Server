package io.foodapp.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.foodapp.server.models.Staff;
@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    
}
