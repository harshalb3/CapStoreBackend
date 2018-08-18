package com.capgemini.storeserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capgemini.storeserver.beans.Orders;

public interface OrdersRepo extends JpaRepository<Orders, Integer>{

}
