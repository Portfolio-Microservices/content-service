package com.portfolio.content.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.content.dto.PaginationResponse;
import com.portfolio.content.entity.Skill;
import com.portfolio.content.service.SkillService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/skills")
@RequiredArgsConstructor
public class SkillController {

	private final SkillService service;

	@GetMapping
	public PaginationResponse<Skill> all(@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "5") Integer size) {
		return service.getAll(page, size);
	}

	@PostMapping
	public Skill create(@RequestBody Skill s) {
		return service.save(s);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		service.delete(id);
	}
}
