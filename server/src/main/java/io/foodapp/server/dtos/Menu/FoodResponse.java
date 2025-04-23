package io.foodapp.server.dtos.Menu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodResponse {
    private Long id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private int defaultQuantity;
    private int remainingQuantity;
    private boolean active;
}
