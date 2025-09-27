package com.cricket.controller;

import com.cricket.dto.LeagueDTO;
import com.cricket.entity.League;
import com.cricket.service.LeagueService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LeagueController.class)
class LeagueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LeagueService leagueService;

    @Autowired
    private ObjectMapper objectMapper;

    private League testLeague;
    private LeagueDTO testLeagueDTO;

    @BeforeEach
    void setUp() {
        testLeague = new League();
        testLeague.setId(1L);
        testLeague.setName("Test League");
        testLeague.setStartDate(LocalDate.now().plusDays(1));
        testLeague.setEndDate(LocalDate.now().plusDays(30));
        testLeague.setStatus(League.LeagueStatus.UPCOMING);

        testLeagueDTO = new LeagueDTO(testLeague);
    }

    @Test
    void createLeague_ShouldReturnCreatedLeague() throws Exception {
        // Given
        when(leagueService.createLeague(any(League.class))).thenReturn(testLeague);

        // When & Then
        mockMvc.perform(post("/api/leagues")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testLeagueDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test League"))
                .andExpect(jsonPath("$.status").value("UPCOMING"));
    }

    @Test
    void getAllLeagues_ShouldReturnListOfLeagues() throws Exception {
        // Given
        List<League> leagues = Arrays.asList(testLeague);
        when(leagueService.getAllLeagues()).thenReturn(leagues);

        // When & Then
        mockMvc.perform(get("/api/leagues"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Test League"));
    }

    @Test
    void getLeagueById_WhenLeagueExists_ShouldReturnLeague() throws Exception {
        // Given
        when(leagueService.getLeagueById(1L)).thenReturn(Optional.of(testLeague));

        // When & Then
        mockMvc.perform(get("/api/leagues/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test League"));
    }

    @Test
    void getLeagueById_WhenLeagueDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Given
        when(leagueService.getLeagueById(1L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/leagues/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateLeague_ShouldReturnUpdatedLeague() throws Exception {
        // Given
        when(leagueService.updateLeague(eq(1L), any(League.class))).thenReturn(testLeague);

        // When & Then
        mockMvc.perform(put("/api/leagues/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testLeagueDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test League"));
    }

    @Test
    void deleteLeague_ShouldReturnNoContent() throws Exception {
        // Given
        when(leagueService.getLeagueById(1L)).thenReturn(Optional.of(testLeague));

        // When & Then
        mockMvc.perform(delete("/api/leagues/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void startLeague_ShouldReturnStartedLeague() throws Exception {
        // Given
        testLeague.setStatus(League.LeagueStatus.ONGOING);
        when(leagueService.startLeague(1L)).thenReturn(testLeague);

        // When & Then
        mockMvc.perform(post("/api/leagues/1/start"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ONGOING"));
    }

    @Test
    void completeLeague_ShouldReturnCompletedLeague() throws Exception {
        // Given
        testLeague.setStatus(League.LeagueStatus.COMPLETED);
        when(leagueService.completeLeague(1L)).thenReturn(testLeague);

        // When & Then
        mockMvc.perform(post("/api/leagues/1/complete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }
}
