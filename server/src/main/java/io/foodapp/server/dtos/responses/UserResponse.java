package io.foodapp.server.dtos.responses;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    private Long id;

    private String avatar;

    private String username;

    private String fullName;

    private String phone;

    private String email;

    private String address;

    private LocalDateTime createdAt;

    private boolean isActive;

    private boolean isDeleted;

    private String role;

    private StaffResponse staff;

}
