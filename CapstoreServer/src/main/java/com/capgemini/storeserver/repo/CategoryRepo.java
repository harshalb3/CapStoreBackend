package com.capgemini.storeserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.capgemini.storeserver.beans.Category;


@Repository
public interface CategoryRepo extends JpaRepository<Category, Integer>{

	@Query(value="SELECT category from Category category where category.id=(:categoryId)")
	Category updateCategory(@Param(value="categoryId") int categoryId);

	public Category findByCategoryName(String categoryName);
}

