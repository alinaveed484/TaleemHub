package com.angoor.project.service;

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
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MentorshipManager {

    //@Transient Can be used for attributes that you don't want the ORM to map to Columns in the DB
	private final StudentRepository studentRepository;
	private final TeacherRepository teacherRepository;

    private int studentCount = 0;
    private int teacherCount = 0;

    private static List<Student> studentsInMemory = new ArrayList<>();
    private static List<Teacher> teachersInMemory = new ArrayList<>();

	@Autowired
	public MentorshipManager(StudentRepository studentRepository, TeacherRepository teacherRepository) {
		this.studentRepository = studentRepository;
		this.teacherRepository = teacherRepository;
	}

	//create a table for student mentorship request to teacher (selectMentor use case)
    

    @PostConstruct
    public void init() {
        studentsInMemory = studentRepository.findAll();
        teachersInMemory = teacherRepository.findAll();
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

    public void assignTeacherToStudent(Integer studentId, Integer teacherId) {
        Student student = studentsInMemory.stream().filter(s -> s.getStudentId() == studentId).findFirst().orElse(null);
        Teacher teacher = teachersInMemory.stream().filter(t -> t.getTeacherId() == teacherId).findFirst().orElse(null);

        if (student != null && teacher != null) {
            student.getTeachers().add(teacher);
            teacher.getStudents().add(student);
            studentRepository.save(student);
            teacherRepository.save(teacher);
        }
    }
    
    public Map<String, Object> displayTeachers(String subject){
    	Map<String, Object> response = new HashMap<>();
    	for(Teacher t: teachersInMemory) {
    		if(subject.equals(t.getSubjectSpecialization())){
    			response.put(t.getFirstName()+t.getLastName(), t.getProfilePhotoURL());
    		}
    	}
    	return response;
    }
    
    public Map<String, Object> showTeacherDetails(Integer teacherID){
    	Map<String, Object> response = new HashMap<>();
    	for(Teacher t: teachersInMemory) {
    		if(teacherID == t.getTeacherId()) {
    			
    			response.put("teacherId", teacherID.toString());
    			response.put("firstName", t.getFirstName());
    			response.put("lastName", t.getLastName());
    			response.put("yearsExperience", t.getYearsExperience().toString());
    			response.put("email", t.getEmail());
    			response.put("phone", t.getPhone());
    			response.put("hireDate", t.getHireDate().toString());
    			response.put("subjectSpecialization", t.getSubjectSpecialization());
    			response.put("qualification", t.getQualification());
    			response.put("profilePhotoURL", t.getProfilePhotoURL());
    			break;
    		}
    	}
    	return response;
    }

	@Transactional
    public Map<String, Object> sendMentorRequest(Integer teacherID, Integer studentID){

    	Map<String, Object> response = new HashMap<>();
    	Teacher TT = null;
    	Student SS = null;
    	for(Teacher t:teachersInMemory) {
    		if(teacherID == t.getTeacherId()) {
    			TT = t;
    			break;
    		}
    	}
    	for(Student s:studentsInMemory) {
    		if(studentID == s.getStudentId()) {
    			SS = s;
    			break;
    		}
    	}
    	
    	SS.addMentorRequest(TT);
    	TT.addMentorRequest(SS);

		studentRepository.save(SS);
		teacherRepository.save(TT);
    	
    	return null;
    }
}

