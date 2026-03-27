package com.back.domain.member.entity;

import com.back.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter // Setter는 엔티티에 비추
@NoArgsConstructor
public class Member extends BaseEntity {
    private String username;
    private String password;
    private String nickname;

    public Member(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }
}
