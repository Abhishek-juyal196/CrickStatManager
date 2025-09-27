package com.cricket.integration;

import com.cricket.entity.League;
import com.cricket.repository.LeagueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class LeagueIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LeagueRepository leagueRepository;

    private League testLeague;

    @BeforeEach
    void setUp() {
        testLeague = new League();
        testLeague.setName("Test League");
        testLeague.setStartDate(LocalDate.now().plusDays(1));
        testLeague.setEndDate(LocalDate.now().plusDays(30));
        testLeague.setStatus(League.LeagueStatus.UPCOMING);
    }

    @Test
    void saveLeague_ShouldPersistLeague() {
        // When
        League savedLeague = leagueRepository.save(testLeague);
        entityManager.flush();

        // Then
        assertNotNull(savedLeague.getId());
        assertEquals("Test League", savedLeague.getName());
        assertEquals(League.LeagueStatus.UPCOMING, savedLeague.getStatus());
    }

    @Test
    void findLeagueById_WhenLeagueExists_ShouldReturnLeague() {
        // Given
        League savedLeague = leagueRepository.save(testLeague);
        entityManager.flush();

        // When
        Optional<League> foundLeague = leagueRepository.findById(savedLeague.getId());

        // Then
        assertTrue(foundLeague.isPresent());
        assertEquals("Test League", foundLeague.get().getName());
    }

    @Test
    void findLeagueById_WhenLeagueDoesNotExist_ShouldReturnEmpty() {
        // When
        Optional<League> foundLeague = leagueRepository.findById(999L);

        // Then
        assertFalse(foundLeague.isPresent());
    }

    @Test
    void findLeagueByName_ShouldReturnLeague() {
        // Given
        League savedLeague = leagueRepository.save(testLeague);
        entityManager.flush();

        // When
        Optional<League> foundLeague = leagueRepository.findByName("Test League");

        // Then
        assertTrue(foundLeague.isPresent());
        assertEquals("Test League", foundLeague.get().getName());
    }

    @Test
    void findLeaguesByStatus_ShouldReturnFilteredLeagues() {
        // Given
        League upcomingLeague = new League();
        upcomingLeague.setName("Upcoming League");
        upcomingLeague.setStartDate(LocalDate.now().plusDays(1));
        upcomingLeague.setEndDate(LocalDate.now().plusDays(30));
        upcomingLeague.setStatus(League.LeagueStatus.UPCOMING);

        League ongoingLeague = new League();
        ongoingLeague.setName("Ongoing League");
        ongoingLeague.setStartDate(LocalDate.now().minusDays(1));
        ongoingLeague.setEndDate(LocalDate.now().plusDays(30));
        ongoingLeague.setStatus(League.LeagueStatus.ONGOING);

        leagueRepository.save(upcomingLeague);
        leagueRepository.save(ongoingLeague);
        entityManager.flush();

        // When
        List<League> upcomingLeagues = leagueRepository.findByStatus(League.LeagueStatus.UPCOMING);

        // Then
        assertEquals(1, upcomingLeagues.size());
        assertEquals("Upcoming League", upcomingLeagues.get(0).getName());
    }

    @Test
    void deleteLeague_ShouldRemoveLeague() {
        // Given
        League savedLeague = leagueRepository.save(testLeague);
        entityManager.flush();
        Long leagueId = savedLeague.getId();

        // When
        leagueRepository.delete(savedLeague);
        entityManager.flush();

        // Then
        Optional<League> foundLeague = leagueRepository.findById(leagueId);
        assertFalse(foundLeague.isPresent());
    }
}
