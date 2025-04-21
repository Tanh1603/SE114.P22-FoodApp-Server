package io.foodapp.server.services.Menu;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import io.foodapp.server.dtos.Menu.UnitRequest;
import io.foodapp.server.dtos.Menu.UnitResponse;
import io.foodapp.server.mappers.Menu.UnitMapper;
import io.foodapp.server.models.MenuModel.Unit;
import io.foodapp.server.repositories.Menu.UnitRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UnitService {
    private final UnitRepository unitRepository;
    private final UnitMapper unitMapper;

    public List<UnitResponse> getActiveUnits() {
        try {
            return unitMapper.toDTOs(unitRepository.findByIsActiveTrue());
        } catch (RuntimeException e) {
            throw new RuntimeException("Error fetching unit data: " + e.getMessage());
        }
    }

    public List<UnitResponse> getInActiveUnits() {
        try {
            return unitMapper.toDTOs(unitRepository.findByIsActiveFalse());
        } catch (RuntimeException e) {
            throw new RuntimeException("Error fetching unit data: " + e.getMessage());
        }
    }

    @Transactional
    public UnitResponse createUnit(UnitRequest request) {
        try {
            Optional<Unit> optionalUnit = unitRepository.findByName(request.getName());

            if (optionalUnit.isPresent()) {
                Unit existingUnit = optionalUnit.get();

                if (!existingUnit.isActive()) {
                    existingUnit.setActive(true);
                    return unitMapper.toDTO(unitRepository.save(existingUnit));
                } else {
                    throw new IllegalArgumentException("Unit already exists.");
                }
            }

            Unit newUnit = unitMapper.toEntity(request);
            return unitMapper.toDTO(unitRepository.save(newUnit));

        } catch (RuntimeException e) {
            throw new RuntimeException("Error fetching unit data: " + e.getMessage());
        }
    }

    @Transactional
    public UnitResponse updateUnit(Long id, UnitRequest request) {
        try {
            var unit = unitRepository.findById(id).orElseThrow();
            unitMapper.updateEntityFromDto(request, unit);
            return unitMapper.toDTO(unitRepository.save(unit));
        } catch (RuntimeException e) {
            throw new RuntimeException("Error fetching unit data: " + e.getMessage());
        }
    }

    @Transactional
    public void setActiveUnit(Long id, boolean isActive) {
        try {
            Unit unit = unitRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Unit not found with id: " + id));
            unit.setActive(isActive);
            unitRepository.save(unit);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error fetching unit data: " + e.getMessage());

        }
    }

    @Transactional
    public void deleteUnit(Long id) {
        try {
            var unit = unitRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Unit not found with id: " + id));

            if (unit.getIngredients() != null && !unit.getIngredients().isEmpty()) {
                throw new RuntimeException("Cannot delete unit because it is being used by some ingredients.");
            }

            unitRepository.delete(unit);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error fetching unit data: " + e.getMessage());
        }
    }

}
