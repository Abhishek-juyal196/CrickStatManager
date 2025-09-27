package com.cricket.repository;

import com.cricket.entity.MatchScorecard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchScorecardRepository extends JpaRepository<MatchScorecard, Long> {
    
    Optional<MatchScorecard> findByMatchId(Long matchId);
    
    @Query("SELECT ms FROM MatchScorecard ms WHERE ms.match.league.id = :leagueId")
    List<MatchScorecard> findByLeagueId(@Param("leagueId") Long leagueId);
    
    @Query("SELECT ms FROM MatchScorecard ms WHERE ms.winningTeamId = :teamId")
    List<MatchScorecard> findByWinningTeamId(@Param("teamId") Long teamId);
    
    @Query("SELECT ms FROM MatchScorecard ms WHERE ms.manOfTheMatchId = :playerId")
    List<MatchScorecard> findByManOfTheMatchId(@Param("playerId") Long playerId);
}
