package io.foodapp.server.dtos.Menu;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItemResponse {
    private Integer id;
    private String menuName;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
}
