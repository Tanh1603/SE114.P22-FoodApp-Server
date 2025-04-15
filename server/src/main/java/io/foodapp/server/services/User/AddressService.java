package io.foodapp.server.services.User;

import java.util.List;

import org.springframework.stereotype.Service;

import io.foodapp.server.dtos.User.AddressDTO;
import io.foodapp.server.mappers.Staff.AddressMapper;
import io.foodapp.server.repositories.User.AddressRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public List<AddressDTO> getAddressesByUserId(String userId) {
        try {
            return addressMapper.toDtoList(addressRepository.findByUserIdAndIsDeletedFalse(userId));
        } catch (Exception e) {
            throw new RuntimeException("Error fetching addresses: " + e.getMessage());
        }
    }

    public AddressDTO createAddress(AddressDTO addressDTO) {
        try {
            return addressMapper.toDTO(addressRepository.save(addressMapper.toEntity(addressDTO)));
        } catch (Exception e) {
            throw new RuntimeException("Error creating address: " + e.getMessage());
        }
    }

    public AddressDTO updateAddress(AddressDTO addressDTO) {
        try {
            var existingAddress = addressRepository.findById(addressDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Address not found with id: " + addressDTO.getId()));
            addressMapper.updateEntityFromDto(addressDTO, existingAddress);
            return addressMapper.toDTO(addressRepository.save(existingAddress));
        } catch (Exception e) {
            throw new RuntimeException("Error updating address: " + e.getMessage());
        }
    }

    public void deleteAddress(Long id) {
        try {
            var existingAddress = addressRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Address not found with id: " + id));
            existingAddress.setDeleted(true);
            addressRepository.save(existingAddress);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting address: " + e.getMessage());
        }
    }

}
