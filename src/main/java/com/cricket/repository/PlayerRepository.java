package com.cricket.repository;

import com.cricket.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    
    List<Player> findByTeamId(Long teamId);
    
    Optional<Player> findByNameAndTeamId(String name, Long teamId);
    
    @Query("SELECT p FROM Player p WHERE p.team.league.id = :leagueId")
    List<Player> findByLeagueId(@Param("leagueId") Long leagueId);
    
    @Query("SELECT p FROM Player p WHERE p.team.id = :teamId ORDER BY p.name")
    List<Player> findByTeamIdOrderByName(@Param("teamId") Long teamId);
}
