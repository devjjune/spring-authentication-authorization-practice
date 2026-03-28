package com.back.domain.member.repository;

import com.back.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findByUsername(String username);
    // Username은 있을 수도 없을 수도 있으니까 Optional 처리
    Optional<Member> findByApiKey(String ApiKey);
}
