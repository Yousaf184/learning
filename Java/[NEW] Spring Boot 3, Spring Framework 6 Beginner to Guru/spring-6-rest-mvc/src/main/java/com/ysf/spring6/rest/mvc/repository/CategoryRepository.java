package com.ysf.spring6.rest.mvc.repository;

import com.ysf.spring6.rest.mvc.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
}