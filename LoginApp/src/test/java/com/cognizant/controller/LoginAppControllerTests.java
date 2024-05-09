package com.cognizant.controller;

import com.cognizant.config.JWTTokenGenerator;
import com.cognizant.controller.LoginController;
import com.cognizant.model.User;
import com.cognizant.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LoginAppControllerTests {

	private MockMvc mockMvc;
	@Mock
	private UserService userService;
	@Mock
	private JWTTokenGenerator jwtTokenGenerator;
	@InjectMocks
	private LoginController loginController;

	private User newUser;
	private User existingUser;
	private List<User> userList;


	@BeforeEach
	public void setUp(){
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
		newUser = new User();
		newUser.setUsername("TestCase@gmail.com");
		newUser.setPassword("TestCase2000");

		existingUser = new User();
		existingUser.setUsername("Test@gmail.com");
		existingUser.setPassword("123456789");

		userList = new ArrayList<>();
		userList.add(existingUser);
	}
	@AfterEach
	public void tearDown(){
		newUser = null;
	}

	@Test
	public void shouldCreateUserAccount() throws Exception {
		when(userService.saveUser(any())).thenReturn(newUser);
		mockMvc.perform(post("/api/v1/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(newUser)))
				.andExpect(status().isCreated())
				.andDo(MockMvcResultHandlers.print());
		verify(userService).saveUser(any());
	}
	@Test
	public void shouldReturnJWTTokenIfUserExist() throws Exception {
		when(userService.getUser(any())).thenReturn(userList);
		mockMvc.perform(get("/api/v1/login")
					.param("username", existingUser.getUsername())
					.param("password", existingUser.getPassword()))
				.andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print());
		verify(userService).getUser(any());
	}
	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
