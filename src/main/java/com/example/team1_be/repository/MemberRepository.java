package com.example.team1_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
