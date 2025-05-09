package io.foodapp.server.dtos.User;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherRequest {
    private String code;
    private double value;
    private Double minOrderPrice;
    private Double maxValue;
    private int total;

    @NotBlank(message = "Type is required")
    private String type;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @FutureOrPresent(message = "Start date cannot be in the past")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @FutureOrPresent(message = "End date cannot be in the past")
    private LocalDate endDate;

    private boolean published;

    public boolean isStartDateBeforeEndDate() {
        if (startDate != null && endDate != null) {
            return startDate.isBefore(endDate);
        }
        return true;
    }

}
