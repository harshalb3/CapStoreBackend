package com.capgemini.storeserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capgemini.storeserver.beans.Cart;
import com.capgemini.storeserver.beans.Customer;



public interface CartRepo extends JpaRepository<Cart, Integer>{
	public Cart findByCustomer(Customer customer);
}
