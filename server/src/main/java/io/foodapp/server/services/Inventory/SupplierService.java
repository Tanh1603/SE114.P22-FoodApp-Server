package io.foodapp.server.services.Inventory;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.foodapp.server.dtos.Inventory.SupplierDTO;
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
    
    public List<SupplierDTO> getAvailableSuppliers() {
        try {
            return supplierMapper.toDtoList(supplierRepository.findByIsDeletedFalse());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<SupplierDTO> getDeletedSuppliers() {
        try {
            return supplierMapper.toDtoList(supplierRepository.findByIsDeletedTrue());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    public SupplierDTO getSupplierById(Long id) {
        try {
            return supplierRepository.findById(id)
                .map(supplierMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found with id: " + id));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    public SupplierDTO createSupplier(SupplierDTO supplierDTO) {
        try {
            return supplierMapper.toDTO(supplierRepository.save(supplierMapper.toEntity(supplierDTO)));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    public SupplierDTO updateSupplier(SupplierDTO supplierDTO) {
        try {
            var supplier = supplierRepository.findById(supplierDTO.getId()).orElseThrow();
            supplierMapper.updateEntityFromDto(supplierDTO, supplier);
            return supplierMapper.toDTO(supplierRepository.save(supplier));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    public void deleteSupplier(Long id) {
        try {
            Supplier supplier = supplierRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Supplier not found with id: " + id));
            supplier.setDeleted(true);
            supplierRepository.save(supplier);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
