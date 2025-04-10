package io.foodapp.server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.foodapp.server.models.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    // Custom query methods can be defined here if needed

    List<Address> findByIsDeletedFalse();
    List<Address> findByIsDeletedTrue();
    List<Address> findByUserIdAndIsDeletedFalse(String userId);
}
