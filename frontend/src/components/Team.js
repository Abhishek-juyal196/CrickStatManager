import React, { useState, useEffect } from 'react';
import { useLeague } from '../App';

const Team = () => {
  const { leagueData, selectedTeamId, setSelectedTeamId } = useLeague();
  const [currentTeamId, setCurrentTeamId] = useState(selectedTeamId || leagueData.teams[0]?.id || '');

  useEffect(() => {
    if (selectedTeamId) {
      setCurrentTeamId(selectedTeamId);
    } else if (leagueData.teams.length > 0 && !currentTeamId) {
      setCurrentTeamId(leagueData.teams[0].id);
    }
  }, [selectedTeamId, leagueData.teams, currentTeamId]);

  const getTeamById = (teamId) => {
    return leagueData.teams.find(t => t.id === teamId);
  };

  const getTeamPerformance = (teamId) => {
    return leagueData.teamPerformance[teamId] || { formLast5: [], runsFor: 0, runsAgainst: 0, netRunRate: 0 };
  };

  const handleTeamChange = (e) => {
    const newTeamId = e.target.value;
    setCurrentTeamId(newTeamId);
    setSelectedTeamId(newTeamId);
  };

  const currentTeam = getTeamById(currentTeamId);
  const performance = getTeamPerformance(currentTeamId);
  const pointsRow = leagueData.pointsTable.find(p => p.teamId === currentTeamId) || { played: 0, won: 0, lost: 0, points: 0 };
  const players = leagueData.players[currentTeamId] || [];

  return (
    <div className="space-y-8">
      <div className="space-y-4">
        <div className="flex items-center justify-between">
          <h2 className="text-lg sm:text-xl font-semibold">Team Center</h2>
        </div>
        <div className="rounded-lg border border-slate-800/60 bg-surface p-4 shadow-card grid grid-cols-1 md:grid-cols-3 gap-4">
          <div className="md:col-span-1">
            <label className="text-sm text-slate-300">
              Select Team
              <select 
                value={currentTeamId} 
                onChange={handleTeamChange}
                className="mt-1 w-full rounded-md border border-slate-700 bg-background px-3 py-2 text-slate-100 focus:outline-none focus:ring-2 focus:ring-primary/60"
              >
                {leagueData.teams.map(team => (
                  <option key={team.id} value={team.id}>{team.name}</option>
                ))}
              </select>
            </label>
          </div>
          <div className="md:col-span-2 grid grid-cols-2 sm:grid-cols-4 gap-3">
            <div className="rounded-md border border-slate-700 bg-background p-3">
              <div className="text-xs text-slate-400">Played</div>
              <div className="text-lg font-semibold">{pointsRow.played}</div>
            </div>
            <div className="rounded-md border border-slate-700 bg-background p-3">
              <div className="text-xs text-slate-400">Won</div>
              <div className="text-lg font-semibold text-emerald-400">{pointsRow.won}</div>
            </div>
            <div className="rounded-md border border-slate-700 bg-background p-3">
              <div className="text-xs text-slate-400">Points</div>
              <div className="text-lg font-semibold text-primary">{pointsRow.points}</div>
            </div>
            <div className="rounded-md border border-slate-700 bg-background p-3">
              <div className="text-xs text-slate-400">NRR</div>
              <div className={`text-lg font-semibold ${performance.netRunRate >= 0 ? 'text-emerald-400' : 'text-rose-400'}`}>
                {performance.netRunRate.toFixed(2)}
              </div>
            </div>
            <div className="col-span-2 sm:col-span-4 rounded-md border border-slate-700 bg-background p-3">
              <div className="text-xs text-slate-400">Form (Last 5)</div>
              <div className="mt-1 text-sm font-medium">
                {performance.formLast5 && performance.formLast5.length ? performance.formLast5.join(' ') : '-'}
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className="space-y-3">
        <h3 className="text-sm font-medium text-slate-300">Players & Performance</h3>
        <div className="overflow-hidden rounded-lg border border-slate-800/60 bg-surface shadow-card">
          <div className="overflow-x-auto">
            <table className="min-w-full text-sm">
              <thead className="bg-background/60">
                <tr className="text-left text-slate-300">
                  <th className="px-4 py-3">Player</th>
                  <th className="px-4 py-3">Role</th>
                  <th className="px-4 py-3">Matches</th>
                  <th className="px-4 py-3">Runs</th>
                  <th className="px-4 py-3">Wickets</th>
                  <th className="px-4 py-3">SR</th>
                  <th className="px-4 py-3">Eco</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-800/60">
                {players.length === 0 ? (
                  <tr>
                    <td colSpan="7" className="px-4 py-4 text-sm text-slate-400">No players data.</td>
                  </tr>
                ) : (
                  players.map(player => (
                    <tr key={player.id}>
                      <td className="px-4 py-3">{player.name}</td>
                      <td className="px-4 py-3 whitespace-nowrap">{player.role}</td>
                      <td className="px-4 py-3">{player.stats.matches}</td>
                      <td className="px-4 py-3">{player.stats.runs}</td>
                      <td className="px-4 py-3">{player.stats.wickets}</td>
                      <td className="px-4 py-3">{player.stats.strikeRate}</td>
                      <td className="px-4 py-3">{player.stats.economy}</td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Team;

