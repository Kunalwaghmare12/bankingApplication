package com.kunal.bankingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kunal.bankingapp.entity.User;

public interface UserRepository extends JpaRepository<User,Long> {
    

}
