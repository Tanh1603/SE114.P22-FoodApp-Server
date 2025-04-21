package io.foodapp.server.dtos.Menu;

import org.springframework.web.multipart.MultipartFile;

import com.google.auto.value.AutoValue.Builder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItemRequest {
    private Long menuId;

    @NotBlank(message = "Menu item name is required")
    private String name;
    
    private String description;

    @NotNull(message = "Menu item price is required")
    private double price;

    private MultipartFile imageUrl;
}
