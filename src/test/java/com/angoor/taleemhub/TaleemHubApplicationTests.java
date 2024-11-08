package com.angoor.taleemhub;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class TaleemHubApplicationTests {

    @Autowired
    private MockMvc mockMvc;  // Inject MockMvc for HTTP testing

    @Test
    @WithMockUser(username = "admin", password = "admin")
    public void testSayHelloEndpoint() throws Exception {
        mockMvc.perform(get("/hello"))  // Perform a GET request to /hello
                .andExpect(MockMvcResultMatchers.status().isOk())  // Expect HTTP 200 OK
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Hello, World!"));  // Check "status" valu
    }

}
