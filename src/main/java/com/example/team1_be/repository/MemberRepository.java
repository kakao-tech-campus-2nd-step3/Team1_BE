package com.example.team1_be.repository;

import com.example.team1_be.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
}
