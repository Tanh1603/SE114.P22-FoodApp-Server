package io.foodapp.server.dtos.User;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDTO {
    private Long id;

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Format address is required")
    private String formatAddress;

    private double latitude;

    private double longitude;

    @NotBlank(message = "Place ID is required")
    private String placeId;

    @JsonProperty("isDefault")
    private boolean isDefault;
    
    @JsonProperty("isDeleted")
    private boolean deleted;
}
