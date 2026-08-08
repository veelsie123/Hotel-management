package com.example.demo;

import com.example.demo.dto.CustomerDTO;
import com.example.demo.dto.PaymentDTO;
import com.example.demo.dto.ReservationDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CustomerPaymentFlowIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void fullFlow_createCustomer_createReservation_createPayment() throws Exception {
        // login
        var tokenRes = mvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"admin\",\"password\":\"changeme\"}"))
            .andExpect(status().isOk())
            .andReturn();
        String body = tokenRes.getResponse().getContentAsString();
        com.fasterxml.jackson.databind.JsonNode node = new com.fasterxml.jackson.databind.ObjectMapper().readTree(body);
        String token = node.get("accessToken").asText();

        // create customer
        CustomerDTO c = new CustomerDTO();
        c.firstName = "Integration"; c.lastName = "Tester"; c.email = "int@test.com";
        var res = mvc.perform(post("/api/customers").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(c)))
            .andExpect(status().isCreated())
            .andReturn();
        com.fasterxml.jackson.databind.JsonNode createdCustomer = new com.fasterxml.jackson.databind.ObjectMapper().readTree(res.getResponse().getContentAsString());
        long customerId = createdCustomer.get("id").asLong();

        // create reservation using seeded room id 1
        ReservationDTO r = new ReservationDTO();
        r.customerId = customerId; r.roomId = 1L; r.checkIn = LocalDate.now().plusDays(3); r.checkOut = LocalDate.now().plusDays(5);
        var res2 = mvc.perform(post("/api/reservations").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(r)))
            .andExpect(status().isCreated())
            .andReturn();
        com.fasterxml.jackson.databind.JsonNode createdReservation = new com.fasterxml.jackson.databind.ObjectMapper().readTree(res2.getResponse().getContentAsString());
        long reservationId = createdReservation.get("id").asLong();

        // create payment
        PaymentDTO p = new PaymentDTO();
        p.reservationId = reservationId; p.amount = 100.0; p.method = "CARD";
        mvc.perform(post("/api/payments").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(p)))
            .andExpect(status().isCreated());
    }
}
