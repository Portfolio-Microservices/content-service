package com.portfolio.content.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portfolio.content.entity.Skill;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
}
