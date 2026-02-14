package com.mj.portfolio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mj.portfolio.config.SecurityConfig;
import com.mj.portfolio.dto.ArcadeMachineRequest;
import com.mj.portfolio.dto.ArcadeMachineResponse;
import com.mj.portfolio.entity.enums.MachineStatus;
import com.mj.portfolio.entity.enums.MachineType;
import com.mj.portfolio.service.ArcadeMachineService;
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

@WebMvcTest(ArcadeMachineController.class)
@Import(SecurityConfig.class)
@WithMockUser
class ArcadeMachineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ArcadeMachineService service;

    @Test
    void list_returnsOk() throws Exception {
        ArcadeMachineResponse resp = buildResponse();
        // PageImpl 3-arg constructor required: single-arg uses Unpaged which throws
        // UnsupportedOperationException when Jackson serializes pageable.getPageSize()
        when(service.findAll(any(), any()))
                .thenReturn(new PageImpl<>(List.of(resp), PageRequest.of(0, 20), 1));

        mockMvc.perform(get("/arcade/api/machines"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Machine-1"));
    }

    @Test
    void create_returnsCreated() throws Exception {
        ArcadeMachineRequest req = new ArcadeMachineRequest();
        req.setName("Machine-1");
        req.setType(MachineType.SLOT_A);

        when(service.create(any())).thenReturn(buildResponse());

        mockMvc.perform(post("/arcade/api/machines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Machine-1"));
    }

    @Test
    void create_withoutName_returnsBadRequest() throws Exception {
        ArcadeMachineRequest req = new ArcadeMachineRequest();
        req.setType(MachineType.SLOT_A);  // name intentionally missing

        mockMvc.perform(post("/arcade/api/machines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void heartbeat_returnsOk() throws Exception {
        UUID id = UUID.randomUUID();
        ArcadeMachineResponse resp = buildResponse();
        resp.getClass(); // just to ensure it's non-null
        when(service.heartbeat(id)).thenReturn(resp);

        mockMvc.perform(patch("/arcade/api/machines/" + id + "/heartbeat"))
                .andExpect(status().isOk());
    }

    private ArcadeMachineResponse buildResponse() {
        ArcadeMachineResponse r = new ArcadeMachineResponse();
        // Use reflection-style setters via from() factory - here we build manually
        // since we control the DTO directly in tests
        return ArcadeMachineResponse.from(buildMachine());
    }

    private com.mj.portfolio.entity.ArcadeMachine buildMachine() {
        com.mj.portfolio.entity.ArcadeMachine m = new com.mj.portfolio.entity.ArcadeMachine();
        // We can't set id (no setter) but that's fine for test assertions on name/type
        m.setName("Machine-1");
        m.setType(MachineType.SLOT_A);
        m.setStatus(MachineStatus.ONLINE);
        return m;
    }
}
