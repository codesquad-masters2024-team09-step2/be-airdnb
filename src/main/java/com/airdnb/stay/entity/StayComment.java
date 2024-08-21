package com.airdnb.stay.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StayComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stay_id")
    private Stay stay;
    private String writer;
    private String content;
    private LocalDateTime createdAt;
    private Float rating;
    @Enumerated(value = EnumType.STRING)
    private CommentStatus status;

    @Builder
    public StayComment(Stay stay, String writer, String content, LocalDateTime createdAt, Float rating,
                       CommentStatus status) {
        this.stay = stay;
        this.writer = writer;
        this.content = content;
        this.createdAt = createdAt;
        this.rating = rating;
        this.status = status;
    }
}
