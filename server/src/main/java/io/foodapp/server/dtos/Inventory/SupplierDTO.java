package io.foodapp.server.dtos.Inventory;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierDTO {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$",message = "Số điện thoại không hợp lệ. Phải có 10 chữ số, có thể bắt đầu bằng mã quốc gia")
    private String phone;

    @Email
    private String email;

    @NotBlank(message = "Address is required")
    private String address;
}
