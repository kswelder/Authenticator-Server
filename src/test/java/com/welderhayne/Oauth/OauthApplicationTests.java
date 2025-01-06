package com.welderhayne.Oauth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.welderhayne.Oauth.Dtos.RegisterDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
		)
				.andDo(print())
				.andExpect(status().isNoContent());
	}

	@Test
	@SneakyThrows
	void getAuthorities() {
		MvcResult result = mock.perform(
						post("/oauth2/token")
								.param("usernameOrEmail", "kswelder")
								.param("password", "5544")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		String token = "Bearer "+result.getResponse().getContentAsString();

		mock.perform(
				get("/oauth2/authorities")
						.header("Authorization", token)
		)
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	@SneakyThrows
	void testAdmin() {
		Random random = new Random();

		MvcResult result = mock.perform(
				post("/oauth2/token")
						.param("usernameOrEmail", "kswelder")
						.param("password", "5544")
		)
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		String token = "Bearer "+result.getResponse().getContentAsString();

		result = mock.perform(
				get("/admin/all")
						.header("Authorization", token)
		)
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		String json = result.getResponse().getContentAsString();

		List<RegisterDto> list = objectMapper.readValue(json, new TypeReference<List<RegisterDto>>() {});

		Assertions.assertTrue(list.size() > 0);

		mock.perform(get(
				"/admin/find/register/id/"+
				list.get(random.nextInt(list.size()-1)).getId())
				.header("Authorization", token)
		)
				.andDo(print())
				.andExpect(status().isOk());

		mock.perform(
				get("/admin/find/register/username/ElPepe")
						.header("Authorization", token)
		)
				.andDo(print())
				.andExpect(status().isOk());

		result = mock.perform(
				get("/admin/find/register/email/oldpresidentofamerica@email.com")
						.header("Authorization", token)
		)
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		RegisterDto register = objectMapper.readValue(result.getResponse().getContentAsString(), RegisterDto.class);

		mock.perform(
				put("/admin/update/register")
						.param("id", register.getId())
						.param("username", "JoelBiden")
						.param("email", register.getEmail())
						.header("Authorization", token)
		)
				.andDo(print())
				.andExpect(status().isNoContent());

		mock.perform(
				put("/admin/update/password")
						.param("id", register.getId())
						.param("password", "JoelBiden@2021.15")
						.header("Authorization", token)
		)
				.andDo(print())
				.andExpect(status().isNoContent());

		mock.perform(
				put("/admin/update/permition")
						.param("id", register.getId())
						.param("perm", "EDITOR")
						.header("Authorization", token)
		)
				.andDo(print())
				.andExpect(status().isNoContent());

		mock.perform(
				delete("/admin/delete/" + register.getId())
						.header("Authorization", token)
		)
				.andDo(print())
				.andExpect(status().isNoContent());
	}

	@Test
	@SneakyThrows
	public void testRegisterController() {
		mock.perform(
				post("/oauth2/register")
						.param("username", "BrazilianBoy")
						.param("email", "RonaldinhoSocker2007@email.com")
						.param("password", "MySockerIsVeryGood@.360")
		)
				.andDo(print())
				.andExpect(status().isNoContent());

		MvcResult result = mock.perform(
				post("/oauth2/token")
						.param("usernameOrEmail", "BrazilianBoy")
						.param("password", "MySockerIsVeryGood@.360")
		)
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		String token = "Bearer " + result.getResponse().getContentAsString();

		result = mock.perform(
				get("/user/register")
						.header("Authorization", token)
		)
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		RegisterDto dto = objectMapper.readValue(
				result.getResponse().getContentAsString(),
				RegisterDto.class
		);

		mock.perform(
				put("/user/update/register")
						.header("Authorization", token)
						.param("username", dto.getUsername())
						.param("email", "MrSadBoySing@email.com")
		)
				.andDo(print())
				.andExpect(status().isNoContent())
				.andDo(print());

		mock.perform(
				put("/user/update/password")
						.header("Authorization", token)
						.param("password", "HbDkhaQi")
		)
				.andDo(print())
				.andExpect(status().isNoContent());

		mock.perform(
				delete("/user/delete")
						.header("Authorization", token)
		)
				.andDo(print())
				.andExpect(status().isNoContent());
	}

	@Test
	@SneakyThrows
	public void throwableRequests() {
		mock.perform(
						post("/oauth2/token")
								.param("usernameOrEmail", "kswelder")
								.param("password", "5544;>")
				)
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andReturn();

		MvcResult result = mock.perform(
				post("/oauth2/token")
						.param("usernameOrEmail", "kswelder")
						.param("password", "5544")
		)
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		String token = "Bearer " + result.getResponse().getContentAsString();

		mock.perform(
				put("/user/update/register")
						.header("Authorization", token)
						.param("username", "awdsa;>")
						.param("email", "inothaveemail@email.com")
		)
				.andDo(print())
				.andExpect(status().isBadRequest());
	}
}
