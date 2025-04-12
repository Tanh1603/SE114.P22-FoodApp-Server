package io.foodapp.server.services.Inventory;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.foodapp.server.dtos.Inventory.ImportRequest;
import io.foodapp.server.dtos.Inventory.ImportResponse;
import io.foodapp.server.mappers.Inventory.ImportDetailRequestMapper;
import io.foodapp.server.mappers.Inventory.ImportRequestMapper;
import io.foodapp.server.mappers.Inventory.ImportResponseMapper;
import io.foodapp.server.models.InventoryModel.Import;
import io.foodapp.server.models.InventoryModel.ImportDetail;
import io.foodapp.server.models.InventoryModel.Supplier;
import io.foodapp.server.models.MenuModel.Ingredient;
import io.foodapp.server.models.StaffModel.Staff;
import io.foodapp.server.repositories.Inventory.ImportRepository;
import io.foodapp.server.repositories.Inventory.SupplierRepository;
import io.foodapp.server.repositories.Menu.IngredientRepository;
import io.foodapp.server.repositories.Staff.StaffRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImportService {
    private final ImportDetailRequestMapper importDetailRequestMapper;
    
    private final ImportRepository importRepository;
    private final ImportRequestMapper importRequestMapper;
    private final ImportResponseMapper importResponseMapper;
    private final SupplierRepository supplierRepository;
    private final StaffRepository staffRepository;
    private final IngredientRepository ingredientRepository;

    public List<ImportResponse> getAvailableImports() {
        try {
            return importResponseMapper.toDtoList(importRepository.findByIsDeletedFalse());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<ImportResponse> getDeletedImports() {
        try {
            return importResponseMapper.toDtoList(importRepository.findByIsDeletedTrue());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public ImportResponse getImportById(Long id) {
        try {
            return importRepository.findById(id)
                    .map(importResponseMapper::toDTO)
                    .orElseThrow(() -> new NoSuchElementException("Import not found with id: " + id));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    public ImportResponse createImport(ImportRequest request) {
        try {
            Import anImport = importRequestMapper.toEntity(request);
    
            Supplier supplier = supplierRepository.findById(request.getSupplierId())
                    .orElseThrow(() -> new RuntimeException("Supplier not found"));
            anImport.setSupplier(supplier); // Gán supplier đã được quản lý

            Staff staff = staffRepository.findById(request.getStaffId())
                    .orElseThrow(() -> new RuntimeException("Staff not found"));
            anImport.setStaff(staff); // Gán staff đã được quản lý

            // Gán danh sách ImportDetail nếu có
            List<ImportDetail> importDetails = request.getImportDetails().stream()
                    .map(detailDTO -> {
                        ImportDetail detail = importDetailRequestMapper.toEntity(detailDTO);
                        Ingredient ingredient = ingredientRepository.findById(detailDTO.getIngredientId())
                                .orElseThrow(() -> new RuntimeException("Ingredient not found"));
                        detail.setIngredient(ingredient);
                        detail.setAnImport(anImport);
                        return detail;
                    })
                    .collect(Collectors.toList());
    
            anImport.setImportDetails(importDetails);
    
            // Lưu vào database
            Import savedImport = importRepository.save(anImport);
            return importResponseMapper.toDTO(savedImport);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating import", e);
        }
    }
    


    public ImportResponse updateImport(ImportRequest request) {
        try {
            return importRepository.findById(request.getId())
                    .map(existingImport -> {
                        // Cập nhật Supplier
                        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                                .orElseThrow(() -> new RuntimeException("Supplier not found"));
                        existingImport.setSupplier(supplier);
    
                        // Cập nhật Staff
                        Staff staff = staffRepository.findById(request.getStaffId())
                                .orElseThrow(() -> new RuntimeException("Staff not found"));
                        existingImport.setStaff(staff);
    
                        // Cập nhật các trường đơn (ngày, ghi chú, v.v.)
                        importRequestMapper.updateEntityFromDto(request, existingImport);
    
                        // Xử lý danh sách chi tiết import
                        List<ImportDetail> updatedDetails = request.getImportDetails().stream()
                                .map(detailDTO -> {
                                    ImportDetail detail = importDetailRequestMapper.toEntity(detailDTO);
    
                                    Ingredient ingredient = ingredientRepository.findById(detailDTO.getIngredientId())
                                            .orElseThrow(() -> new RuntimeException("Ingredient not found"));
    
                                    detail.setIngredient(ingredient);
                                    detail.setAnImport(existingImport); // Thiết lập quan hệ 2 chiều
                                    return detail;
                                })
                                .collect(Collectors.toList());
    
                        // Gán lại danh sách chi tiết mới
                        existingImport.setImportDetails(updatedDetails);
    
                        // Lưu lại vào database
                        Import updatedImport = importRepository.save(existingImport);
                        return importResponseMapper.toDTO(updatedImport);
                    })
                    .orElseThrow(() -> new RuntimeException("Import not found"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating import", e);
        }
    }
    
    
    

    public void deleteImport(Long id) {
        try {
            Import anImport = importRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Import not found"));
    
            // Xóa mềm Import
            anImport.setIsDeleted(true);
    
            // Xóa mềm tất cả ImportDetail liên quan
            anImport.getImportDetails().forEach(detail -> detail.setDeleted(true));
    
            importRepository.save(anImport);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    
}
