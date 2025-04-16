package io.foodapp.server.services.Inventory;

import java.util.List;

import org.springframework.stereotype.Service;

import io.foodapp.server.dtos.Inventory.SupplierRequest;
import io.foodapp.server.dtos.Inventory.SupplierResponse;
import io.foodapp.server.mappers.Inventory.SupplierMapper;
import io.foodapp.server.models.InventoryModel.Supplier;
import io.foodapp.server.repositories.Inventory.SupplierRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupplierService {
    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;
    
    public List<SupplierResponse> getAvailableSuppliers() {
        try {
            return supplierMapper.toDTOs(supplierRepository.findByIsDeletedFalse());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching supplier data: " + e.getMessage());

        }
    }

    public List<SupplierResponse> getDeletedSuppliers() {
        try {
            return supplierMapper.toDTOs(supplierRepository.findByIsDeletedTrue());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching supplier data: " + e.getMessage());

        }
    }
    
    public SupplierResponse getSupplierById(Long id) {
        try {
            return supplierRepository.findById(id)
                .map(supplierMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found with id: " + id));
        } catch (Exception e) {
            throw new RuntimeException("Error fetching supplier data: " + e.getMessage());

        }
    }
    public SupplierResponse createSupplier(SupplierRequest request) {
        try {
            return supplierMapper.toDTO(supplierRepository.save(supplierMapper.toEntity(request)));
        } catch (Exception e) {
            throw new RuntimeException("Error fetching supplier data: " + e.getMessage());

        }
    }
    public SupplierResponse updateSupplier(Long id, SupplierRequest request) {
        try {
            var supplier = supplierRepository.findById(id).orElseThrow();
            supplierMapper.updateEntityFromDto(request, supplier);
            return supplierMapper.toDTO(supplierRepository.save(supplier));
        } catch (Exception e) {
            throw new RuntimeException("Error fetching supplier data: " + e.getMessage());

        }
    }
    public void deleteSupplier(Long id) {
        try {
            Supplier supplier = supplierRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Supplier not found with id: " + id));
            supplier.setDeleted(true);
            supplierRepository.save(supplier);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching supplier data: " + e.getMessage());

        }
    }

}
