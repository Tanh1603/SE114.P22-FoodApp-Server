package io.foodapp.server.services.Inventory;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import io.foodapp.server.dtos.Filter.ImportFilter;
import io.foodapp.server.dtos.Inventory.ImportRequest;
import io.foodapp.server.dtos.Inventory.ImportResponse;
import io.foodapp.server.dtos.Specification.ImportSpecification;
import io.foodapp.server.mappers.Inventory.ImportDetailMapper;
import io.foodapp.server.mappers.Inventory.ImportMapper;
import io.foodapp.server.models.InventoryModel.Import;
import io.foodapp.server.repositories.Inventory.ImportDetailRepository;
import io.foodapp.server.repositories.Inventory.ImportRepository;
import io.foodapp.server.repositories.Inventory.SupplierRepository;
import io.foodapp.server.repositories.Menu.IngredientRepository;
import io.foodapp.server.repositories.Staff.StaffRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImportService {
    private final ImportRepository importRepository;
    private final ImportDetailRepository importDetailRepository;
    private final IngredientRepository ingredientRepository;
    private final StaffRepository staffRepository;
    private final SupplierRepository supplierRepository;
    private final InventoryService inventoryService;

    private final ImportMapper importMapper;
    private final ImportDetailMapper importDetailMapper;

    public Page<ImportResponse> getImports(ImportFilter importFilter, Pageable pageable) {
        try {
            // Sử dụng Specification để lọc dữ liệu
            Specification<Import> specification = ImportSpecification.withFilter(importFilter);
            Page<Import> imports = importRepository.findAll(specification, pageable);
            return imports.map(importMapper::toDTO);
        } catch (Exception e) {
            System.out.println("Error fetching imports: " + e.getMessage());
            throw new RuntimeException("Error fetching imports", e);
        }
    }

    @Transactional
    public ImportResponse createImport(ImportRequest request) {
        try {
            Import import1 = importMapper.toEntity(
                    request,
                    supplierRepository,
                    staffRepository,
                    importDetailRepository,
                    importDetailMapper,
                    ingredientRepository);
            Import saved = importRepository.saveAndFlush(import1);
            inventoryService.updateInventoryFromImport(saved);
            return importMapper.toDTO(saved);
        } catch (Exception e) {
            throw new RuntimeException("Error creating import: " + e.getMessage());
        }
    }

    @Transactional
    public ImportResponse updateImport(Long id, ImportRequest importRequest) {
        try {
            Import import1 = importRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Import not found"));
            // Cập nhật các trường cơ bản
            importMapper.updateEntityFromDto(importRequest, import1,
                    supplierRepository,
                    staffRepository,
                    importDetailRepository,
                    importDetailMapper,
                    ingredientRepository);
            return importMapper.toDTO(importRepository.saveAndFlush(import1));
        } catch (Exception e) {
            System.out.println("Error updating import: " + e.getLocalizedMessage());
            throw new RuntimeException("Error updating import: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteImport(Long id) {
        try {
            Import import1 = importRepository.findById(id).orElseThrow(() -> new RuntimeException("Import not found"));
            import1.getImportDetails().forEach(importDetail -> {
                importDetail.setDeleted(true);
                importDetailRepository.save(importDetail);
            });
            import1.setDeleted(true);
            importRepository.save(import1);
        } catch (Exception e) {
            System.out.println("Error deleting import: " + e.getMessage());
            throw new RuntimeException("Error deleting import", e);
        }
    }
    
}
