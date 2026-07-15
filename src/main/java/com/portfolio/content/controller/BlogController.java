package com.portfolio.content.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.content.dto.BlogResponse;
import com.portfolio.content.dto.CreateBlogRequest;
import com.portfolio.content.dto.PaginationResponse;
import com.portfolio.content.service.BlogService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/blog")
@RequiredArgsConstructor
@Validated
public class BlogController {

	private final BlogService service;

	@GetMapping("/blogs")
	public ResponseEntity<PaginationResponse<BlogResponse>> getAllBlogs(@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size) {
		return ResponseEntity.ok(service.getAllBlogs(page, size));
	}

	@PostMapping("/admin/blogs")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BlogResponse> createBlog(@Valid @RequestBody CreateBlogRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.createBlog(request));
	}

	@PostMapping("/blogs/{id}/views")
	public ResponseEntity<BlogResponse> incrementView(@PathVariable Long id) {
		return ResponseEntity.ok(service.incrementViewCount(id));
	}
}
