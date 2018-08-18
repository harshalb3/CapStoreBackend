package com.capgemini.storeserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.capgemini.storeserver.beans.Customer;
import com.capgemini.storeserver.beans.Wishlist;

public interface WishlistRepo extends JpaRepository<Wishlist, Integer>,CrudRepository<Wishlist, Integer>{

	public Wishlist findByCustomer(Customer customer);
}
