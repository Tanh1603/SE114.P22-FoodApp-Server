package io.foodapp.server.services.Menu;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.foodapp.server.dtos.Menu.UnitDTO;
import io.foodapp.server.mappers.Menu.UnitMapper;
import io.foodapp.server.models.MenuModel.Unit;
import io.foodapp.server.repositories.Menu.UnitRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UnitService {
    private final UnitRepository unitRepository;
    private final UnitMapper unitMapper;

    public List<UnitDTO> getAllUnits() {
        try {
            return unitRepository.findAll().stream()
                .filter(unit -> !unit.isDeleted())
                .map(unitMapper::toDTO)
                .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public UnitDTO getUnitById(Long id) {
        try {
            return unitMapper.toDTO(unitRepository.findById(id).orElseThrow());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public UnitDTO createUnit(UnitDTO unitDTO) {
        try {
            return unitMapper.toDTO(unitRepository.save(unitMapper.toEntity(unitDTO)));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public UnitDTO updateUnit(UnitDTO unitDTO) {
        try {
            var unit = unitRepository.findById(unitDTO.getId()).orElseThrow();
            unitMapper.updateEntityFromDto(unitDTO, unit);
            return unitMapper.toDTO(unitRepository.save(unit));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean deleteUnit(Long id) {
        try {
            Optional<Unit> entity = unitRepository.findById(id);
            if (entity.isPresent()) {
                entity.get().setDeleted(true);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }
}
