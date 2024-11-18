package com.angoor.project.repository;

import com.angoor.project.model.Person;
import com.angoor.project.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    @Query(value = "SELECT person_type FROM person WHERE id = :id", nativeQuery = true)
    String findPersonTypeById(@Param("id") Integer id);
}
