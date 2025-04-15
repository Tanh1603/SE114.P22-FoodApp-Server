package io.foodapp.server.services.Order;

import java.util.List;

import org.springframework.stereotype.Service;

import io.foodapp.server.dtos.Order.FoodTableRequest;
import io.foodapp.server.dtos.Order.FoodTableResponse;
import io.foodapp.server.mappers.Order.FoodTableMapper;
import io.foodapp.server.models.Order.FoodTable;
import io.foodapp.server.repositories.Order.FoodTableRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FoodTableService {
    private final FoodTableRepository coffeeTableRepository;
    private final FoodTableMapper coffeeTableMapper;

    public List<FoodTableResponse> getCoffeTablesAvailable() {
        try {
            return coffeeTableMapper.toDTOs(coffeeTableRepository.findByIsDeletedFalse());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching coffee tables", e);
        }
    }

    public List<FoodTableResponse> getCoffeTablesDeleted() {
        try {
            return coffeeTableMapper.toDTOs(coffeeTableRepository.findByIsDeletedTrue());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching coffee table", e);
        }
    }

    public FoodTableResponse createCoffeeTable(FoodTableRequest coffeeTable) {
        try {
            return coffeeTableMapper.toDTO(coffeeTableRepository.save(coffeeTableMapper.toEntity(coffeeTable)));
        } catch (Exception e) {
            throw new RuntimeException("Error creating coffee table", e);
        }
    }


    public FoodTableResponse updateCoffeeTable(Long id, FoodTableRequest coffeeTable) {
        try {
            FoodTable existingCoffeeTable = coffeeTableRepository.findById(id).orElseThrow(() -> new RuntimeException("Coffee table not found"));
            existingCoffeeTable.setTableNumber(coffeeTable.getTableNumber());
            existingCoffeeTable.setSeatCapacity(coffeeTable.getSeatCapacity());
            return coffeeTableMapper.toDTO(coffeeTableRepository.save(existingCoffeeTable));
        } catch (Exception e) {
            throw new RuntimeException("Error updating coffee table", e);
        }
    }

    public void deleteCoffeeTable(Long id) {
        try {
            FoodTable delete =  coffeeTableRepository.findById(id).orElseThrow(() -> new RuntimeException("Coffee table not found"));
            delete.setDeleted(true);
            coffeeTableRepository.save(delete);

        } catch (Exception e) {
            throw new RuntimeException("Error deleting coffee table", e);
        }
    }
}
