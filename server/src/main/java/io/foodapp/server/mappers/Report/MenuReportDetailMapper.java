package io.foodapp.server.mappers.Report;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import io.foodapp.server.dtos.Report.MenuReportDetailResponse;
import io.foodapp.server.models.Report.MenuReportDetail;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuReportDetailMapper {
    @Mapping(source = "menu.name", target = "menuName")
    MenuReportDetailResponse toDTO(MenuReportDetail menu);

    List<MenuReportDetailResponse> toDTOs(List<MenuReportDetail> menus);
    
}