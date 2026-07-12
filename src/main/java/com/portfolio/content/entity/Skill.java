package com.portfolio.content.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Skill {
	@Id
	@GeneratedValue
	private Long id;

	private String name;
	private int level; // 1-100
}
