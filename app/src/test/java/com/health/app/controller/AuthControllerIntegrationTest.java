package com.health.app.controller;

import com.health.app.repository.UserAccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Test
    void shouldRegisterUserSuccessfully() throws Exception{
        String requestBody = """
                {
                    "email": "integration@example.com",
                    "password": "TestPassword@123",
                    "confirmPassword": "TestPassword@123"
                }""";

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());

        var account = userAccountRepository.findByNormalizedEmail("integration@example.com")
                .orElseThrow();

        assertEquals("integration@example.com", account.getNormalizedEmail());
        assertEquals("USER", account.getRole().getCode());
        assertEquals("PENDING_VERIFICATION", account.getStatus().name());
        assertFalse(account.isEmailVerified());
        assertTrue(account.getPasswordHash()!= null);
        assertTrue(account.getPasswordHash().startsWith("$argon2"));
    }

    @Test
    void shouldReturnConflictWhenEmailAlreadyExists() throws Exception {
        String requestBody = """
                {
                    "email": "duplicate@example.com",
                    "password": "TestPassword@123",
                    "confirmPassword": "TestPassword@123"
                }""";

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnBadRequestWhenPasswordsDoNotMatch() throws Exception{
        String requestBody = """
                {
                    "email": "mismatch@example.com",
                    "password": "TestPassword@123",
                    "confirmPassword": "DifferentPassword@123"
                }
                """;

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());

        assertTrue(userAccountRepository
                .findByNormalizedEmail("mismatch@example.com")
                .isEmpty());
    }

    @Test
    void shouldReturnBadRequestWhenEmailIsInvalid() throws Exception{
        String requestBody = """
                {
                    "email": "not-an-email",
                    "password": "TestPassword@123",
                    "confirmPassword": "TestPassword@123"
                }
                """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenEmailIsMissing() throws Exception{
        String requestBody = """
                {
                    "password": "TestPassword@123",
                    "confirmPassword": "TestPassword@123"
                }
                """;
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenPasswordIsTooShort() throws Exception {

        String requestBody = """
            {
                "email": "shortpassword@example.com",
                "password": "Short@123",
                "confirmPassword": "Short@123"
            }
            """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        assertTrue(userAccountRepository
                .findByNormalizedEmail("shortpassword@example.com")
                .isEmpty());

    }

    @Test
    void shouldReturnBadRequestWhenPasswordIsMissing() throws Exception {

        String requestBody = """
                {
                    "email": "missingpassword@example.com",
                    "confirmPassword": "TestPassword@123"
                }
                """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        assertTrue(userAccountRepository
                .findByNormalizedEmail("missingpassword@example.com")
                .isEmpty());

    }

    @Test
    void shouldReturnBadRequestWhenConfirmPasswordIsMissing() throws Exception {

        String requestBody = """
            {
                "email": "missingconfirm@example.com",
                "password": "TestPassword@123"
            }
            """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        assertTrue(userAccountRepository
                .findByNormalizedEmail("missingconfirm@example.com")
                .isEmpty());
    }
}
