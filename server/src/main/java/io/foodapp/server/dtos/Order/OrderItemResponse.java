package io.foodapp.server.dtos.Order;


import io.foodapp.server.utils.ImageInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponse {
    private Long id;
    private String foodName;
    private double price;
    private int quantity;
    private ImageInfo foodImages;
}
