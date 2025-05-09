package io.foodapp.server.services.Order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final FoodTableRepository foodTableRepository;

    public Page<FoodTableResponse> getCoffeeTables(Pageable pageable) {
        try {
            Page<FoodTable> coffeeTables = coffeeTableRepository.findAll(pageable);
            return coffeeTables.map(coffeeTableMapper::toDTO);
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


    public FoodTableResponse updateCoffeeTable(Integer id, FoodTableRequest coffeeTable) {
        try {
            FoodTable existingCoffeeTable = coffeeTableRepository.findById(id).orElseThrow(() -> new RuntimeException("Coffee table not found for " + id));
            existingCoffeeTable.setTableNumber(coffeeTable.getTableNumber());
            existingCoffeeTable.setSeatCapacity(coffeeTable.getSeatCapacity());
            return coffeeTableMapper.toDTO(coffeeTableRepository.save(existingCoffeeTable));
        } catch (Exception e) {
            throw new RuntimeException("Error updating coffee table", e);
        }
    }

    public  void  setCoffeeTableStatus(Integer id, boolean status) {
        try {
            FoodTable existingCoffeeTable = coffeeTableRepository.findById(id).orElseThrow(() -> new RuntimeException("Coffee table not found for " + id));
            existingCoffeeTable.setActive(status);
            coffeeTableRepository.save(existingCoffeeTable);
        }
        catch (Exception e) {
            throw new RuntimeException("Error updating coffee table status", e);
        }
    }

    public void deleteCoffeeTable(Integer id) {
        try {
            foodTableRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting coffee table", e);
        }
    }
}
