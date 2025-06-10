package io.foodapp.server.mappers.Report;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import io.foodapp.server.dtos.Report.MonthlyReportResponse;
import io.foodapp.server.models.Report.MonthlyReport;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MonthlyReportMapper {
    MonthlyReportResponse toDTO(MonthlyReport entity);

    List<MonthlyReportResponse> toDTOs(List<MonthlyReport> entities);
}
