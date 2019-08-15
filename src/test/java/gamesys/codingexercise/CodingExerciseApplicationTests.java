package gamesys.codingexercise;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import gamesys.codingexercise.validation.ParamValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CodingExerciseApplicationTests {
	private static final ObjectMapper MAPPER = new ObjectMapper();

	private static final String PARAM_USERNAME = "username";
	private static final String PARAM_PASSWORD = "password";
	private static final String PARAM_DOB = "dob";
	private static final String PARAM_SSN = "ssn";

	private static final String VALID_USERNAME = "testUser";
	private static final String VALID_PASSWORD = "passWord1";
	private static final String VALID_DOB = "30-12-2000";
	private static final String VALID_SSN = "123-45-6788";

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testValidRegistrationPost() throws Exception {
		mockMvc.perform(post("/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(MAPPER.writeValueAsString(ImmutableMap.of(
						PARAM_USERNAME, VALID_USERNAME + "post",
						PARAM_PASSWORD, VALID_PASSWORD,
						PARAM_DOB,      VALID_DOB,
						PARAM_SSN,      VALID_SSN
				))))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(RegistrationService.MESSAGE_OK));
	}

	@Test
	public void testValidRegistrationGet() throws Exception {
		mockMvc.perform(get("/register")
						.param(PARAM_USERNAME, VALID_USERNAME + "get")
						.param(PARAM_PASSWORD, VALID_PASSWORD)
						.param(PARAM_DOB, VALID_DOB)
						.param(PARAM_SSN, VALID_SSN)
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(RegistrationService.MESSAGE_OK));
	}

	@Test
	public void testDuplicateUser() throws Exception {
		final String username = "duplicateUser";

		mockMvc.perform(post("/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(MAPPER.writeValueAsString(ImmutableMap.of(
						PARAM_USERNAME, username,
						PARAM_PASSWORD, VALID_PASSWORD,
						PARAM_DOB,      VALID_DOB,
						PARAM_SSN,      VALID_SSN
				))))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(RegistrationService.MESSAGE_OK));

		mockMvc.perform(post("/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(MAPPER.writeValueAsString(ImmutableMap.of(
						PARAM_USERNAME, username,
						PARAM_PASSWORD, VALID_PASSWORD,
						PARAM_DOB,      VALID_DOB,
						PARAM_SSN,      VALID_SSN
				))))
				.andDo(print())
				.andExpect(status().is(HttpStatus.CONFLICT.value()))
				.andExpect(content().string(RegistrationService.MESSAGE_DUPLICATE));
	}

	@Test
	public void testBlacklistedUser() throws Exception {
		mockMvc.perform(post("/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(MAPPER.writeValueAsString(ImmutableMap.of(
						PARAM_USERNAME, VALID_USERNAME + "blacklisted",
						PARAM_PASSWORD, VALID_PASSWORD,
						PARAM_DOB,      VALID_DOB,
						PARAM_SSN,      "123-45-6789"
				))))
				.andDo(print())
				.andExpect(status().is(HttpStatus.FORBIDDEN.value()))
				.andExpect(content().string(RegistrationService.MESSAGE_BLACKLISTED));
	}

	@Test
	public void testInvalidUsername() throws Exception {
		mockMvc.perform(post("/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(MAPPER.writeValueAsString(ImmutableMap.of(
						PARAM_USERNAME, "user!",
						PARAM_PASSWORD, VALID_PASSWORD,
						PARAM_DOB,      VALID_DOB,
						PARAM_SSN,      VALID_SSN
				))))
				.andDo(print())
				.andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
				.andExpect(content().string(containsString(ParamValidator.USERNAME_ERROR)));
	}

	@Test
	public void testInvalidPassword() throws Exception {
		mockMvc.perform(post("/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(MAPPER.writeValueAsString(ImmutableMap.of(
						PARAM_USERNAME, VALID_USERNAME,
						PARAM_PASSWORD, "pass",
						PARAM_DOB,      VALID_DOB,
						PARAM_SSN,      VALID_SSN
				))))
				.andDo(print())
				.andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
				.andExpect(content().string(containsString(ParamValidator.PASSWORD_ERROR)));
	}

	@Test
	public void testInvalidDob() throws Exception {
		mockMvc.perform(post("/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(MAPPER.writeValueAsString(ImmutableMap.of(
						PARAM_USERNAME, VALID_USERNAME,
						PARAM_PASSWORD, VALID_PASSWORD,
						PARAM_DOB,      "2000-12-30",
						PARAM_SSN,      VALID_SSN
				))))
				.andDo(print())
				.andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
				.andExpect(content().string(containsString(ParamValidator.DOB_ERROR)));
	}

	@Test
	public void testInvalidSsn() throws Exception {
		mockMvc.perform(post("/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(MAPPER.writeValueAsString(ImmutableMap.of(
						PARAM_USERNAME, VALID_USERNAME,
						PARAM_PASSWORD, VALID_PASSWORD,
						PARAM_DOB,      VALID_DOB,
						PARAM_SSN,      "1234566789"
				))))
				.andDo(print())
				.andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
				.andExpect(content().string(containsString(ParamValidator.SSN_ERROR)));
	}
}
