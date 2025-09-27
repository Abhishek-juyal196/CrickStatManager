package com.cricket.service;

import com.cricket.entity.League;
import com.cricket.repository.LeagueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LeagueService {
    
    @Autowired
    private LeagueRepository leagueRepository;
    
    public League createLeague(League league) {
        return leagueRepository.save(league);
    }
    
    @Transactional(readOnly = true)
    public List<League> getAllLeagues() {
        return leagueRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<League> getLeagueById(Long id) {
        return leagueRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public List<League> getActiveLeagues() {
        return leagueRepository.findActiveLeagues();
    }
    
    @Transactional(readOnly = true)
    public List<League> getCompletedLeagues() {
        return leagueRepository.findCompletedLeagues();
    }
    
    public League updateLeague(Long id, League leagueDetails) {
        League league = leagueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("League not found with id: " + id));
        
        league.setName(leagueDetails.getName());
        league.setStartDate(leagueDetails.getStartDate());
        league.setEndDate(leagueDetails.getEndDate());
        league.setStatus(leagueDetails.getStatus());
        
        return leagueRepository.save(league);
    }
    
    public void deleteLeague(Long id) {
        League league = leagueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("League not found with id: " + id));
        leagueRepository.delete(league);
    }
    
    public League startLeague(Long id) {
        League league = leagueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("League not found with id: " + id));
        
        if (league.getStartDate().isAfter(LocalDate.now())) {
            throw new RuntimeException("Cannot start league before start date");
        }
        
        league.setStatus(League.LeagueStatus.ONGOING);
        return leagueRepository.save(league);
    }
    
    public League completeLeague(Long id) {
        League league = leagueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("League not found with id: " + id));
        
        league.setStatus(League.LeagueStatus.COMPLETED);
        return leagueRepository.save(league);
    }
}
