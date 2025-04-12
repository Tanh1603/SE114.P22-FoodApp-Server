package io.foodapp.server.services.Menu;

import java.util.List;
import java.util.Optional;

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

    public List<UnitDTO> getAvailableUnits() {
        try {
            return unitMapper.toDtoList(unitRepository.findByIsDeletedFalse());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching unit data: " + e.getMessage());        }
    }

    public List<UnitDTO> getDeletedUnits() {
        try {
            return unitMapper.toDtoList(unitRepository.findByIsDeletedTrue());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching unit data: " + e.getMessage());        }
    }

    public UnitDTO getUnitById(Long id) {
        try {
            return unitMapper.toDTO(unitRepository.findById(id).orElseThrow());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching unit data: " + e.getMessage());        }
    }

    public UnitDTO createUnit(UnitDTO unitDTO) {
        try {
            Optional<Unit> optionalUnit = unitRepository.findByName(unitDTO.getName());
    
            if (optionalUnit.isPresent()) {
                Unit existingUnit = optionalUnit.get();
    
                if (existingUnit.isDeleted()) {
                    existingUnit.setDeleted(false);
                    return unitMapper.toDTO(unitRepository.save(existingUnit));
                } else {
                    throw new IllegalArgumentException("Unit already exists.");
                }
            }
    
            Unit newUnit = unitMapper.toEntity(unitDTO);
            return unitMapper.toDTO(unitRepository.save(newUnit));
    
        } catch (Exception e) {
            throw new RuntimeException("Error fetching unit data: " + e.getMessage());
        }
    }
    


    public UnitDTO updateUnit(UnitDTO unitDTO) {
        try {
            var unit = unitRepository.findById(unitDTO.getId()).orElseThrow();
            unitMapper.updateEntityFromDto(unitDTO, unit);
            return unitMapper.toDTO(unitRepository.save(unit));
        } catch (Exception e) {
            throw new RuntimeException("Error fetching unit data: " + e.getMessage());
        }
    }

    public boolean deleteUnit(Long id) {
        try {
            var existingUnit = unitRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Unit not found with id: " + id));

            existingUnit.setDeleted(true);
            unitRepository.save(existingUnit);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching unit data: " + e.getMessage());
        }
    }

    public UnitDTO recoverUnit(Long id) {
        Unit unit = unitRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Unit not found"));
    
        if (!unit.isDeleted()) {
            throw new IllegalStateException("Unit is not deleted");
        }
    
        unit.setDeleted(false);
        return unitMapper.toDTO(unitRepository.save(unit));
    }
    
}
