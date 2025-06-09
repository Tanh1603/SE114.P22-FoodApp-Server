package io.foodapp.server.utils;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressInfo {
    private String formatAddress;
    private Optional<Double> latitude;
    private Optional<Double> longtitude;
}
