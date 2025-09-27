package com.cricket.service;

import com.cricket.entity.League;
import com.cricket.entity.Player;
import com.cricket.entity.Team;
import com.cricket.repository.LeagueRepository;
import com.cricket.repository.PlayerRepository;
import com.cricket.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TeamService {
    
    @Autowired
    private TeamRepository teamRepository;
    
    @Autowired
    private LeagueRepository leagueRepository;
    
    @Autowired
    private PlayerRepository playerRepository;
    
    public Team createTeam(Team team) {
        return teamRepository.save(team);
    }
    
    @Transactional(readOnly = true)
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<Team> getTeamsByLeagueId(Long leagueId) {
        return teamRepository.findByLeagueId(leagueId);
    }
    
    @Transactional(readOnly = true)
    public Optional<Team> getTeamById(Long id) {
        return teamRepository.findById(id);
    }
    
    public Team updateTeam(Long id, Team teamDetails) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + id));
        
        team.setName(teamDetails.getName());
        team.setLogoUrl(teamDetails.getLogoUrl());
        
        return teamRepository.save(team);
    }
    
    public void deleteTeam(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + id));
        teamRepository.delete(team);
    }
    
    public Player addPlayerToTeam(Long teamId, Player player) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + teamId));
        
        player.setTeam(team);
        return playerRepository.save(player);
    }
    
    @Transactional(readOnly = true)
    public List<Player> getTeamPlayers(Long teamId) {
        return playerRepository.findByTeamId(teamId);
    }
    
    public void removePlayerFromTeam(Long teamId, Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found with id: " + playerId));
        
        if (!player.getTeam().getId().equals(teamId)) {
            throw new RuntimeException("Player does not belong to this team");
        }
        
        playerRepository.delete(player);
    }
}
