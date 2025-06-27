package io.foodapp.server.services.Order;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import io.foodapp.server.dtos.Filter.FoodTableFilter;
import io.foodapp.server.dtos.Order.FoodTableRequest;
import io.foodapp.server.dtos.Order.FoodTableResponse;
import io.foodapp.server.dtos.Specification.FoodTableSpecification;
import io.foodapp.server.mappers.Order.FoodTableMapper;
import io.foodapp.server.models.Order.FoodTable;
import io.foodapp.server.models.Order.Order;
import io.foodapp.server.models.enums.FoodTableStatus;
import io.foodapp.server.models.enums.OrderStatus;
import io.foodapp.server.repositories.Order.FoodTableRepository;
import io.foodapp.server.repositories.Order.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class FoodTableService {
    private final FoodTableMapper foodTableMapper;
    private final FoodTableRepository foodTableRepository;
    private final OrderRepository orderRepository;

    public Page<FoodTableResponse> getFoodTables(Pageable pageable, FoodTableFilter filter) {
        try {
            Specification<FoodTable> specification = FoodTableSpecification.withFilter(filter);
            Page<FoodTable> coffeeTables = foodTableRepository.findAll(specification, pageable);
            return coffeeTables.map(foodTableMapper::toDTO);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching coffee table", e);
        }
    }

    public FoodTableResponse createFoodTable(FoodTableRequest coffeeTable) {
        try {
            return foodTableMapper.toDTO(foodTableRepository.save(foodTableMapper.toEntity(coffeeTable)));
        } catch (Exception e) {
            throw new RuntimeException("Error creating coffee table", e);
        }
    }

    public FoodTableResponse updateFoodTable(Integer id, FoodTableRequest coffeeTable) {
        try {
            FoodTable existingCoffeeTable = foodTableRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Coffee table not found for " + id));
            existingCoffeeTable.setTableNumber(coffeeTable.getTableNumber());
            existingCoffeeTable.setSeatCapacity(coffeeTable.getSeatCapacity());
            return foodTableMapper.toDTO(foodTableRepository.save(existingCoffeeTable));
        } catch (Exception e) {
            throw new RuntimeException("Error updating coffee table", e);
        }
    }

    public void setFoodTableStatus(Integer id) {
        try {
            FoodTable existingCoffeeTable = foodTableRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Coffee table not found for " + id));
            existingCoffeeTable.setActive(!existingCoffeeTable.isActive());
            foodTableRepository.save(existingCoffeeTable);
        } catch (Exception e) {
            throw new RuntimeException("Error updating coffee table status", e);
        }
    }

    public void deleteFoodTable(Integer id) {
        try {
            foodTableRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting coffee table", e);
        }
    }

    public FoodTableResponse createOrderForFoodTable(Integer id) {
        try {
            FoodTable existingFoodTable = foodTableRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Food table not found for " + id));
            existingFoodTable.setStatus(FoodTableStatus.OCCUPIED);
            Order order = Order.builder()
                    .table(existingFoodTable)
                    .status(OrderStatus.CONFIRMED)
                    .startedAt(LocalDateTime.now())
                    .build();
            orderRepository.save(order);
            return foodTableMapper.toDTO(foodTableRepository.save(existingFoodTable));
        } catch (Exception e) {
            throw new RuntimeException("Error creating order for food table", e);
        }
    }

    
}
