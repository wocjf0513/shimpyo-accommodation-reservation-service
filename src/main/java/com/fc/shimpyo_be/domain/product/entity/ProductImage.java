package com.fc.shimpyo_be.domain.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("숙소 이미지 식별자")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "product_id")
    @Comment("숙소 식별자")
    private Product product;
    @Column(nullable = false, columnDefinition = "TEXT")
    @Comment("숙소 사진 URL")
    private String photoUrl;

    @Builder
    public ProductImage(Long id, Product product, String photoUrl) {
        this.id = id;
        this.product = product;
        this.photoUrl = photoUrl;
    }
}
