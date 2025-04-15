package io.foodapp.server.dtos.Order;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class FoodTableRequest {

    private int tableNumber;

    private int seatCapacity;

    @JsonProperty("isDeleted")
    private boolean deleted;

}
