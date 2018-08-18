package com.capgemini.storeserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capgemini.storeserver.beans.Address;


public interface AddressRepo  extends JpaRepository<Address, Integer>{

}