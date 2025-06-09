package io.foodapp.server.dtos.Menu;

import io.foodapp.server.utils.ImageInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodResponse {
    private Long id;
    private String name;
    private String description;
    private double price;
    private List<ImageInfo> images;
    private int defaultQuantity;
    private int remainingQuantity;
    private boolean active;
    private double totalRating;
    private int totalFeedbacks;
    private int totalLikes;
    private boolean liked;
}
