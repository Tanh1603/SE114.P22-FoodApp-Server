package io.foodapp.server.dtos.User;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressRequest {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Format address is required")
    private String formatAddress;
    private double latitude;
    private double longitude;
    @NotBlank(message = "Place ID is required")
    private String placeId;
    private boolean defaultAddress;
}
