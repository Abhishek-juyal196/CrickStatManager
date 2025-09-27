package com.cricket.repository;

import com.cricket.entity.Innings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InningsRepository extends JpaRepository<Innings, Long> {
    
    List<Innings> findByMatchId(Long matchId);
    
    Optional<Innings> findByMatchIdAndInningsNumber(Long matchId, Integer inningsNumber);
    
    @Query("SELECT i FROM Innings i WHERE i.match.id = :matchId ORDER BY i.inningsNumber")
    List<Innings> findByMatchIdOrderByInningsNumber(@Param("matchId") Long matchId);
    
    @Query("SELECT i FROM Innings i WHERE i.battingTeam.id = :teamId")
    List<Innings> findByBattingTeamId(@Param("teamId") Long teamId);
    
    @Query("SELECT i FROM Innings i WHERE i.bowlingTeam.id = :teamId")
    List<Innings> findByBowlingTeamId(@Param("teamId") Long teamId);
}
