package io.foodapp.server.models.MenuModel;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import io.foodapp.server.utils.ImageInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "foods")
@EntityListeners(AuditingEntityListener.class)
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    private String name;
    private String description;
    private double price;
    private int defaultQuantity;
    private int remainingQuantity;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    @Builder.Default
    private List<ImageInfo> images = new ArrayList<>();

    @Builder.Default
    private boolean active = true;

    @Builder.Default
    private double totalRating = 0;

    @Builder.Default
    private int totalFeedbacks = 0;

    @Builder.Default
    private int totalLikes = 0;

}
