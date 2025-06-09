package io.foodapp.server.dtos.Order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.foodapp.server.utils.AddressInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {

    private Integer foodTableId;
    private Long voucherId;

    @NotBlank(message = "ServingType is required")
    private String type;

    @NotBlank(message = "OrderStatus is required")
    private String status;

    @NotBlank(message = "PaymentMethod is required")
    private String method;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime startedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime paymentAt;

    private String note;

    private AddressInfo address;

    private String sellerId;

    private String customerId;

    private String shipperId;

    @Pattern(regexp="^\\d{10}$", message="Phone number must consist of exactly 10 digits")
    private String phone;

    @Builder.Default
    private List<OrderItemRequest> orderItems = new ArrayList<>();
}
