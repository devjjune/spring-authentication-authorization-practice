package com.back.domain.member.entity;

import com.back.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter // Setter는 엔티티에 비추
@NoArgsConstructor
public class Member extends BaseEntity {

    @Column(unique = true) // username 값은 중복X (유일한 회원 식별자)
    private String username;
    private String password;
    private String nickname;
    @Column(unique = true)
    private String apiKey;

    public Member(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.apiKey = UUID.randomUUID().toString(); // apiKey 값 부여 (의미 없고 무작위한 값으로)
    }

    public String getName() {
        return nickname;
    }
}
