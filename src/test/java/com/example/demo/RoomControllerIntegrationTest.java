package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RoomControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void listRooms_requiresAuth() throws Exception {
        mvc.perform(get("/api/rooms")).andExpect(status().isUnauthorized());
    }

    @Test
    void listRooms_withAuth() throws Exception {
        // obtain token
        var tokenRes = mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/auth/login")
            .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
            .content("{\"username\":\"admin\",\"password\":\"changeme\"}"))
            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk())
            .andReturn();
        String body = tokenRes.getResponse().getContentAsString();
        com.fasterxml.jackson.databind.JsonNode node = new com.fasterxml.jackson.databind.ObjectMapper().readTree(body);
        String token = node.get("accessToken").asText();

        mvc.perform(get("/api/rooms").header("Authorization", "Bearer " + token))
            .andExpect(status().isOk());
    }
}
