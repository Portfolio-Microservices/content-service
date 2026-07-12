package com.portfolio.content.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.portfolio.content.dto.PaginationResponse;
import com.portfolio.content.entity.Skill;
import com.portfolio.content.repository.SkillRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SkillService {

	private final SkillRepository repo;

	private static final int DEFAULT_PAGE_SIZE = 20;
	private static final int MAX_PAGE_SIZE = 100;

	@Cacheable(value = "skills", key = "{#pageNumber, #pageSize}")
	public PaginationResponse<Skill> getAll(Integer pageNumber, Integer pageSize) {
		log.debug("Retrieving skills with pagination - page: {}, size: {}", pageNumber, pageSize);

		// Validate pagination parameters
		pageNumber = pageNumber == null || pageNumber < 0 ? 0 : pageNumber;
		pageSize = validatePageSize(pageSize);

		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<Skill> skills = repo.findAll(pageable);
		log.info(">>> CACHE TEST <<<");
		PaginationResponse<Skill> response = PaginationResponse.<Skill>builder().content(skills.getContent())
				.pageNumber(skills.getNumber()).pageSize(skills.getSize()).totalElements(skills.getTotalElements())
				.totalPages(skills.getTotalPages()).isFirst(skills.isFirst()).isLast(skills.isLast())
				.hasNext(skills.hasNext()).hasPrevious(skills.hasPrevious()).build();

		log.info("Retrieved {} skills out of {} total", response.getContent().size(), response.getTotalElements());
		return response;
	}

	@CacheEvict(value = "skills", allEntries = true)
	public Skill save(Skill s) {
		log.info("Saving skill: {}", s.getName());
		Skill saved = repo.save(s);
		log.info("Skill saved with ID: {}", saved.getId());
		return saved;
	}

	@CacheEvict(value = "skills", allEntries = true)
	public void delete(Long id) {
		log.info("Deleting skill with ID: {}", id);
		repo.deleteById(id);
		log.info("Skill deleted with ID: {}", id);
	}

	private int validatePageSize(Integer pageSize) {
		if (pageSize == null || pageSize <= 0) {
			return DEFAULT_PAGE_SIZE;
		}
		return Math.min(pageSize, MAX_PAGE_SIZE);
	}
}
