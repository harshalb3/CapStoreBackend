package com.capgemini.storeserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capgemini.storeserver.beans.Admin;

@Repository
public interface AdminRepo extends JpaRepository<Admin, Integer>{

}
