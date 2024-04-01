package com.dosu04.memoWebApp.repositories;
import com.dosu04.memoWebApp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public User findByUsername(String username);
    List<User> findByUsernameContainingIgnoreCaseOrSurnameContainingIgnoreCaseOrNameContainingIgnoreCaseOrOtherNameContainingIgnoreCaseOrDepartment_NameContainingIgnoreCaseOrFaculty_NameContainingIgnoreCase(String username, String surname, String name, String otherName, String departmentName, String facultyName);


}

