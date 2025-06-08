package io.foodapp.server.mappers.Report;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import io.foodapp.server.dtos.Report.DailyReportResponse;
import io.foodapp.server.models.Report.DailyReport;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DailyReportMapper {
    DailyReportResponse toDTO(DailyReport entity);

    List<DailyReportResponse> toDTOs(List<DailyReport> entities);
}
