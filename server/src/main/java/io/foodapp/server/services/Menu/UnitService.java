package io.foodapp.server.services.Menu;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import io.foodapp.server.dtos.Menu.UnitRequest;
import io.foodapp.server.dtos.Menu.UnitResponse;
import io.foodapp.server.mappers.Menu.UnitMapper;
import io.foodapp.server.models.MenuModel.Unit;
import io.foodapp.server.repositories.Menu.UnitRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UnitService {
    private final UnitRepository unitRepository;
    private final UnitMapper unitMapper;

    public List<UnitResponse> getAvailableUnits() {
        try {
            return unitMapper.toDTOs(unitRepository.findByIsDeletedFalse());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching unit data: " + e.getMessage());        }
    }

    public List<UnitResponse> getDeletedUnits() {
        try {
            return unitMapper.toDTOs(unitRepository.findByIsDeletedTrue());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching unit data: " + e.getMessage());        }
    }

    public UnitResponse getUnitById(Long id) {
        try {
            return unitMapper.toDTO(unitRepository.findById(id).orElseThrow());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching unit data: " + e.getMessage());        }
    }

    public UnitResponse createUnit(UnitRequest request) {
        try {
            Optional<Unit> optionalUnit = unitRepository.findByName(request.getName());
    
            if (optionalUnit.isPresent()) {
                Unit existingUnit = optionalUnit.get();
    
                if (existingUnit.isDeleted()) {
                    existingUnit.setDeleted(false);
                    return unitMapper.toDTO(unitRepository.save(existingUnit));
                } else {
                    throw new IllegalArgumentException("Unit already exists.");
                }
            }
    
            Unit newUnit = unitMapper.toEntity(request);
            return unitMapper.toDTO(unitRepository.save(newUnit));
    
        } catch (Exception e) {
            throw new RuntimeException("Error fetching unit data: " + e.getMessage());
        }
    }
    


    public UnitResponse updateUnit(Long id, UnitRequest request) {
        try {
            var unit = unitRepository.findById(id).orElseThrow();
            unitMapper.updateEntityFromDto(request, unit);
            return unitMapper.toDTO(unitRepository.save(unit));
        } catch (Exception e) {
            throw new RuntimeException("Error fetching unit data: " + e.getMessage());
        }
    }

    public void deleteUnit(Long id) {
        try {
            var existingUnit = unitRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Unit not found with id: " + id));

            existingUnit.setDeleted(true);
            unitRepository.save(existingUnit);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching unit data: " + e.getMessage());
        }
    }

    public void recoverUnit(Long id) {
        Unit unit = unitRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Unit not found"));
    
        if (!unit.isDeleted()) {
            throw new IllegalStateException("Unit is not deleted");
        }
    
        unit.setDeleted(false);
    }
    
}
