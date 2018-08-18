package com.capgemini.storeserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.capgemini.storeserver.beans.Review;

public interface ReviewRepo extends JpaRepository<Review, Integer>,CrudRepository<Review, Integer>{
	
}
