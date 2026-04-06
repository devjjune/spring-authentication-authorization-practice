package com.back.domain.member.entity;

import com.back.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public Member(String username, String password, String nickname, String apiKey) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.apiKey = apiKey;
    }

    public Member(int id, String name) {
        this.setId(id);
        this.nickname = name;
    }

    public String getName() {
        return nickname;
    }
}
