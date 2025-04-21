package io.foodapp.server.services.Inventory;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import io.foodapp.server.dtos.Filter.ImportFilter;
import io.foodapp.server.dtos.Inventory.ImportDetailRequest;
import io.foodapp.server.dtos.Inventory.ImportRequest;
import io.foodapp.server.dtos.Inventory.ImportResponse;
import io.foodapp.server.dtos.Specification.ImportSpecification;
import io.foodapp.server.mappers.Inventory.ImportDetailMapper;
import io.foodapp.server.mappers.Inventory.ImportMapper;
import io.foodapp.server.models.InventoryModel.Import;
import io.foodapp.server.models.InventoryModel.ImportDetail;
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
            return importMapper.toDTO(saved);
        } catch (Exception e) {
            throw new RuntimeException("Error creating import: " + e.getMessage());
        }
    }

    // @Transactional
    // public ImportResponse updateImport(Long id, ImportRequest importRequest) {
    // try {
    // Import import1 = importRepository.findById(id)
    // .orElseThrow(() -> new RuntimeException("Import not found"));

    // importMapper.updateEntityFromDto(importRequest, import1,
    // supplierRepository,
    // staffRepository,
    // importDetailRepository,
    // importDetailMapper,
    // ingredientRepository);
    // return importMapper.toDTO(importRepository.save(import1));
    // } catch (Exception e) {
    // System.out.println("Error updating import: " + e.getLocalizedMessage());
    // throw new RuntimeException("Error updating import: " + e.getMessage());
    // }
    // }

    @Transactional
    public ImportResponse updateImport(Long id, ImportRequest importRequest) {
        try {
            Import import1 = importRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Import not found"));

            // 🔁 Lấy danh sách ID từ request
            Set<Long> requestDetailIds = importRequest.getImportDetails().stream()
                    .map(ImportDetailRequest::getId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            // 🧹 Xoá ImportDetail không còn trong request
            Iterator<ImportDetail> iterator = import1.getImportDetails().iterator();
            while (iterator.hasNext()) {
                ImportDetail detail = iterator.next();
                if (detail.getId() != null && !requestDetailIds.contains(detail.getId())) {
                    iterator.remove(); // xoá khỏi list
                    detail.setAnImport(null);
                    importDetailRepository.delete(detail);
                }
            }
            importDetailRepository.flush(); // đảm bảo xoá sạch

            // 🔁 Cập nhật lại thông tin import + details còn lại
            importMapper.updateEntityFromDto(importRequest, import1,
                    supplierRepository,
                    staffRepository,
                    importDetailRepository,
                    importDetailMapper,
                    ingredientRepository);

            Import saved = importRepository.save(import1);
            return importMapper.toDTO(saved);

        } catch (Exception e) {
            System.out.println("Error updating import: " + e.getLocalizedMessage());
            throw new RuntimeException("Error updating import: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteImport(Long id) {
        try {
            Import import1 = importRepository.findById(id).orElseThrow(() -> new RuntimeException("Import not found"));
            // ❗ Gỡ quan hệ trước khi xoá
            import1.getImportDetails().forEach(detail -> detail.setAnImport(null));

            // ❗ Xoá toàn bộ import details trước
            importDetailRepository.deleteAll(import1.getImportDetails());

            // ❗ Xoá chính import
            importRepository.delete(import1);
        } catch (Exception e) {
            System.out.println("Error deleting import: " + e.getMessage());
            throw new RuntimeException("Error deleting import", e);
        }
    }

}
