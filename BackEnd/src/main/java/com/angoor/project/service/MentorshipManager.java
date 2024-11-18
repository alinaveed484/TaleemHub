package com.angoor.project.service;

import com.angoor.project.dto.TeacherDto;
import com.angoor.project.model.Person;
import com.angoor.project.model.PersonDTO;
import com.angoor.project.model.Student;
import com.angoor.project.model.Teacher;
import com.angoor.project.repository.PersonRepo;
import com.angoor.project.repository.StudentRepository;
import com.angoor.project.repository.TeacherRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.CloseableThreadContext;
import org.hibernate.Hibernate;
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



	public List<TeacherDto> displayTeachers(String subject) {
		List<TeacherDto> response = new ArrayList<>();
		for (Teacher t : teachersInMemory) {
			if (subject.equals(t.getSubjectSpecialization())) {
				TeacherDto teacherDTO = new TeacherDto(
						t.getFirstName(),
						t.getLastName(),
						t.getProfilePhotoURL(),
						t.getYearsExperience(),
						t.getHireDate(),
						t.getSubjectSpecialization(),
						t.getQualification()
				);
				response.add(teacherDTO);
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
	public void connect(PersonDTO personDTO){
		if (personDTO.getType().equals("Student"))
		{
			Student s = studentRepository.findById(personDTO.getId()).orElse(null);
			if (s != null)
			{
				s.setStatus(true);
				studentRepository.save(s);
			}
		}
		else{
			Teacher t = teacherRepository.findById(personDTO.getId()).orElse(null);
			if (t != null)
			{
				t.setStatus(true);
				teacherRepository.save(t);
			}
		}


	}

	@MessageMapping("/user.addUser")
	@SendTo("/user/topic")
	public void disconnect(PersonDTO personDTO){

		if (personDTO.getType().equals("Teacher"))
		{
			Student s = studentRepository.findById(personDTO.getId()).orElse(null);
			if (s != null) {
				s.setStatus(false);
				studentRepository.save(s);
			}
		}
		else{
			Teacher t = teacherRepository.findById(personDTO.getId()).orElse(null);
			if (t != null) {
				t.setStatus(false);
				teacherRepository.save(t);
			}
		}

	}



	public Set<PersonDTO> getConnectedUsersDTO(Integer personID, String person_type) {
		// Get connected users (students for a teacher, teachers for a student)
		Set<PersonDTO> connectedUsers = displayConnectedUsers(personID, person_type);

		// Convert each Person to PersonDTO and collect into a set
		return connectedUsers;
	}

	private PersonDTO convertToDTO(Person person) {
		String type = (person.getId() != null) ? teacherRepository.findPersonTypeById(person.getId()) : null;
		return new PersonDTO(
				person.getId(),
				person.getFirstName(),
				person.getLastName(),
				person.getStatus(),
				type
		);
	}

	public Set<PersonDTO> displayConnectedUsers(Integer personID, String person_type) {

		if ("Student".equals(person_type)) {
			Student student = studentRepository.findById(personID).orElse(null);
			if (student != null) {
				// Map each Teacher to a PersonDTO
				return student.getTeachers().stream()
						.map(teacher -> new PersonDTO(
								teacher.getId(),
								teacher.getFirstName(),
								teacher.getLastName(),
								teacher.isStatus(),
								"Teacher" // Assuming getPersonType() returns "Teacher"
						))
						.collect(Collectors.toSet());
			}
		}

		if ("Teacher".equals(person_type)) {
			Teacher teacher = teacherRepository.findById(personID).orElse(null);
			if (teacher != null) {
				// Map each Student to a PersonDTO
				return teacher.getStudents().stream()
						.map(student -> new PersonDTO(
								student.getId(),
								student.getFirstName(),
								student.getLastName(),
								student.isStatus(),
								"Student" // Assuming getPersonType() returns "Student"
						))
						.collect(Collectors.toSet());
			}
		}

		return Collections.emptySet(); // Return empty set if person type is unrecognized or person not found
	}



	@Transactional
	public Map<String, Object> displayStudents(Integer teacherId) {
		Map<String, Object> response = new HashMap<>();
		Teacher teacher = teacherRepository.findById(teacherId).orElse(null);

		if (teacher != null) {
			// Initialize the lazy-loaded studentRequests collection
			Hibernate.initialize(teacher.getStudentRequests());

			// Convert the Set<Student> to a Map where student ID is the key and PersonDTO is the value
			Map<Integer, PersonDTO> studentDTOMap = teacher.getStudentRequests().stream()
					.map(student -> new PersonDTO(
							student.getId(),
							student.getFirstName(),
							student.getLastName(),
							student.isStatus(),
							student.getPersonType()
					))
					.collect(Collectors.toMap(PersonDTO::getId, dto -> dto));

			response.put("students", studentDTOMap);
		} else {
			response.put("error", "Teacher not found");
		}

		return response;
	}

	public Map<String, Object> showStudentRequest(Integer teacherID, Integer studentID) {
		Map<String, Object> response = new HashMap<>();

		Teacher teacher = teacherRepository.findById(teacherID).orElse(null);

		if (teacher != null) {
			Set<Student> students = teacher.getStudentRequests();

			// Find the student with the matching ID and convert to PersonDTO
			PersonDTO matchingStudentDTO = students.stream()
					.filter(student -> student.getId().equals(studentID))
					.map(student -> new PersonDTO(
							student.getId(),
							student.getFirstName(),
							student.getLastName(),
							student.isStatus(),
							student.getPersonType()
					))
					.findFirst()
					.orElse(null);

			if (matchingStudentDTO != null) {
				response.put("student", matchingStudentDTO);
			} else {
				response.put("error", "Student with the specified ID not found in teacher's requests");
			}
		} else {
			response.put("error", "Teacher not found");
		}

		return response;
	}

	public Map<String, Object> acceptStudent(Integer teacherID, Integer studentID) {
		Map<String, Object> response = new HashMap<>();

		assignTeacherToStudent(teacherID, studentID);

		response.put("value", "Success!");
		return response;
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
}

