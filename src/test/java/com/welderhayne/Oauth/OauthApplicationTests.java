package com.welderhayne.Oauth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.welderhayne.Oauth.Dtos.RegisterDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OauthApplicationTests {
	@Autowired
	private MockMvc mock;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@SneakyThrows
	void register() {
		mock.perform(
				post("/oauth2/register")
						.param("username", "sheilinha")
						.param("email", "sheila@email.com")
						.param("password", "5544")
		).andExpect(status().isNoContent());
	}

	@Test
	@SneakyThrows
	void testAdmin() {
		MvcResult result = mock.perform(
				post("/oauth2/token")
						.param("usernameOrEmail", "kswelder")
						.param("password", "5544")
		).andExpect(status().isOk())
				.andReturn();

		String token = "Bearer "+result.getResponse().getContentAsString();

		mock.perform(
				get("/admin/all")
						.header("Authorization", token)
		)
				.andExpect(status().isOk());
	}
}
