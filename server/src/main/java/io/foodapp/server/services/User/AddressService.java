package io.foodapp.server.services.User;

import io.foodapp.server.dtos.User.AddressRequest;
import io.foodapp.server.dtos.User.AddressResponse;
import io.foodapp.server.mappers.User.AddressMapper;
import io.foodapp.server.models.User.Address;
import io.foodapp.server.repositories.User.AddressRepository;
import io.foodapp.server.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final String customerId = SecurityUtils.getCurrentCustomerId();

    public List<AddressResponse> getAddresses() {
        try {
            return addressMapper.toDTO(addressRepository.findByUserId(customerId));
        } catch (Exception e) {
            throw new RuntimeException("Error fetching addresses: " + e.getMessage());
        }
    }

    public AddressResponse createAddress(AddressRequest request) {
        try {
            Address address = addressMapper.toEntity(request);
            address.setUserId(customerId);
            return addressMapper.toDTO(addressRepository.save(address));
        } catch (Exception e) {
            throw new RuntimeException("Error creating address: " + e.getMessage());
        }
    }

    public AddressResponse updateAddress(Long id, AddressRequest request) {
        try {
            var existingAddress = addressRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Address not found with id: " + id));
            addressMapper.updateEntityFromDTO(request, existingAddress);
            existingAddress.setUserId(customerId);
            return addressMapper.toDTO(addressRepository.save(existingAddress));
        } catch (Exception e) {
            throw new RuntimeException("Error updating address: " + e.getMessage());
        }
    }

    public void deleteAddress(Long id) {
        try {
            Address address = addressRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Address not found"));

            if (!address.getUserId().equals(customerId)) {
                throw new RuntimeException("You do not have permission to delete this address.");
            }

            addressRepository.delete(address);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error deleting address: " + e.getMessage());
        }
    }
}
