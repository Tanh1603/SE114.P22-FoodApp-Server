package io.foodapp.server.dtos.requests;

import io.foodapp.server.models.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    @NotNull
    @NotBlank
    private String username;

    @NotNull
    @NotBlank
    @Size(min = 6)
    private String password;

    private String avatar;

    @NotNull
    @NotBlank
    private String fullName;

    @NotNull
    @NotBlank
    private String phone;

    @Email
    private String email;

    @NotNull
    @NotBlank
    private String address;

    @NotNull
    private Role role;

    private StaffRequest staff;
}
