package com.angoor.project.service;

import com.angoor.project.repository.StudentRepo;
import com.angoor.project.repository.TeacherRepo;
import com.angoor.project.repository.LogRepo;
import com.angoor.project.repository.PaymentRepo;

import java.util.HashMap;
import java.util.LinkedList;

import com.angoor.project.model.Student;
import com.angoor.project.model.Teacher;


import com.angoor.project.model.Student;
import com.angoor.project.model.Teacher;
import com.angoor.project.repository.StudentRepository;
import com.angoor.project.repository.TeacherRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
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

    //@Transient Can be used for attributes that you don't want the ORM to map to Columns in the DB

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    private int studentCount = 0;
    private int teacherCount = 0;

    private List<Student> studentsInMemory = new ArrayList<>();
    private List<Teacher> teachersInMemory = new ArrayList<>();

    @PostConstruct
    public void init() {
        this.studentsInMemory = studentRepository.findAll();
        this.teachersInMemory = teacherRepository.findAll();
        this.studentCount = studentsInMemory.size();
        this.teacherCount = teachersInMemory.size();
    }

    public void addStudent(Student student) {
        studentsInMemory.add(student);
        studentRepository.save(student);
    }

    public void addTeacher(Teacher teacher) {
        teachersInMemory.add(teacher);
        teacherRepository.save(teacher);
    }

    public void assignTeacherToStudent(int studentId, int teacherId) {
        Student student = studentsInMemory.stream().filter(s -> s.getStudentId() == studentId).findFirst().orElse(null);
        Teacher teacher = teachersInMemory.stream().filter(t -> t.getTeacherId() == teacherId).findFirst().orElse(null);

        if (student != null && teacher != null) {
            student.getTeachers().add(teacher);
            teacher.getStudents().add(student);
            studentRepository.save(student);
            teacherRepository.save(teacher);
        }
    }
    public void addStudentToTeacher(Teacher teacher, Student student) {
    	studentTeacherMap.get(teacher).add(student);	//get ftn returns the linked list and then .add concatenates the student there
    }


}

