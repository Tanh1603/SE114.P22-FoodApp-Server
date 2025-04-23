package io.foodapp.server.dtos.User;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponse {
    private Long id;
    private String userId;
    private String name;
    private String formatAddress;
    private double latitude;
    private double longitude;
    private String placeId;
    private boolean defaultAddress;
}
