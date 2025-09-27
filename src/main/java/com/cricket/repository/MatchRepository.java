package com.cricket.repository;

import com.cricket.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    
    List<Match> findByLeagueId(Long leagueId);
    
    List<Match> findByStatus(Match.MatchStatus status);
    
    List<Match> findByMatchDate(LocalDate matchDate);
    
    @Query("SELECT m FROM Match m WHERE m.league.id = :leagueId AND m.status = :status")
    List<Match> findByLeagueIdAndStatus(@Param("leagueId") Long leagueId, @Param("status") Match.MatchStatus status);
    
    @Query("SELECT m FROM Match m WHERE (m.team1.id = :teamId OR m.team2.id = :teamId) AND m.league.id = :leagueId")
    List<Match> findByTeamIdAndLeagueId(@Param("teamId") Long teamId, @Param("leagueId") Long leagueId);
    
    @Query("SELECT m FROM Match m WHERE m.league.id = :leagueId ORDER BY m.matchDate DESC")
    List<Match> findByLeagueIdOrderByMatchDateDesc(@Param("leagueId") Long leagueId);
}
