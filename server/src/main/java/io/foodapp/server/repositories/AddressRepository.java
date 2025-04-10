package io.foodapp.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.foodapp.server.models.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    // Custom query methods can be defined here if needed

}
