package com.cricket.repository;

import com.cricket.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    
    List<Team> findByLeagueId(Long leagueId);
    
    Optional<Team> findByNameAndLeagueId(String name, Long leagueId);
    
    @Query("SELECT t FROM Team t WHERE t.league.id = :leagueId ORDER BY t.name")
    List<Team> findByLeagueIdOrderByName(@Param("leagueId") Long leagueId);
}
