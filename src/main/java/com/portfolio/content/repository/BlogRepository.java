package com.portfolio.content.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portfolio.content.entity.Blog;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

	Page<Blog> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
