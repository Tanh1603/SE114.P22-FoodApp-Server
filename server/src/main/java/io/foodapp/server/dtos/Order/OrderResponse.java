package io.foodapp.server.dtos.Order;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long id;
    private String customerId;
    private int tableNumber;
    private double voucherDiscount;
    private double totalPrice;
    private String staffName;
    private String status;

    private String paymentMethod;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate orderDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime createAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime paymentAt;

    private String note;
    private String address;

    @JsonProperty("isDeleted")
    private boolean isDeleted;

    private List<OrderItemResponse> orderItems;

}