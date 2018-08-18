package com.capgemini.storeserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.capgemini.storeserver.beans.Merchant;


@Repository
public interface MerchantRepo extends JpaRepository<Merchant, Integer>{

	@Query("SELECT m FROM Merchant m WHERE m.email =:username")
	public Merchant findByUsername(@Param("username") String username);
}
