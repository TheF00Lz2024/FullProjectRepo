package com.cognizant.repository;

import com.cognizant.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    @Query(value = "SELECT username, password FROM user WHERE username=:username AND password=:password", nativeQuery = true)
    List<User> loginUser(@Param("username")String username, @Param("password") String password);

}
