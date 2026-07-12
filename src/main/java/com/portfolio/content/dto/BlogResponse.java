package com.portfolio.content.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlogResponse {
	private Long id;
	private String title;
	private String content;
	private Integer views;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
