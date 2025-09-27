package com.cricket.service;

import com.cricket.entity.League;
import com.cricket.repository.LeagueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeagueServiceTest {

    @Mock
    private LeagueRepository leagueRepository;

    @InjectMocks
    private LeagueService leagueService;

    private League testLeague;

    @BeforeEach
    void setUp() {
        testLeague = new League();
        testLeague.setId(1L);
        testLeague.setName("Test League");
        testLeague.setStartDate(LocalDate.now().plusDays(1));
        testLeague.setEndDate(LocalDate.now().plusDays(30));
        testLeague.setStatus(League.LeagueStatus.UPCOMING);
    }

    @Test
    void createLeague_ShouldReturnCreatedLeague() {
        // Given
        when(leagueRepository.save(any(League.class))).thenReturn(testLeague);

        // When
        League result = leagueService.createLeague(testLeague);

        // Then
        assertNotNull(result);
        assertEquals("Test League", result.getName());
        verify(leagueRepository, times(1)).save(testLeague);
    }

    @Test
    void getLeagueById_WhenLeagueExists_ShouldReturnLeague() {
        // Given
        when(leagueRepository.findById(1L)).thenReturn(Optional.of(testLeague));

        // When
        Optional<League> result = leagueService.getLeagueById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Test League", result.get().getName());
        verify(leagueRepository, times(1)).findById(1L);
    }

    @Test
    void getLeagueById_WhenLeagueDoesNotExist_ShouldReturnEmpty() {
        // Given
        when(leagueRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<League> result = leagueService.getLeagueById(1L);

        // Then
        assertFalse(result.isPresent());
        verify(leagueRepository, times(1)).findById(1L);
    }

    @Test
    void updateLeague_WhenLeagueExists_ShouldReturnUpdatedLeague() {
        // Given
        League updatedLeague = new League();
        updatedLeague.setName("Updated League");
        updatedLeague.setStartDate(LocalDate.now().plusDays(1));
        updatedLeague.setEndDate(LocalDate.now().plusDays(30));
        updatedLeague.setStatus(League.LeagueStatus.ONGOING);

        when(leagueRepository.findById(1L)).thenReturn(Optional.of(testLeague));
        when(leagueRepository.save(any(League.class))).thenReturn(updatedLeague);

        // When
        League result = leagueService.updateLeague(1L, updatedLeague);

        // Then
        assertNotNull(result);
        assertEquals("Updated League", result.getName());
        verify(leagueRepository, times(1)).findById(1L);
        verify(leagueRepository, times(1)).save(any(League.class));
    }

    @Test
    void updateLeague_WhenLeagueDoesNotExist_ShouldThrowException() {
        // Given
        when(leagueRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            leagueService.updateLeague(1L, testLeague);
        });
        verify(leagueRepository, times(1)).findById(1L);
        verify(leagueRepository, never()).save(any(League.class));
    }

    @Test
    void deleteLeague_WhenLeagueExists_ShouldDeleteLeague() {
        // Given
        when(leagueRepository.findById(1L)).thenReturn(Optional.of(testLeague));

        // When
        leagueService.deleteLeague(1L);

        // Then
        verify(leagueRepository, times(1)).findById(1L);
        verify(leagueRepository, times(1)).delete(testLeague);
    }

    @Test
    void deleteLeague_WhenLeagueDoesNotExist_ShouldThrowException() {
        // Given
        when(leagueRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            leagueService.deleteLeague(1L);
        });
        verify(leagueRepository, times(1)).findById(1L);
        verify(leagueRepository, never()).delete(any(League.class));
    }
}
