package com.tripbook.main.global.entity;

import com.tripbook.main.global.common.BasicEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "TB_IMAGE")
@Getter
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
public class Image extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String name;

    @Builder
    public Image(String url, String name) {
        this.url = url;
        this.name = name;
    }
}
