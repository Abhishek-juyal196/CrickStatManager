// Simple API client wrapper aligned with Spring Boot backend
// Use full URL in development to avoid proxy issues
const BASE = process.env.REACT_APP_API_BASE || 'http://localhost:8081/api';

async function request(path, options = {}) {
  const url = `${BASE}${path}`;
  const headers = { 'Content-Type': 'application/json', ...(options.headers || {}) };
  
  try {
    const resp = await fetch(url, { ...options, headers });
    if (!resp.ok) {
      const text = await resp.text();
      throw new Error(`${resp.status} ${resp.statusText}: ${text}`);
    }
    const contentType = resp.headers.get('content-type') || '';
    if (contentType.includes('application/json')) return resp.json();
    return null;
  } catch (error) {
    console.error(`API Error [${path}]:`, error);
    throw error;
  }
}

// Leagues
export const getLeagues = () => request('/leagues');
export const getLeague = (id) => request(`/leagues/${id}`);
export const createLeague = (payload) => request('/leagues', { method: 'POST', body: JSON.stringify(payload) });
export const deleteLeague = (id) => request(`/leagues/${id}`, { method: 'DELETE' });

// Teams
export const getTeams = () => request('/teams');
export const getTeam = (id) => request(`/teams/${id}`);
export const createTeam = (payload) => request('/teams', { method: 'POST', body: JSON.stringify(payload) });
export const deleteTeam = (id) => request(`/teams/${id}`, { method: 'DELETE' });
export const getTeamPlayers = (teamId) => request(`/teams/${teamId}/players`);
export const addPlayerToTeam = (teamId, payload) => request(`/teams/${teamId}/players`, { method: 'POST', body: JSON.stringify(payload) });
export const deletePlayer = (teamId, playerId) => request(`/teams/${teamId}/players/${playerId}`, { method: 'DELETE' });

// Matches
export const getMatches = () => request('/matches');
export const getMatch = (id) => request(`/matches/${id}`);
export const createMatch = (payload) => request('/matches', { method: 'POST', body: JSON.stringify(payload) });
export const deleteMatch = (id) => request(`/matches/${id}`, { method: 'DELETE' });
export const getMatchScorecard = (id) => request(`/matches/${id}/scorecard`);

// Statistics
export const getPlayerBatting = (playerId) => request(`/statistics/players/${playerId}/batting`);
export const getPlayerBowling = (playerId) => request(`/statistics/players/${playerId}/bowling`);
export const getBattingLeaderboard = (leagueId) => request(`/statistics/leagues/${leagueId}/leaderboards/batting`);
export const getBowlingLeaderboard = (leagueId) => request(`/statistics/leagues/${leagueId}/leaderboards/bowling`);
export const getLeagueStandings = (leagueId) => request(`/statistics/leagues/${leagueId}/standings`);

// Scoring
export const getCurrentRunRate = (inningsId) => request(`/scoring/innings/${inningsId}/current-run-rate`);
export const getRequiredRunRate = (inningsId, targetRuns) => request(`/scoring/innings/${inningsId}/required-run-rate?targetRuns=${encodeURIComponent(targetRuns)}`);
export const getPartnership = (inningsId, batsman1Id, batsman2Id) => request(`/scoring/innings/${inningsId}/partnership?batsman1Id=${batsman1Id}&batsman2Id=${batsman2Id}`);
export const getOverProgression = (inningsId) => request(`/scoring/innings/${inningsId}/over-progression`);
export const getMatchStatistics = (matchId) => request(`/scoring/matches/${matchId}/statistics`);

export default {
  getLeagues,
  getLeague,
  createLeague,
  deleteLeague,
  getTeams,
  getTeam,
  createTeam,
  deleteTeam,
  getTeamPlayers,
  addPlayerToTeam,
  deletePlayer,
  getMatches,
  getMatch,
  createMatch,
  deleteMatch,
  getMatchScorecard,
  getPlayerBatting,
  getPlayerBowling,
  getBattingLeaderboard,
  getBowlingLeaderboard,
  getLeagueStandings,
  getCurrentRunRate,
  getRequiredRunRate,
  getPartnership,
  getOverProgression,
  getMatchStatistics
};
