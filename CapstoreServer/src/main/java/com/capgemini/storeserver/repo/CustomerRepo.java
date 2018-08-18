package com.capgemini.storeserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.capgemini.storeserver.beans.Customer;



public interface CustomerRepo extends JpaRepository<Customer, String>,CrudRepository<Customer, String>{
	public Customer findByEmail(String email);
}

