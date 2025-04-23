package io.foodapp.server.mappers.User;

import io.foodapp.server.dtos.User.VoucherRequest;
import io.foodapp.server.dtos.User.VoucherResponse;
import io.foodapp.server.models.User.Voucher;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface VoucherMapper {
    Voucher toEntity(VoucherRequest dto);

    VoucherResponse toDTO(Voucher entity);

    List<Voucher> toEntities(List<VoucherRequest> dto);

    List<VoucherResponse> toDTO(List<Voucher> entity);

    void updateEntityFromDTO(VoucherRequest dto, @MappingTarget Voucher entity);
}
