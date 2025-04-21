package io.foodapp.server.services.Inventory;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import io.foodapp.server.dtos.Filter.ExportFilter;
import io.foodapp.server.dtos.Inventory.ExportDetailRequest;
import io.foodapp.server.dtos.Inventory.ExportRequest;
import io.foodapp.server.dtos.Inventory.ExportResponse;
import io.foodapp.server.dtos.Specification.ExportSpecification;
import io.foodapp.server.mappers.Inventory.ExportDetailMapper;
import io.foodapp.server.mappers.Inventory.ExportMapper;
import io.foodapp.server.models.InventoryModel.Export;
import io.foodapp.server.models.InventoryModel.ExportDetail;
import io.foodapp.server.repositories.Inventory.ExportDetailRepository;
import io.foodapp.server.repositories.Inventory.ExportRepository;
import io.foodapp.server.repositories.Inventory.InventoryRepository;
import io.foodapp.server.repositories.Staff.StaffRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExportService {
    private final ExportRepository exportRepository;
    private final ExportDetailRepository exportDetailRepository;
    private final InventoryRepository inventoryRepository;
    private final StaffRepository staffRepository;

    private final ExportMapper exportMapper;
    private final ExportDetailMapper exportDetailMapper;
    private final InventoryService inventoryService;

    public Page<ExportResponse> getExports(ExportFilter exportFilter, Pageable pageable) {
        try {
            // Sử dụng Specification để lọc dữ liệu
            Specification<Export> specification = ExportSpecification.withFilter(exportFilter);
            Page<Export> exports = exportRepository.findAll(specification, pageable);
            return exports.map(exportMapper::toDTO);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching exports" + e.getMessage());
        }
    }

    @Transactional
    public ExportResponse createExport(ExportRequest request) {
        try {
            Export export = exportMapper.toEntity(
                    request,
                    staffRepository,
                    exportDetailRepository,
                    exportDetailMapper,
                    inventoryRepository);

            export.getExportDetails().forEach(detail -> {
                inventoryService.exportFromInventory(detail.getInventory().getId(), detail.getQuantity());
            });

            Export saved = exportRepository.saveAndFlush(export);

            return exportMapper.toDTO(saved);
        } catch (Exception e) {
            throw new RuntimeException("Error creating export: " + e.getMessage());
        }
    }

    @Transactional
    public ExportResponse updateExport(Long id, ExportRequest exportRequest) {
        try {
            Export export = exportRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Export not found"));

            if (export.getExportDate().isBefore(LocalDate.now().minusDays(1))) {
                throw new RuntimeException("Không thể sửa phiếu xuất đã quá 1 ngày.");
            }

            export.getExportDetails().forEach(detail -> {
                inventoryService.returnInventory(detail.getInventory().getId(), detail.getQuantity());
            });

            // 🔁 Lấy danh sách ID từ request
            Set<Long> requestDetailIds = exportRequest.getExportDetails().stream()
                    .map(ExportDetailRequest::getId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            // 🧹 Xoá ExportDetail không còn trong request
            Iterator<ExportDetail> iterator = export.getExportDetails().iterator();
            while (iterator.hasNext()) {
                ExportDetail detail = iterator.next();
                if (detail.getId() != null && !requestDetailIds.contains(detail.getId())) {
                    iterator.remove(); // xoá khỏi list
                    detail.setExport(null);
                    exportDetailRepository.delete(detail);
                }
            }
            exportDetailRepository.flush(); // đảm bảo xoá sạch

            // 🔁 Cập nhật lại thông tin export + details còn lại
            exportMapper.updateEntityFromDto(exportRequest, export,
                    staffRepository,
                    exportDetailRepository,
                    exportDetailMapper,
                    inventoryRepository);

            Export saved = exportRepository.save(export);

            // 🔁 Cập nhật lại số lượng tồn kho cho từng chi tiết nhập hàng còn lại
            export.getExportDetails().forEach(detail -> {
                inventoryService.exportFromInventory(detail.getInventory().getId(), detail.getQuantity());
            });

            return exportMapper.toDTO(saved);

        } catch (RuntimeException e) {
            throw new RuntimeException("Error updating export: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteExport(Long id) {
        try {
            Export export = exportRepository.findById(id).orElseThrow(() -> new RuntimeException("Export not found"));

            if (export.getExportDate().isBefore(LocalDate.now().minusDays(1))) {
                throw new RuntimeException("Không thể xoá phiếu xuất đã quá 1 ngày.");
            }

            export.getExportDetails().forEach(detail -> {
                inventoryService.returnInventory(detail.getInventory().getId(), detail.getQuantity());
            });

            // ❗ Gỡ quan hệ trước khi xoá
            export.getExportDetails().forEach(detail -> detail.setExport(null));

            // ❗ Xoá toàn bộ export details trước
            exportDetailRepository.deleteAll(export.getExportDetails());

            // ❗ Xoá chính export
            exportRepository.delete(export);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error deleting export" + e.getMessage());
        }
    }

}
