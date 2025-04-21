package io.foodapp.server.dtos.Filter;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierFilter {
    private String name;             // Tìm theo tên (partial match)
    private String phone;            // Tìm chính xác số điện thoại
    private String email;            // Tìm chính xác hoặc chứa chuỗi
    private String address;  
    
    @Builder.Default
    private Boolean isActive = true;     // Đang cung cấp hay không
}