package io.foodapp.server.dtos.User;


import java.util.List;

import javax.validation.constraints.NotNull;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

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
public class FeedbackRequest {
    @NotNull(message = "Order item ID is required")
    private Long orderItemId;
    private String content;
    @NotBlank(message = "Customer ID cannot be blank")
    private String customerId;
    private List<MultipartFile> images;
    @Min(value = 1, message = "Rating must be between 1 and 5")
    @Max(value = 5, message = "Rating must be between 1 and 5")
    private int rating;
}
