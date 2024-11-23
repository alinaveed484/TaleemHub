package com.angoor.project.service;

import org.springframework.stereotype.Service;

import com.angoor.project.model.CalendarToken;
import com.angoor.project.model.Person;
import com.angoor.project.model.Student;
import com.angoor.project.model.Teacher;
import com.angoor.project.repository.CalendarTokenRepo;
import com.angoor.project.repository.PersonRepo;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.UserCredentials;

import jakarta.persistence.OneToOne;
import jakarta.transaction.Transactional;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Service
public class CalendarService {
    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;
    
	private final PersonRepo personRepository;
	private final CalendarTokenRepo tokenRepository;
	
	@Autowired
	public CalendarService(PersonRepo personRepo, CalendarTokenRepo tokenRepo) {
		this.personRepository = personRepo;
		this.tokenRepository = tokenRepo;
	}
	
	private void refreshAccessToken(CalendarToken token) throws IOException {
	    // Construct the refresh token request
	    URL url = new URL("https://oauth2.googleapis.com/token");
	    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	    connection.setDoOutput(true);
	    connection.setRequestMethod("POST");
	    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

	    String postData = "client_id=" + clientId +
	                      "&client_secret=" + clientSecret +
	                      "&refresh_token=" + token.getRefreshToken() +
	                      "&grant_type=refresh_token";

	    try (OutputStream os = connection.getOutputStream()) {
	        os.write(postData.getBytes(StandardCharsets.UTF_8));
	    }

	    // Parse the response
	    if (connection.getResponseCode() == 200) {
	        try (InputStream is = connection.getInputStream();
	             Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {

	            JsonObject jsonResponse = JsonParser.parseReader(reader).getAsJsonObject();
	            String newAccessToken = jsonResponse.get("access_token").getAsString();
	            int expiresIn = jsonResponse.get("expires_in").getAsInt();

	            // Update the CalendarToken entity
	            token.setAccessToken(newAccessToken);
	            token.setExpireAfter(LocalDateTime.now().plusSeconds(expiresIn));

	            // Save the updated token to the database
	            tokenRepository.save(token);
	        }
	    } else {
	        throw new IOException("Failed to refresh access token: " + connection.getResponseMessage());
	    }
	}

	public boolean saveTokens(String uid, String accessToken, String refreshToken, LocalDateTime expirationTime) {
	    // Retrieve the person from the database using personID
	    Person person = personRepository.findByUid(uid).orElse(null);
	    
	    // Check if the person exists
	    if (person != null) {
	        // Create a new CalendarToken instance and set its fields
	        CalendarToken calendarToken = person.getCalendartoken();
	        if(calendarToken == null) {
	        	calendarToken = new CalendarToken();
		        // Associate the calendar token with the person
		        calendarToken.setPerson(person);
	        }	        
	        calendarToken.setAccessToken(accessToken);
	        calendarToken.setRefreshToken(refreshToken);
	        calendarToken.setExpireAfter(expirationTime);
	        

	        person.setCalendartoken(calendarToken);
	        
	        // Save the CalendarToken instance using the repository
	        tokenRepository.save(calendarToken);
	        
	        // Update the Person entity as well if needed
	        personRepository.save(person);
	        return true;
	    } else {
	        // Handle case where person is not found, maybe throw an exception or log a message
	        System.out.println("Person with ID " + person.getId() + " not found.");
	        return false;
	    }
	}

	public Teacher getTeacherByUid(String uid) {
		return (Teacher) personRepository.findByUid(uid).orElseThrow(null);
	}

	public boolean createSchedule(Integer teacherId, Integer studentId, String startTime, String endTime) {
	    try {
	        // Fetch teacher and student from the database
	        Person teacher = personRepository.findById(teacherId)
	                .orElseThrow(() -> new Exception("Teacher not found."));
	        Person student = personRepository.findById(studentId)
	                .orElseThrow(() -> new Exception("Student not found."));
	        
	        // Validate entities
	        if (teacher == null || student == null) {
	            System.out.println("Teacher or Student not found");
	            return false;
	        }

	        
	        // Refresh access tokens if expired
	        CalendarToken teacherToken = teacher.getCalendartoken();
	        CalendarToken studentToken = student.getCalendartoken();

	        // Refresh the teacher's access token if it's expired
	        if (teacherToken != null && teacherToken.getExpireAfter().isBefore(LocalDateTime.now())) {
	            refreshAccessToken(teacherToken);
	        }

	        // Refresh the student's access token if it's expired
	        if (studentToken != null && studentToken.getExpireAfter().isBefore(LocalDateTime.now())) {
	            refreshAccessToken(studentToken);
	        }
	        
	        
	        // Parse and convert startTime and endTime to ISO 8601 format with timezone offset
	        LocalDateTime startLocalDateTime = LocalDateTime.parse(startTime);
	        LocalDateTime endLocalDateTime = LocalDateTime.parse(endTime);

	        ZoneId zoneId = ZoneId.of("Asia/Karachi"); // Use your timezone
	        ZonedDateTime startZonedDateTime = startLocalDateTime.atZone(zoneId);
	        ZonedDateTime endZonedDateTime = endLocalDateTime.atZone(zoneId);

	        // Format to ISO 8601 with offset (e.g., 2024-11-23T15:41:00+05:00)
	        String formattedStart = startZonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	        String formattedEnd = endZonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

	        DateTime googleStartDateTime = new DateTime(formattedStart);
	        DateTime googleEndDateTime = new DateTime(formattedEnd);

	        // Create EventDateTime objects
	        EventDateTime startEventDateTime = new EventDateTime()
	                .setDateTime(googleStartDateTime)
	                .setTimeZone("Asia/Karachi");

	        EventDateTime endEventDateTime = new EventDateTime()
	                .setDateTime(googleEndDateTime)
	                .setTimeZone("Asia/Karachi");

	        // Create the event
	        Event event = new Event()
	                .setSummary(teacher.getFirstName() + " " + teacher.getLastName() + "Set up a meeting with " + student.getFirstName() + " " + student.getLastName())
	                .setStart(startEventDateTime)
	                .setEnd(endEventDateTime);

	        System.out.println("Start Time: " + formattedStart);
	        System.out.println("End Time: " + formattedEnd);

	        
	        
	        // Insert event into the teacher's Google Calendar
	        com.google.api.services.calendar.Calendar teacherService = getCalendarService(teacherToken);
	        teacherService.events().insert("primary", event).execute();

	        // Insert event into the student's Google Calendar
	        com.google.api.services.calendar.Calendar studentService = getCalendarService(studentToken);
	        studentService.events().insert("primary", event).execute();

	        
	        return true;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	
	public Calendar getCalendarService(CalendarToken token) throws IOException {
	    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
	    NetHttpTransport httpTransport = new NetHttpTransport();

	    // Refresh the access token if expired
	    if (token.getExpireAfter().isBefore(LocalDateTime.now())) {
	        refreshAccessToken(token);
	    }

	    // Create credentials
	    Credential credential = new Credential.Builder(new Credential.AccessMethod() {
	        @Override
	        public void intercept(HttpRequest request, String accessToken) throws IOException {
	            request.getHeaders().setAuthorization("Bearer " + accessToken);
	        }

	        @Override
	        public String getAccessTokenFromRequest(HttpRequest request) {
	            return null; // Not used
	        }
	    })
	        .setTransport(httpTransport)
	        .setJsonFactory(jsonFactory)
	        .setClientAuthentication(new ClientParametersAuthentication(clientId, clientSecret))
	        .setTokenServerEncodedUrl("https://oauth2.googleapis.com/token")
	        .build();

	    credential.setAccessToken(token.getAccessToken());
	    credential.setRefreshToken(token.getRefreshToken());
	    credential.setExpirationTimeMilliseconds(
	        token.getExpireAfter().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
	    );

	    // Return the Google Calendar service
	    return new Calendar.Builder(httpTransport, jsonFactory, credential)
	            .setApplicationName("TaleemHub")
	            .build();
	}


	public boolean checkRefreshToken(String refreshToken) {
	    try {

	        // Create UserCredentials with refreshToken
	        UserCredentials userCredentials = UserCredentials.newBuilder()
	                .setClientId(clientId)
	                .setClientSecret(clientSecret)
	                .setRefreshToken(refreshToken)
	                .build();

	        // Get a new access token
	        AccessToken accessToken = userCredentials.refreshAccessToken();

	        // Check if the token is valid and not expired
	        if (accessToken != null && accessToken.getExpirationTime().after(new Date())) {
	            return true;
	        }
	    } catch (IOException e) {
	        // Log the exception
	        System.out.println("Failed to validate refresh token: " + e.getMessage());
	    }

	    return false; // Return false if any error occurs or token is invalid
	}

}
