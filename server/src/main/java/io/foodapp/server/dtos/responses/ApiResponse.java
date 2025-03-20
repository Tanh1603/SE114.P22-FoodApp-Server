package io.foodapp.server.dtos.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private final int status;
    private final String message;
    private T data;

    public ApiResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.data = null;
    }
}