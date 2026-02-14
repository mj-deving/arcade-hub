package com.mj.portfolio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mj.portfolio.config.SecurityConfig;
import com.mj.portfolio.dto.LocationRequest;
import com.mj.portfolio.dto.LocationResponse;
import com.mj.portfolio.service.LocationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LocationController.class)
@Import(SecurityConfig.class)
@WithMockUser
class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LocationService service;

    @Test
    void list_returnsOk() throws Exception {
        when(service.findAll(any()))
                .thenReturn(new PageImpl<>(List.of(buildResponse()), PageRequest.of(0, 20), 1));

        mockMvc.perform(get("/arcade/api/locations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Arcade Hall 1"));
    }

    @Test
    void create_returnsCreated() throws Exception {
        LocationRequest req = new LocationRequest();
        req.setName("Arcade Hall 1");
        req.setMaxCapacity(50);

        when(service.create(any())).thenReturn(buildResponse());

        mockMvc.perform(post("/arcade/api/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Arcade Hall 1"));
    }

    @Test
    void create_withoutName_returnsBadRequest() throws Exception {
        LocationRequest req = new LocationRequest();
        req.setMaxCapacity(50);  // name intentionally missing

        mockMvc.perform(post("/arcade/api/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    private LocationResponse buildResponse() {
        com.mj.portfolio.entity.Location loc = new com.mj.portfolio.entity.Location();
        loc.setName("Arcade Hall 1");
        loc.setAddress("123 Game St");
        loc.setMaxCapacity(50);
        return LocationResponse.from(loc);
    }
}
