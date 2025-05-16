package io.foodapp.server.mappers.User;

import java.util.List;

import io.foodapp.server.utils.ImageInfo;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import io.foodapp.server.dtos.User.FeedbackRequest;
import io.foodapp.server.dtos.User.FeedbackResponse;
import io.foodapp.server.models.User.Feedback;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE, unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface  FeedbackMapper {
    Feedback toEntity(FeedbackRequest dto);

    FeedbackResponse toDTO(Feedback entity);

    List<Feedback> toEntity(List<FeedbackRequest> dto);

    List<FeedbackResponse> toDTO(List<Feedback> entity);

    void updatedEntityFromDTO(FeedbackResponse dto, @MappingTarget Feedback entity);

    ImageInfo map(ImageInfo imageInfo);
}
