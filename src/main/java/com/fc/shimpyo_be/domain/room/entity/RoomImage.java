package com.fc.shimpyo_be.domain.room.entity;

import com.fc.shimpyo_be.domain.product.entity.Product;
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
public class RoomImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("객실 이미지 식별자")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "room_id")
    @Comment("객실 식별자")
    private Room room;
    @Column(nullable = false)
    @Comment("객실 이미지 URL")
    private String photoUrl;
    @Column(nullable = false)
    @Comment("객실 이미지 설명")
    private String description;

    @Builder
    private RoomImage(Long id, Room room, String photoUrl, String description) {
        this.id = id;
        this.room = room;
        this.photoUrl = photoUrl;
        this.description = description;
    }
}
