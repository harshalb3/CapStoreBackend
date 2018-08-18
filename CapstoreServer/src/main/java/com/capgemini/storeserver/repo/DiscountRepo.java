package com.capgemini.storeserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capgemini.storeserver.beans.Discount;

public interface DiscountRepo extends JpaRepository<Discount, Integer>{

}