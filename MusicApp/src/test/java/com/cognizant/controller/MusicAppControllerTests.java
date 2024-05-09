package com.cognizant.controller;

import com.cognizant.config.SpotifyConfig;
import com.cognizant.model.UserFavouriteSongList;
import com.cognizant.service.UserFavouriteSongService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class MusicAppControllerTests {
	private MockMvc mockMvc;
	private String authToken;
	private HttpHeaders headers;
	@Mock
	private UserFavouriteSongService userFavouriteSongService;
	@Mock
	private UserFavouriteSongList userFavouriteSongList;
	@Mock
	private SpotifyConfig spotifyConfig;
	@InjectMocks
	private MusicAppController musicAppController;

	@BeforeEach
	public void setUp() throws IOException, InterruptedException {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(musicAppController).build();
		HttpClient httpClient = HttpClient.newBuilder().build();
		HttpRequest httpRequest =
				HttpRequest.newBuilder()
						.uri(URI.create("http://localhost:8081/api/v1/login?username=Test@gmail.com&password=123456789"))
						.build();
		HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
		authToken = httpResponse.body().split(",")[1].split(":")[1].replaceAll("\"|}", "");
		headers = new HttpHeaders();
		headers.set("Authentication", authToken);
	}
	@Test
	void verifyUserLoginWithJWTAuthentication() throws Exception {
		mockMvc.perform(get("/api/v1/authenticate-login")
					.headers(headers))
				.andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	void checkIfAbleToGetSpotifyToken() throws Exception {
		mockMvc.perform(get("/api/v1/spotify-token")
				.headers(headers))
			.andExpect(status().isOk())
			.andDo(MockMvcResultHandlers.print());
	}

	@Test
	void checkIfAbleToGetFavouriteSongList() throws Exception {
		mockMvc.perform(get("/api/v1/get-favourite-music")
				.param("userId", "Test@gmail.com")
				.headers(headers))
			.andExpect(status().isOk())
			.andDo(MockMvcResultHandlers.print());
	}

}
