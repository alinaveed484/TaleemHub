package com.angoor.project.service;

import com.angoor.project.model.Person;
import com.angoor.project.model.PersonDTO;
import com.angoor.project.model.Student;
import com.angoor.project.model.Teacher;
import com.angoor.project.repository.PersonRepo;
import com.angoor.project.repository.StudentRepository;
import com.angoor.project.repository.TeacherRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MentorshipManager {

    //@Transient Can be used for attributes that you don't want the ORM to map to Columns in the DB
	private final StudentRepository studentRepository;
	private final TeacherRepository teacherRepository;
	private final PersonRepo personRepo;

    private int studentCount = 0;
    private int teacherCount = 0;

    private static List<Student> studentsInMemory = new ArrayList<>();
    private static List<Teacher> teachersInMemory = new ArrayList<>();

	@Autowired
	public MentorshipManager(StudentRepository studentRepository, TeacherRepository teacherRepository, PersonRepo personRepo) {
		this.studentRepository = studentRepository;
		this.teacherRepository = teacherRepository;
		this.personRepo = personRepo;
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
		// Find student and teacher using their IDs
		Student student = studentRepository.findById(studentId).orElse(null);
		Teacher teacher = teacherRepository.findById(teacherId).orElse(null);

		if (student != null && teacher != null) {
			// Add the teacher to the student's list of teachers
			student.getTeachers().add(teacher);

			// Add the student to the teacher's list of students
			teacher.getStudents().add(student);

			// Save both the student and the teacher to the database (JPA handles the ManyToMany association)
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

	public Map<String, Object> showTeacherDetails(Integer teacherID) {
		Map<String, Object> response = new HashMap<>();

		// Fetch Teacher entity from the database using the JPA repository
		Teacher teacher = teacherRepository.findById(teacherID).orElse(null);

		if (teacher != null) {
			// Populate the response map with teacher details
			response.put("teacherId", teacherID.toString());
			response.put("firstName", teacher.getFirstName());
			response.put("lastName", teacher.getLastName());
			response.put("yearsExperience", (teacher.getYearsExperience() != null) ? teacher.getYearsExperience().toString() : null);
			response.put("email", teacher.getEmail());
			response.put("phone", teacher.getPhone());
			response.put("hireDate", (teacher.getHireDate() != null) ? teacher.getHireDate().toString() : null);
			response.put("subjectSpecialization", teacher.getSubjectSpecialization());
			response.put("qualification", teacher.getQualification());
			response.put("profilePhotoURL", teacher.getProfilePhotoURL());
		} else {
			// Teacher not found
			response.put("Error", "Teacher not found");
		}

		return response;
	}


	@Transactional
	public Map<String, Object> sendMentorRequest(Integer teacherID, Integer studentID) {

		Map<String, Object> response = new HashMap<>();

		// Fetch Teacher and Student using JPA repositories
		Teacher teacher = teacherRepository.findById(teacherID).orElse(null);
		Student student = studentRepository.findById(studentID).orElse(null);

		if (teacher == null || student == null) {
			response.put("Error", "Teacher or Student ID not found");
			return response;
		}

		// Add the mentor request to the student and teacher
		student.addMentorRequest(teacher); // Assuming you have this logic in your model
		teacher.addMentorRequest(student); // Same for teacher

		// Save the entities back to the database
		studentRepository.save(student);
		teacherRepository.save(teacher);

		response.put("value", "Success!");
		return response;
	}

	// Chat Functionalities

	@MessageMapping("/user.addUser")
	@SendTo("/user/topic")
	public void connect(Person person){
		person.setStatus(true);
		Person p = personRepo.findById(person.getId()).orElse(null);

		if (p != null) {
			if (p instanceof Student) {
				Student s = (Student) p;
				s.setStatus(true);
				studentRepository.save(s);
			}
			else if (p instanceof Teacher) {
				Teacher t = (Teacher) p;
				t.setStatus(true);
				teacherRepository.save(t);
			}
		}


	}

	@MessageMapping("/user.addUser")
	@SendTo("/user/topic")
	public void disconnect(Person person){
		var p = personRepo.findById(person.getId()).orElse(null);

		if (p != null){
			p.setStatus(false);
			personRepo.save(p);
		}

	}

	// implement this after discussing with Ali
	// in case of student, this function will return a list of his registered teachers
	// in case of teacher, this function will return a list of his registered students
//	public Set<Person> displayConnectedUsers(Person person) {
//		if (person instanceof Student) {
//			Student student = (Student) person;
//			return new HashSet<>(student.getTeachers()); // returns Set<Teacher>
//		} else if (person instanceof Teacher) {
//			Teacher teacher = (Teacher) person;
//			return new HashSet<>(teacher.getStudents()); // returns Set<Student>
//		}
//		return Collections.emptySet();
//	}

	public Set<PersonDTO> getConnectedUsersDTO(Person person) {
		// Get connected users (students for a teacher, teachers for a student)
		Set<Person> connectedUsers = displayConnectedUsers(person);

		// Convert each Person to PersonDTO and collect into a set
		return connectedUsers.stream()
				.map(this::convertToDTO)
				.collect(Collectors.toSet());
	}

	private PersonDTO convertToDTO(Person person) {
		return new PersonDTO(
				person.getId(),
				person.getFirstName(),
				person.getLastName(),
				person.getStatus()
		);
	}

	public Set<Person> displayConnectedUsers(Person person) {
		if (person instanceof Student) {
			Student student = (Student) person;
			return new HashSet<>(student.getTeachers()); // Return Set<Teacher>
		} else if (person instanceof Teacher) {
			Teacher teacher = (Teacher) person;
			return new HashSet<>(teacher.getStudents()); // Return Set<Student>
		}
		return new HashSet<>(); // Return empty set if person type is unrecognized
	}



}

