package com.portfolio.content.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBlogRequest {

	@NotBlank(message = "Blog title is required")
	@Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters")
	private String title;

	@NotBlank(message = "Blog content is required")
	@Size(min = 10, max = 10000, message = "Content must be between 10 and 10000 characters")
	private String content;
}
