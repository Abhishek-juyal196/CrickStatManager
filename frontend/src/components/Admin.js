import React, { useState } from 'react';
import { useLeague } from '../App';
import api from '../api';

const Admin = () => {
  const { leagueData, updateLeagueData, reload, loading: globalLoading } = useLeague();
  const [messages, setMessages] = useState({});
  const [localLoading, setLocalLoading] = useState({});

  const getTeamById = (teamId) => {
    return leagueData.teams.find(t => t.id === teamId);
  };

  const formatMatchLabel = (aId, bId) => {
    const a = getTeamById(aId);
    const b = getTeamById(bId);
    return `${a ? a.name : 'Unknown'} vs ${b ? b.name : 'Unknown'}`;
  };

  const sortStandings = (rows) => {
    return [...rows].sort((x, y) => {
      if (y.points !== x.points) return y.points - x.points;
      if (y.won !== x.won) return y.won - x.won;
      const nameX = (getTeamById(x.teamId)?.name || '').toLowerCase();
      const nameY = (getTeamById(y.teamId)?.name || '').toLowerCase();
      return nameX.localeCompare(nameY);
    });
  };

  const setMessage = (key, message, isError = false) => {
    setMessages(prev => ({ ...prev, [key]: { text: message, isError } }));
    // Auto-clear success messages after 5 seconds
    if (!isError) {
      setTimeout(() => {
        setMessages(prev => {
          const updated = { ...prev };
          if (updated[key]?.text === message) {
            delete updated[key];
          }
          return updated;
        });
      }, 5000);
    }
  };

  const setLoading = (key, isLoading) => {
    setLocalLoading(prev => ({ ...prev, [key]: isLoading }));
  };

  // League Management
  const handleAddLeague = async (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const name = formData.get('leagueName').trim();
    const startDate = formData.get('startDate');
    const endDate = formData.get('endDate');
    const status = formData.get('status') || 'UPCOMING';

    if (!name || !startDate || !endDate) {
      setMessage('league', 'All fields are required.', true);
      return;
    }

    if (new Date(startDate) > new Date(endDate)) {
      setMessage('league', 'Start date must be before end date.', true);
      return;
    }

    setLoading('league', true);
    try {
      const payload = { name, startDate, endDate, status };
      await api.createLeague(payload);
      await reload(); // Reload all data
      e.target.reset();
      setMessage('league', 'League added successfully!');
    } catch (err) {
      setMessage('league', `Failed to add league: ${err.message}`, true);
    } finally {
      setLoading('league', false);
    }
  };

  // Team Management
  const handleAddTeam = async (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const name = formData.get('teamName').trim();
    const logo = formData.get('teamLogo').trim();
    const leagueId = formData.get('leagueId');

    if (!name || !leagueId) {
      setMessage('team', 'Team name and league are required.', true);
      return;
    }

    setLoading('team', true);
    try {
      const payload = { name, logoUrl: logo || null, leagueId: Number(leagueId) };
      await api.createTeam(payload);
      await reload(); // Reload all data
      e.target.reset();
      setMessage('team', 'Team added successfully!');
    } catch (err) {
      setMessage('team', `Failed to add team: ${err.message}`, true);
    } finally {
      setLoading('team', false);
    }
  };

  const handleDeleteTeam = async (teamId) => {
    if (!window.confirm('Are you sure you want to delete this team? This will also delete all associated players and matches.')) {
      return;
    }

    setLoading(`delete-team-${teamId}`, true);
    try {
      await api.deleteTeam(teamId);
      await reload(); // Reload all data
      setMessage('team', 'Team deleted successfully!');
    } catch (err) {
      setMessage('team', `Failed to delete team: ${err.message}`, true);
    } finally {
      setLoading(`delete-team-${teamId}`, false);
    }
  };

  // Player Management
  const handleAddPlayer = async (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const teamId = formData.get('playerTeamId');
    const name = formData.get('playerName').trim();
    const dateOfBirth = formData.get('dateOfBirth');
    const battingStyle = formData.get('battingStyle') || 'RIGHT_HANDED';
    const bowlingStyle = formData.get('bowlingStyle') || 'RIGHT_ARM_MEDIUM';

    if (!teamId || !name || !dateOfBirth) {
      setMessage('player', 'Team, name, and date of birth are required.', true);
      return;
    }

    setLoading('player', true);
    try {
      const payload = {
        name,
        dateOfBirth,
        battingStyle,
        bowlingStyle,
        teamId: Number(teamId)
      };
      await api.addPlayerToTeam(Number(teamId), payload);
      await reload(); // Reload all data
      e.target.reset();
      setMessage('player', 'Player added successfully!');
    } catch (err) {
      setMessage('player', `Failed to add player: ${err.message}`, true);
    } finally {
      setLoading('player', false);
    }
  };

  // Match Management
  const handleAddMatch = async (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const leagueId = formData.get('league');
    const teamAId = formData.get('teamA');
    const teamBId = formData.get('teamB');
    const venue = formData.get('venue').trim();
    const date = formData.get('date');

    if (!leagueId || !teamAId || !teamBId || !venue || !date) {
      setMessage('match', 'All fields are required.', true);
      return;
    }

    if (teamAId === teamBId) {
      setMessage('match', 'Team A and Team B must be different.', true);
      return;
    }

    setLoading('match', true);
    try {
      const payload = {
        leagueId: Number(leagueId),
        team1Id: Number(teamAId),
        team2Id: Number(teamBId),
        venue,
        matchDate: date,
        status: 'SCHEDULED'
      };
      await api.createMatch(payload);
      await reload(); // Reload all data
      e.target.reset();
      setMessage('match', 'Match added successfully!');
    } catch (err) {
      setMessage('match', `Failed to add match: ${err.message}`, true);
    } finally {
      setLoading('match', false);
    }
  };

  const handleDeleteMatch = async (matchId) => {
    if (!window.confirm('Are you sure you want to delete this match?')) {
      return;
    }

    setLoading(`delete-match-${matchId}`, true);
    try {
      await api.deleteMatch(matchId);
      await reload(); // Reload all data
      setMessage('match', 'Match deleted successfully!');
    } catch (err) {
      setMessage('match', `Failed to delete match: ${err.message}`, true);
    } finally {
      setLoading(`delete-match-${matchId}`, false);
    }
  };

  // Points Management
  const handleUpdatePoints = (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const teamId = formData.get('team');
    const played = Number(formData.get('played') || 0);
    const won = Number(formData.get('won') || 0);
    const lost = Number(formData.get('lost') || 0);
    const points = Number(formData.get('points') || 0);

    if (!teamId) {
      setMessage('points', 'Select a team.', true);
      return;
    }

    if (played < 0 || won < 0 || lost < 0 || points < 0) {
      setMessage('points', 'Values cannot be negative.', true);
      return;
    }

    if (won + lost > played) {
      setMessage('points', 'Won + Lost cannot exceed Played.', true);
      return;
    }

    updateLeagueData(prev => {
      const existing = prev.pointsTable.find(p => p.teamId === teamId);
      if (existing) {
        return {
          ...prev,
          pointsTable: prev.pointsTable.map(p => 
            p.teamId === teamId ? { ...p, played, won, lost, points } : p
          )
        };
      } else {
        return {
          ...prev,
          pointsTable: [...prev.pointsTable, { teamId, played, won, lost, points }]
        };
      }
    });

    e.target.reset();
    setMessage('points', 'Points updated successfully!');
  };

  // Live Controls
  const handleStartLive = () => {
    const select = document.getElementById('live-match-select');
    const matchId = select.value;
    if (!matchId) {
      setMessage('live', 'Select a match to go live.', true);
      return;
    }

    updateLeagueData(prev => ({
      ...prev,
      live: {
        matchId,
        overRuns: { a: [], b: [] },
        votes: { a: 0, b: 0 },
        playerLive: []
      }
    }));

    setMessage('live', 'Live match started!');
  };

  const handleEndLive = () => {
    updateLeagueData(prev => ({
      ...prev,
      live: {
        matchId: '',
        overRuns: { a: [], b: [] },
        votes: { a: 0, b: 0 },
        playerLive: []
      }
    }));

    setMessage('live', 'Live ended.');
  };

  const handleResetVotes = () => {
    updateLeagueData(prev => ({
      ...prev,
      live: { ...prev.live, votes: { a: 0, b: 0 } }
    }));

    setMessage('live', 'Votes reset.');
  };

  const handleResetOvers = () => {
    updateLeagueData(prev => ({
      ...prev,
      live: { ...prev.live, overRuns: { a: [], b: [] } }
    }));

    setMessage('live', 'Runs per over reset.');
  };

  const handleAddOver = (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const overNum = Math.max(1, Number(formData.get('overNum') || 1));
    const team = formData.get('team');
    const runs = Math.max(0, Number(formData.get('runs') || 0));

    if (!leagueData.live.matchId) {
      setMessage('overs', 'Start a live match first.', true);
      return;
    }

    updateLeagueData(prev => {
      const newOverRuns = { ...prev.live.overRuns };
      while (newOverRuns[team].length < overNum) {
        newOverRuns[team].push(0);
      }
      newOverRuns[team][overNum - 1] = runs;

      return {
        ...prev,
        live: { ...prev.live, overRuns: newOverRuns }
      };
    });

    e.target.reset();
    setMessage('overs', `Over ${overNum} updated for Team ${team.toUpperCase()}.`);
  };

  const handleUpdatePlayer = (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const playerId = formData.get('player');
    const runs = Math.max(0, Number(formData.get('runs') || 0));
    const balls = Math.max(0, Number(formData.get('balls') || 0));
    const wkts = Math.max(0, Number(formData.get('wkts') || 0));
    const overs = Math.max(0, Number(formData.get('overs') || 0));

    if (!leagueData.live.matchId) {
      setMessage('playerLive', 'Start a live match first.', true);
      return;
    }

    if (!playerId) {
      setMessage('playerLive', 'Select a player.', true);
      return;
    }

    // Find player's team
    let teamId = '';
    for (const [tId, teamPlayers] of Object.entries(leagueData.players)) {
      if (teamPlayers.some(p => p.id === playerId)) {
        teamId = tId;
        break;
      }
    }

    if (!teamId) {
      setMessage('playerLive', 'Player not found.', true);
      return;
    }

    updateLeagueData(prev => {
      const existing = prev.live.playerLive.find(p => p.playerId === playerId);
      if (existing) {
        return {
          ...prev,
          live: {
            ...prev.live,
            playerLive: prev.live.playerLive.map(p =>
              p.playerId === playerId ? { ...p, runs, balls, wkts, overs } : p
            )
          }
        };
      } else {
        return {
          ...prev,
          live: {
            ...prev.live,
            playerLive: [...prev.live.playerLive, { playerId, teamId, runs, balls, wkts, overs }]
          }
        };
      }
    });

    e.target.reset();
    setMessage('playerLive', 'Player live stats updated.');
  };

  const isLoading = (key) => localLoading[key] || false;
  const getMessage = (key) => messages[key]?.text || '';
  const isError = (key) => messages[key]?.isError || false;

  return (
    <div className="space-y-8">
      {/* Manage Leagues Panel */}
      <div className="space-y-4">
        <div className="flex items-center justify-between">
          <h2 className="text-lg sm:text-xl font-semibold">Manage Leagues</h2>
        </div>
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <form onSubmit={handleAddLeague} className="lg:col-span-1 space-y-3 rounded-lg border border-slate-800/60 bg-surface p-4 shadow-card">
            <div className="grid grid-cols-1 gap-3">
              <label className="text-sm text-slate-300">
                League Name
                <input name="leagueName" type="text" required placeholder="e.g., IPL 2025" className="mt-1 w-full rounded-md border border-slate-700 bg-background px-3 py-2 text-slate-100 placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-primary/60" />
              </label>
              <label className="text-sm text-slate-300">
                Start Date
                <input name="startDate" type="date" required className="mt-1 w-full rounded-md border border-slate-700 bg-background px-3 py-2 text-slate-100 focus:outline-none focus:ring-2 focus:ring-primary/60" />
              </label>
              <label className="text-sm text-slate-300">
                End Date
                <input name="endDate" type="date" required className="mt-1 w-full rounded-md border border-slate-700 bg-background px-3 py-2 text-slate-100 focus:outline-none focus:ring-2 focus:ring-primary/60" />
              </label>
              <label className="text-sm text-slate-300">
                Status
                <select name="status" className="mt-1 w-full rounded-md border border-slate-700 bg-background px-3 py-2 text-slate-100 focus:outline-none focus:ring-2 focus:ring-primary/60">
                  <option value="UPCOMING">Upcoming</option>
                  <option value="ONGOING">Ongoing</option>
                  <option value="COMPLETED">Completed</option>
                  <option value="CANCELLED">Cancelled</option>
                </select>
              </label>
            </div>
            <button type="submit" disabled={isLoading('league')} className="w-full rounded-md bg-primary/90 px-4 py-2 text-sm font-semibold text-slate-900 hover:bg-primary transition-colors disabled:opacity-50 disabled:cursor-not-allowed">
              {isLoading('league') ? 'Adding...' : 'Add League'}
            </button>
            {getMessage('league') && (
              <p className={`text-xs ${isError('league') ? 'text-rose-400' : 'text-emerald-400'}`}>{getMessage('league')}</p>
            )}
          </form>

          <div className="lg:col-span-2 space-y-3 rounded-lg border border-slate-800/60 bg-surface p-4 shadow-card">
            <h3 className="text-sm font-medium text-slate-300">Existing Leagues</h3>
            <ul className="divide-y divide-slate-800/60">
              {leagueData.leagues.length === 0 ? (
                <li className="px-2 py-3 text-sm text-slate-400">No leagues added.</li>
              ) : (
                leagueData.leagues.map(league => (
                  <li key={league.id} className="flex items-center justify-between gap-2 px-2 py-3">
                    <div className="min-w-0">
                      <div className="text-sm font-medium truncate">{league.name}</div>
                      <div className="text-xs text-slate-400 truncate">
                        {league.startDate} to {league.endDate} • {league.status}
                      </div>
                    </div>
                  </li>
                ))
              )}
            </ul>
          </div>
        </div>
      </div>

      {/* Manage Teams Panel */}
      <div className="space-y-4">
        <div className="flex items-center justify-between">
          <h2 className="text-lg sm:text-xl font-semibold">Manage Teams</h2>
        </div>
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <form onSubmit={handleAddTeam} className="lg:col-span-1 space-y-3 rounded-lg border border-slate-800/60 bg-surface p-4 shadow-card">
            <div className="grid grid-cols-1 gap-3">
              <label className="text-sm text-slate-300">
                League
                <select name="leagueId" required className="mt-1 w-full rounded-md border border-slate-700 bg-background px-3 py-2 text-slate-100 focus:outline-none focus:ring-2 focus:ring-primary/60">
                  <option value="" disabled>Select league</option>
                  {leagueData.leagues.map(l => (
                    <option key={l.id} value={l.id}>{l.name}</option>
                  ))}
                </select>
              </label>
              <label className="text-sm text-slate-300">
                Team Name
                <input name="teamName" type="text" required placeholder="e.g., Thunder Strikers" className="mt-1 w-full rounded-md border border-slate-700 bg-background px-3 py-2 text-slate-100 placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-primary/60" />
              </label>
              <label className="text-sm text-slate-300">
                Logo URL
                <input name="teamLogo" type="url" placeholder="https://..." className="mt-1 w-full rounded-md border border-slate-700 bg-background px-3 py-2 text-slate-100 placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-primary/60" />
              </label>
            </div>
            <button type="submit" disabled={isLoading('team')} className="w-full rounded-md bg-primary/90 px-4 py-2 text-sm font-semibold text-slate-900 hover:bg-primary transition-colors disabled:opacity-50 disabled:cursor-not-allowed">
              {isLoading('team') ? 'Adding...' : 'Add Team'}
            </button>
            {getMessage('team') && (
              <p className={`text-xs ${isError('team') ? 'text-rose-400' : 'text-emerald-400'}`}>{getMessage('team')}</p>
            )}
          </form>

          <div className="lg:col-span-2 space-y-3 rounded-lg border border-slate-800/60 bg-surface p-4 shadow-card">
            <h3 className="text-sm font-medium text-slate-300">Existing Teams</h3>
            <ul className="divide-y divide-slate-800/60">
              {leagueData.teams.length === 0 ? (
                <li className="px-2 py-3 text-sm text-slate-400">No teams added.</li>
              ) : (
                leagueData.teams.map(team => (
                  <li key={team.id} className="flex items-center justify-between gap-2 px-2 py-3">
                    <div className="flex items-center gap-3 min-w-0">
                      <img src={team.logo || 'https://placehold.co/40x40/334155/94a3b8?text=T'} alt="logo" className="h-8 w-8 rounded object-cover ring-1 ring-slate-700" onError={(e) => e.target.src = 'https://placehold.co/40x40/334155/94a3b8?text=?'} />
                      <div className="min-w-0">
                        <div className="text-sm font-medium truncate">{team.name}</div>
                        <div className="text-xs text-slate-400 truncate">
                          Players: {leagueData.players[team.id]?.length || 0}
                        </div>
                      </div>
                    </div>
                    <button 
                      onClick={() => handleDeleteTeam(team.id)} 
                      disabled={isLoading(`delete-team-${team.id}`)}
                      className="rounded-md border border-rose-700/50 bg-rose-600/20 px-3 py-1.5 text-xs font-semibold text-rose-200 hover:bg-rose-600/30 disabled:opacity-50"
                    >
                      {isLoading(`delete-team-${team.id}`) ? '...' : 'Delete'}
                    </button>
                  </li>
                ))
              )}
            </ul>
          </div>
        </div>
      </div>

      {/* Manage Players Panel */}
      <div className="space-y-4">
        <div className="flex items-center justify-between">
          <h2 className="text-lg sm:text-xl font-semibold">Manage Players</h2>
        </div>
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <form onSubmit={handleAddPlayer} className="lg:col-span-1 space-y-3 rounded-lg border border-slate-800/60 bg-surface p-4 shadow-card">
            <div className="grid grid-cols-1 gap-3">
              <label className="text-sm text-slate-300">
                Team
                <select name="playerTeamId" required className="mt-1 w-full rounded-md border border-slate-700 bg-background px-3 py-2 text-slate-100 focus:outline-none focus:ring-2 focus:ring-primary/60">
                  <option value="" disabled>Select team</option>
                  {leagueData.teams.map(team => (
                    <option key={team.id} value={team.id}>{team.name}</option>
                  ))}
                </select>
              </label>
              <label className="text-sm text-slate-300">
                Player Name
                <input name="playerName" type="text" required placeholder="e.g., Virat Kohli" className="mt-1 w-full rounded-md border border-slate-700 bg-background px-3 py-2 text-slate-100 placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-primary/60" />
              </label>
              <label className="text-sm text-slate-300">
                Date of Birth
                <input name="dateOfBirth" type="date" required className="mt-1 w-full rounded-md border border-slate-700 bg-background px-3 py-2 text-slate-100 focus:outline-none focus:ring-2 focus:ring-primary/60" />
              </label>
              <label className="text-sm text-slate-300">
                Batting Style
                <select name="battingStyle" className="mt-1 w-full rounded-md border border-slate-700 bg-background px-3 py-2 text-slate-100 focus:outline-none focus:ring-2 focus:ring-primary/60">
                  <option value="RIGHT_HANDED">Right Handed</option>
                  <option value="LEFT_HANDED">Left Handed</option>
                </select>
              </label>
              <label className="text-sm text-slate-300">
                Bowling Style
                <select name="bowlingStyle" className="mt-1 w-full rounded-md border border-slate-700 bg-background px-3 py-2 text-slate-100 focus:outline-none focus:ring-2 focus:ring-primary/60">
                  <option value="RIGHT_ARM_FAST">Right Arm Fast</option>
                  <option value="RIGHT_ARM_MEDIUM">Right Arm Medium</option>
                  <option value="RIGHT_ARM_OFF_SPIN">Right Arm Off Spin</option>
                  <option value="RIGHT_ARM_LEG_SPIN">Right Arm Leg Spin</option>
                  <option value="LEFT_ARM_FAST">Left Arm Fast</option>
                  <option value="LEFT_ARM_MEDIUM">Left Arm Medium</option>
                  <option value="LEFT_ARM_ORTHODOX">Left Arm Orthodox</option>
                  <option value="LEFT_ARM_LEG_SPIN">Left Arm Leg Spin</option>
                </select>
              </label>
            </div>
            <button type="submit" disabled={isLoading('player')} className="w-full rounded-md bg-primary/90 px-4 py-2 text-sm font-semibold text-slate-900 hover:bg-primary transition-colors disabled:opacity-50 disabled:cursor-not-allowed">
              {isLoading('player') ? 'Adding...' : 'Add Player'}
            </button>
            {getMessage('player') && (
              <p className={`text-xs ${isError('player') ? 'text-rose-400' : 'text-emerald-400'}`}>{getMessage('player')}</p>
            )}
          </form>

          <div className="lg:col-span-2 space-y-3 rounded-lg border border-slate-800/60 bg-surface p-4 shadow-card">
            <h3 className="text-sm font-medium text-slate-300">Players by Team</h3>
            <div className="space-y-4">
              {leagueData.teams.length === 0 ? (
                <p className="text-sm text-slate-400">No teams available. Add a team first.</p>
              ) : (
                leagueData.teams.map(team => {
                  const players = leagueData.players[team.id] || [];
                  return (
                    <div key={team.id} className="border-b border-slate-800/60 pb-4 last:border-0 last:pb-0">
                      <h4 className="text-sm font-semibold text-slate-200 mb-2">{team.name} ({players.length} players)</h4>
                      {players.length === 0 ? (
                        <p className="text-xs text-slate-500">No players added yet.</p>
                      ) : (
                        <ul className="space-y-1">
                          {players.map(player => (
                            <li key={player.id} className="text-xs text-slate-400 flex items-center justify-between">
                              <span>{player.name}</span>
                              <span className="text-slate-500">{player.stats?.matches || 0} matches</span>
                            </li>
                          ))}
                        </ul>
                      )}
                    </div>
                  );
                })
              )}
            </div>
          </div>
        </div>
      </div>

      {/* Manage Matches Panel */}
      <div className="space-y-4">
        <div className="flex items-center justify-between">
          <h2 className="text-lg sm:text-xl font-semibold">Manage Matches</h2>
        </div>
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <form onSubmit={handleAddMatch} className="lg:col-span-1 space-y-3 rounded-lg border border-slate-800/60 bg-surface p-4 shadow-card">
            <div className="grid grid-cols-1 gap-3">
              <label className="text-sm text-slate-300">
                League
                <select name="league" required className="mt-1 w-full rounded-md border border-slate-700 bg-background px-3 py-2 text-slate-100 focus:outline-none focus:ring-2 focus:ring-primary/60">
                  <option value="" disabled>Select league</option>
                  {leagueData.leagues.map(l => (
                    <option key={l.id} value={l.id}>{l.name}</option>
                  ))}
                </select>
              </label>
              <label className="text-sm text-slate-300">
                Team A
                <select name="teamA" required className="mt-1 w-full rounded-md border border-slate-700 bg-background px-3 py-2 text-slate-100 focus:outline-none focus:ring-2 focus:ring-primary/60">
                  <option value="" disabled>Select team</option>
                  {leagueData.teams
                    .filter(t => !leagueData.activeLeagueId || String(t.leagueId) === String(leagueData.activeLeagueId))
                    .map(team => (
                      <option key={team.id} value={team.id}>{team.name}</option>
                    ))}
                </select>
              </label>
              <label className="text-sm text-slate-300">
                Team B
                <select name="teamB" required className="mt-1 w-full rounded-md border border-slate-700 bg-background px-3 py-2 text-slate-100 focus:outline-none focus:ring-2 focus:ring-primary/60">
                  <option value="" disabled>Select team</option>
                  {leagueData.teams
                    .filter(t => !leagueData.activeLeagueId || String(t.leagueId) === String(leagueData.activeLeagueId))
                    .map(team => (
                      <option key={team.id} value={team.id}>{team.name}</option>
                    ))}
                </select>
              </label>
              <label className="text-sm text-slate-300">
                Venue
                <input name="venue" type="text" required placeholder="e.g., National Stadium" className="mt-1 w-full rounded-md border border-slate-700 bg-background px-3 py-2 text-slate-100 placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-primary/60" />
              </label>
              <label className="text-sm text-slate-300">
                Date
                <input name="date" type="date" required className="mt-1 w-full rounded-md border border-slate-700 bg-background px-3 py-2 text-slate-100 focus:outline-none focus:ring-2 focus:ring-primary/60" />
              </label>
            </div>
            <button type="submit" disabled={isLoading('match')} className="w-full rounded-md bg-primary/90 px-4 py-2 text-sm font-semibold text-slate-900 hover:bg-primary transition-colors disabled:opacity-50 disabled:cursor-not-allowed">
              {isLoading('match') ? 'Adding...' : 'Add Match'}
            </button>
            {getMessage('match') && (
              <p className={`text-xs ${isError('match') ? 'text-rose-400' : 'text-emerald-400'}`}>{getMessage('match')}</p>
            )}
          </form>

          <div className="lg:col-span-2 space-y-3 rounded-lg border border-slate-800/60 bg-surface p-4 shadow-card">
            <h3 className="text-sm font-medium text-slate-300">Scheduled Matches</h3>
            <ul className="divide-y divide-slate-800/60">
              {leagueData.matches.length === 0 ? (
                <li className="px-2 py-3 text-sm text-slate-400">No matches scheduled.</li>
              ) : (
                leagueData.matches
                  .slice()
                  .sort((a,b) => String(a.date).localeCompare(String(b.date)))
                  .map(match => (
                    <li key={match.id} className="flex items-center justify-between gap-2 px-2 py-3">
                      <div className="min-w-0">
                        <div className="text-sm font-medium truncate">{formatMatchLabel(match.teamAId, match.teamBId)}</div>
                        <div className="text-xs text-slate-400 truncate">{match.date} • {match.venue}</div>
                      </div>
                      <button 
                        onClick={() => handleDeleteMatch(match.id)} 
                        disabled={isLoading(`delete-match-${match.id}`)}
                        className="rounded-md border border-rose-700/50 bg-rose-600/20 px-3 py-1.5 text-xs font-semibold text-rose-200 hover:bg-rose-600/30 disabled:opacity-50"
                      >
                        {isLoading(`delete-match-${match.id}`) ? '...' : 'Delete'}
                      </button>
                    </li>
                  ))
              )}
            </ul>
          </div>
        </div>
      </div>

      {/* Update Points Table Panel */}
      <div className="space-y-4">
        <div className="flex items-center justify-between">
          <h2 className="text-lg sm:text-xl font-semibold">Update Points Table</h2>
        </div>
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <form onSubmit={handleUpdatePoints} className="lg:col-span-1 space-y-3 rounded-lg border border-slate-800/60 bg-surface p-4 shadow-card">
            <div className="grid grid-cols-1 gap-3">
              <label className="text-sm text-slate-300">
                Team
                <select name="team" required className="mt-1 w-full rounded-md border border-slate-700 bg-background px-3 py-2 text-slate-100 focus:outline-none focus:ring-2 focus:ring-primary/60">
                  <option value="" disabled>Select team</option>
                  {leagueData.teams.map(team => (
                    <option key={team.id} value={team.id}>{team.name}</option>
                  ))}
                </select>
              </label>
              <label className="text-sm text-slate-300">
                Played
                <input name="played" type="number" min="0" defaultValue="0" className="mt-1 w-full rounded-md border border-slate-700 bg-background px-3 py-2 text-slate-100 focus:outline-none focus:ring-2 focus:ring-primary/60" />
              </label>
              <label className="text-sm text-slate-300">
                Won
                <input name="won" type="number" min="0" defaultValue="0" className="mt-1 w-full rounded-md border border-slate-700 bg-background px-3 py-2 text-slate-100 focus:outline-none focus:ring-2 focus:ring-primary/60" />
              </label>
              <label className="text-sm text-slate-300">
                Lost
                <input name="lost" type="number" min="0" defaultValue="0" className="mt-1 w-full rounded-md border border-slate-700 bg-background px-3 py-2 text-slate-100 focus:outline-none focus:ring-2 focus:ring-primary/60" />
              </label>
              <label className="text-sm text-slate-300">
                Points
                <input name="points" type="number" min="0" defaultValue="0" className="mt-1 w-full rounded-md border border-slate-700 bg-background px-3 py-2 text-slate-100 focus:outline-none focus:ring-2 focus:ring-primary/60" />
              </label>
            </div>
            <button type="submit" className="w-full rounded-md bg-primary/90 px-4 py-2 text-sm font-semibold text-slate-900 hover:bg-primary transition-colors">Update Stats</button>
            {getMessage('points') && (
              <p className={`text-xs ${isError('points') ? 'text-rose-400' : 'text-emerald-400'}`}>{getMessage('points')}</p>
            )}
          </form>

          <div className="lg:col-span-2 space-y-3 rounded-lg border border-slate-800/60 bg-surface p-4 shadow-card">
            <h3 className="text-sm font-medium text-slate-300">Current Standings</h3>
            <div className="overflow-x-auto">
              <table className="min-w-full text-sm">
                <thead className="bg-background/60">
                  <tr className="text-left text-slate-300">
                    <th className="px-4 py-3">Team</th>
                    <th className="px-4 py-3">Played</th>
                    <th className="px-4 py-3">Won</th>
                    <th className="px-4 py-3">Lost</th>
                    <th className="px-4 py-3">Points</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-800/60">
                  {leagueData.pointsTable.length === 0 ? (
                    <tr>
                      <td colSpan="5" className="px-4 py-4 text-slate-400 text-sm">No standings yet.</td>
                    </tr>
                  ) : (
                    sortStandings(leagueData.pointsTable).map(row => {
                      const team = getTeamById(row.teamId);
                      return (
                        <tr key={row.teamId}>
                          <td className="px-4 py-3">{team ? team.name : 'Unknown'}</td>
                          <td className="px-4 py-3">{row.played}</td>
                          <td className="px-4 py-3">{row.won}</td>
                          <td className="px-4 py-3">{row.lost}</td>
                          <td className="px-4 py-3">{row.points}</td>
                        </tr>
                      );
                    })
                  )}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>

      {/* Live Controls Panel */}
      <div className="space-y-4">
        <div className="flex items-center justify-between">
          <h2 className="text-lg sm:text-xl font-semibold">Live Match Controls</h2>
        </div>
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <form className="lg:col-span-1 space-y-3 rounded-lg border border-slate-800/60 bg-surface p-4 shadow-card">
            <div className="grid grid-cols-1 gap-3">
              <label className="text-sm text-slate-300">
                Choose Live Match
                <select id="live-match-select" className="mt-1 w-full rounded-md border border-slate-700 bg-background px-3 py-2 text-slate-100 focus:outline-none focus:ring-2 focus:ring-primary/60">
                  <option value="" disabled>Select match</option>
                  {leagueData.matches
                    .slice()
                    .sort((a,b) => String(a.date).localeCompare(String(b.date)))
                    .map(match => (
                      <option key={match.id} value={match.id}>{formatMatchLabel(match.teamAId, match.teamBId)} • {match.date}</option>
                    ))
                  }
                </select>
              </label>
            </div>
            <div className="flex gap-2">
              <button type="button" onClick={handleStartLive} className="flex-1 rounded-md bg-emerald-500/20 border border-emerald-600/50 px-4 py-2 text-xs font-semibold text-emerald-200 hover:bg-emerald-500/30">Start Live</button>
              <button type="button" onClick={handleEndLive} className="flex-1 rounded-md bg-rose-600/20 border border-rose-700/50 px-4 py-2 text-xs font-semibold text-rose-200 hover:bg-rose-600/30">End Live</button>
            </div>
            <div className="flex gap-2">
              <button type="button" onClick={handleResetVotes} className="flex-1 rounded-md bg-background border border-slate-700 px-4 py-2 text-xs font-semibold text-slate-200 hover:bg-background/70">Reset Votes</button>
              <button type="button" onClick={handleResetOvers} className="flex-1 rounded-md bg-background border border-slate-700 px-4 py-2 text-xs font-semibold text-slate-200 hover:bg-background/70">Reset Overs</button>
            </div>
            {getMessage('live') && (
              <p className={`text-xs ${isError('live') ? 'text-rose-400' : 'text-emerald-400'}`}>{getMessage('live')}</p>
            )}
          </form>

          <form onSubmit={handleAddOver} className="lg:col-span-1 space-y-3 rounded-lg border border-slate-800/60 bg-surface p-4 shadow-card">
            <div className="grid grid-cols-1 gap-3">
              <label className="text-sm text-slate-300">
                Over Number
                <input name="overNum" type="number" min="1" defaultValue="1" className="mt-1 w-full rounded-md border border-slate-700 bg-background px-3 py-2 text-slate-100 focus:outline-none focus:ring-2 focus:ring-primary/60" />
              </label>
              <label className="text-sm text-slate-300">
                Team
                <select name="team" className="mt-1 w-full rounded-md border border-slate-700 bg-background px-3 py-2 text-slate-100 focus:outline-none focus:ring-2 focus:ring-primary/60">
                  <option value="a">Team A</option>
                  <option value="b">Team B</option>
                </select>
              </label>
              <label className="text-sm text-slate-300">
                Runs in Over
                <input name="runs" type="number" min="0" defaultValue="0" className="mt-1 w-full rounded-md border border-slate-700 bg-background px-3 py-2 text-slate-100 focus:outline-none focus:ring-2 focus:ring-primary/60" />
              </label>
            </div>
            <button type="submit" className="w-full rounded-md bg-primary/90 px-4 py-2 text-sm font-semibold text-slate-900 hover:bg-primary transition-colors">Add Over Score</button>
            {getMessage('overs') && (
              <p className={`text-xs ${isError('overs') ? 'text-rose-400' : 'text-emerald-400'}`}>{getMessage('overs')}</p>
            )}
          </form>

          <form onSubmit={handleUpdatePlayer} className="lg:col-span-1 space-y-3 rounded-lg border border-slate-800/60 bg-surface p-4 shadow-card">
            <div className="grid grid-cols-1 gap-3">
              <label className="text-sm text-slate-300">
                Player
                <select name="player" className="mt-1 w-full rounded-md border border-slate-700 bg-background px-3 py-2 text-slate-100 focus:outline-none focus:ring-2 focus:ring-primary/60">
                  <option value="" disabled>Select player</option>
                  {Object.entries(leagueData.players).map(([teamId, players]) => 
                    players.map(player => (
                      <option key={player.id} value={player.id}>{player.name} ({getTeamById(teamId)?.name})</option>
                    ))
                  )}
                </select>
              </label>
              <div className="grid grid-cols-2 gap-3">
                <label className="text-sm text-slate-300">
                  Runs
                  <input name="runs" type="number" min="0" defaultValue="0" className="mt-1 w-full rounded-md border border-slate-700 bg-background px-3 py-2 text-slate-100 focus:outline-none focus:ring-2 focus:ring-primary/60" />
                </label>
                <label className="text-sm text-slate-300">
                  Balls
                  <input name="balls" type="number" min="0" defaultValue="0" className="mt-1 w-full rounded-md border border-slate-700 bg-background px-3 py-2 text-slate-100 focus:outline-none focus:ring-2 focus:ring-primary/60" />
                </label>
                <label className="text-sm text-slate-300">
                  Wickets
                  <input name="wkts" type="number" min="0" defaultValue="0" className="mt-1 w-full rounded-md border border-slate-700 bg-background px-3 py-2 text-slate-100 focus:outline-none focus:ring-2 focus:ring-primary/60" />
                </label>
                <label className="text-sm text-slate-300">
                  Overs
                  <input name="overs" type="number" min="0" step="0.1" defaultValue="0" className="mt-1 w-full rounded-md border border-slate-700 bg-background px-3 py-2 text-slate-100 focus:outline-none focus:ring-2 focus:ring-primary/60" />
                </label>
              </div>
            </div>
            <button type="submit" className="w-full rounded-md bg-primary/90 px-4 py-2 text-sm font-semibold text-slate-900 hover:bg-primary transition-colors">Update Player Live Stats</button>
            {getMessage('playerLive') && (
              <p className={`text-xs ${isError('playerLive') ? 'text-rose-400' : 'text-emerald-400'}`}>{getMessage('playerLive')}</p>
            )}
          </form>
        </div>
      </div>
    </div>
  );
};

export default Admin;
