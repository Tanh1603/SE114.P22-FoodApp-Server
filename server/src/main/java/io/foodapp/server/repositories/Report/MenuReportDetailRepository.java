package io.foodapp.server.repositories.Report;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.foodapp.server.models.Report.MenuReportDetail;

public interface MenuReportDetailRepository extends JpaRepository<MenuReportDetail, Integer> {

    List<MenuReportDetail> findByReportMonth(LocalDate reportMonth);

}
