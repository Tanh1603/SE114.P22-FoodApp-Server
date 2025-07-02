package io.foodapp.server.models.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import io.foodapp.server.models.Order.OrderItem;
import io.foodapp.server.utils.ImageInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
@Table(name = "feedbacks")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private String customerId;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    @Builder.Default
    private List<ImageInfo> images = new ArrayList<>();

    private int rating;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updatedAt;
}
