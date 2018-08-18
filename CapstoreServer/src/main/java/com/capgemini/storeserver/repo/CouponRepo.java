package com.capgemini.storeserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capgemini.storeserver.beans.Coupon;


public interface CouponRepo extends JpaRepository<Coupon, Integer>{

}