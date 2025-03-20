package io.foodapp.server.mappers;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;

public interface GenericMapper<E, D> {
    E toEntity(D dto);

    D toDTO(E entity);

    List<D> toDtoList(List<E> entities);

    List<E> toEntityList(List<D> dtos);

    @BeanMapping(ignoreByDefault = false)
    void updateEntityFromDto(D dto, @MappingTarget E entity);
}
