package com.angoor.project.service;

import com.angoor.project.repository.StudentRepo;
import com.angoor.project.repository.TeacherRepo;
import com.angoor.project.repository.LogRepo;
import com.angoor.project.repository.PaymentRepo;

import java.util.HashMap;
import java.util.LinkedList;

import com.angoor.project.model.Student;
import com.angoor.project.model.Teacher;


public class MentorshipManager {
	
	HashMap<Teacher, LinkedList<Student>> studentTeacherMap;
	TeacherRepo teacherRepository;
	
    public MentorshipManager() {
        this.studentTeacherMap = new HashMap<>();
        teacherRepository = TeacherRepo.getInstance();
        
        for (Teacher t : teacherRepository.getAllTeachers()) {
        	addTeacher(t);
        }
    }
    
    public void addTeacher(Teacher teacher) {
    	studentTeacherMap.putIfAbsent(teacher, new LinkedList<>());//if teacher is not in the map already, it adds him
    }

    public void addStudentToTeacher(Teacher teacher, Student student) {
    	studentTeacherMap.get(teacher).add(student);	//get ftn returns the linked list and then .add concatenates the student there
    }
	
    
}

