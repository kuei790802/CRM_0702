package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//import com.example.demo.entity.Users;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

}
