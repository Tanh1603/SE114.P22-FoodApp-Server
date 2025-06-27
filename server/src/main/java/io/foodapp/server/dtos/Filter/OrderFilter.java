package io.foodapp.server.dtos.Filter;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.foodapp.server.models.enums.OrderStatus;
import io.foodapp.server.models.enums.PaymentMethod;
import io.foodapp.server.models.enums.ServingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderFilter {
    
    private String customerId;
    private String sellerId;
    private String shipperId;
    private OrderStatus status;
    private OrderStatus notStatus;
    private ServingType type;
    private PaymentMethod method;
    private Integer foodTableId;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate endDate;
}
