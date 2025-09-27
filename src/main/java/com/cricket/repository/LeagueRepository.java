package com.cricket.repository;

import com.cricket.entity.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeagueRepository extends JpaRepository<League, Long> {
    
    Optional<League> findByName(String name);
    
    List<League> findByStatus(League.LeagueStatus status);
    
    @Query("SELECT l FROM League l WHERE l.startDate <= CURRENT_DATE AND l.endDate >= CURRENT_DATE")
    List<League> findActiveLeagues();
    
    @Query("SELECT l FROM League l WHERE l.endDate < CURRENT_DATE")
    List<League> findCompletedLeagues();
}
