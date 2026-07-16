package com.example.Student_Management;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class StudentManagementApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	void publicLoginPageRendersSuccessfully() throws Exception {
		mockMvc.perform(get("/login"))
				.andExpect(status().isOk());
	}

	@Test
	void protectedPagesRedirectWhenLoggedOut() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().is3xxRedirection());
		mockMvc.perform(get("/students"))
				.andExpect(status().is3xxRedirection());
		mockMvc.perform(get("/students/new"))
				.andExpect(status().is3xxRedirection());
	}

	@Test
	void adminPagesRenderWithAdminSession() throws Exception {
		MockHttpSession adminSession = new MockHttpSession();
		adminSession.setAttribute("role", "ADMIN");
		adminSession.setAttribute("displayName", "Administrator");

		mockMvc.perform(get("/students").session(adminSession))
				.andExpect(status().isOk());
		mockMvc.perform(get("/students/new").session(adminSession))
				.andExpect(status().isOk());
	}
}
