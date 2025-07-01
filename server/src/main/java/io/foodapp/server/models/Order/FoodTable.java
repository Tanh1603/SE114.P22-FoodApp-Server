package io.foodapp.server.models.Order;

import io.foodapp.server.models.enums.FoodTableStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "food_tables")
public class FoodTable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private int tableNumber;
    
    private int seatCapacity;
    
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private FoodTableStatus status = FoodTableStatus.EMPTY;

    @Builder.Default
    private boolean active = true;
}
