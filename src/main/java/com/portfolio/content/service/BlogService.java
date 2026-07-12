package com.portfolio.content.service;

import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portfolio.content.dto.BlogResponse;
import com.portfolio.content.dto.CreateBlogRequest;
import com.portfolio.content.dto.PaginationResponse;
import com.portfolio.content.entity.Blog;
import com.portfolio.content.exception.ResourceNotFoundException;
import com.portfolio.content.repository.BlogRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlogService {

	private final BlogRepository repo;

	private static final int DEFAULT_PAGE_SIZE = 20;
	private static final int MAX_PAGE_SIZE = 100;

	@Transactional(readOnly = true)
	@Cacheable(value = "blogs", key = "#pageNumber + '_' + #pageSize")
	public PaginationResponse<BlogResponse> getAllBlogs(Integer pageNumber, Integer pageSize) {
		log.debug("Retrieving blogs with pagination - page: {}, size: {}", pageNumber, pageSize);

		// Validate pagination parameters
		pageNumber = pageNumber == null || pageNumber < 0 ? 0 : pageNumber;
		pageSize = validatePageSize(pageSize);

		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<Blog> blogs = repo.findAllByOrderByCreatedAtDesc(pageable);

		PaginationResponse<BlogResponse> response = buildPaginationResponse(blogs);
		log.info("Retrieved {} blogs out of {} total", response.getContent().size(), response.getTotalElements());

		return response;
	}

	@Transactional
	@CacheEvict(value = "blogs", allEntries = true)
	public BlogResponse createBlog(CreateBlogRequest request) {
		log.info("Creating new blog with title: {}", request.getTitle());

		Blog blog = Blog.builder().title(request.getTitle().trim()).content(request.getContent().trim()).views(0)
				.build();

		Blog saved = repo.save(blog);
		log.info("Blog created successfully with ID: {}", saved.getId());

		return mapToResponse(saved);
	}

	@Transactional
	@CacheEvict(value = "blogs", allEntries = true)
	public BlogResponse incrementViewCount(Long id) {
		log.debug("Incrementing view count for blog ID: {}", id);

		Blog blog = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Blog not found with ID: " + id));

		blog.setViews(blog.getViews() + 1);
		Blog saved = repo.save(blog);

		log.debug("View count incremented for blog ID: {}", id);
		return mapToResponse(saved);
	}

	private int validatePageSize(Integer pageSize) {
		if (pageSize == null || pageSize <= 0) {
			return DEFAULT_PAGE_SIZE;
		}
		return Math.min(pageSize, MAX_PAGE_SIZE);
	}

	private PaginationResponse<BlogResponse> buildPaginationResponse(Page<Blog> blogs) {
		return PaginationResponse.<BlogResponse>builder()
				.content(blogs.getContent().stream().map(this::mapToResponse).collect(Collectors.toList()))
				.pageNumber(blogs.getNumber()).pageSize(blogs.getSize()).totalElements(blogs.getTotalElements())
				.totalPages(blogs.getTotalPages()).isFirst(blogs.isFirst()).isLast(blogs.isLast())
				.hasNext(blogs.hasNext()).hasPrevious(blogs.hasPrevious()).build();
	}

	private BlogResponse mapToResponse(Blog blog) {
		return BlogResponse.builder().id(blog.getId()).title(blog.getTitle()).content(blog.getContent())
				.views(blog.getViews()).createdAt(blog.getCreatedAt()).updatedAt(blog.getUpdatedAt()).build();
	}
}
