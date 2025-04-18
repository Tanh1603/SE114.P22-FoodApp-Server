package io.foodapp.server.dtos.Menu;

import java.math.BigDecimal;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItemResponse {
    private Integer id;
    private String menuName;
    private String name;
    private BigDecimal price;
    private String imageUrl;
}
