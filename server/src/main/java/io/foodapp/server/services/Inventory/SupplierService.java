package io.foodapp.server.services.Inventory;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import io.foodapp.server.dtos.Filter.SupplierFilter;
import io.foodapp.server.dtos.Inventory.SupplierRequest;
import io.foodapp.server.dtos.Inventory.SupplierResponse;
import io.foodapp.server.dtos.Specification.SupplierSpecification;
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

    // ------------------ Get All ------------------
    public Page<SupplierResponse> getSuppliers(SupplierFilter supplierFilter, Pageable pageable) {
        try {
            Specification<Supplier> specification = SupplierSpecification.withFilter(supplierFilter);
            Page<Supplier> suppliers = supplierRepository.findAll(specification, pageable);
            return suppliers.map(supplierMapper::toDTO);
        } catch (Exception e) {
            System.out.println("Error fetching suppliers: " + e.getMessage());
            throw new RuntimeException("Error fetching suppliers", e);
        }
    }

    public SupplierResponse createSupplier(SupplierRequest request) {
        try {
            return supplierMapper.toDTO(supplierRepository.save(supplierMapper.toEntity(request)));
        } catch (RuntimeException e) {
            throw new RuntimeException("Error fetching supplier data: " + e.getMessage());

        }
    }

    public SupplierResponse updateSupplier(Long id, SupplierRequest request) {
        try {
            var supplier = supplierRepository.findById(id).orElseThrow();
            supplierMapper.updateEntityFromDto(request, supplier);
            return supplierMapper.toDTO(supplierRepository.save(supplier));
        } catch (RuntimeException e) {
            throw new RuntimeException("Error fetching supplier data: " + e.getMessage());

        }
    }

    public void setActiveSupplier(Long id, boolean isActive) {
        try {
            Supplier supplier = supplierRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Supplier not found with id: " + id));
            supplier.setActive(isActive);
            supplierRepository.save(supplier);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error fetching supplier data: " + e.getMessage());

        }
    }

    public void deleteSupplier(Long id) {
        try {
            Supplier supplier = supplierRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Supplier not found with id: " + id));
            supplierRepository.delete(supplier);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error fetching supplier data: " + e.getMessage());

        }
    }

}
