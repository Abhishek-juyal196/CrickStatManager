import React from 'react';
import { useLeague } from '../App';

const Home = () => {
  const { leagueData, setActiveTab, setSelectedTeamId, loading, error } = useLeague();

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

  const handleTeamClick = (teamId) => {
    setSelectedTeamId(teamId);
    setActiveTab('team');
  };

  return (
    <div className="space-y-8">
      {loading && (
        <div className="rounded-md border border-gray-700 bg-gray-800 p-3 text-sm text-gray-300">Loading...</div>
      )}
      {!!error && (
        <div className="rounded-md border border-rose-700/50 bg-rose-600/10 p-3 text-sm text-rose-200">{error}</div>
      )}
      {/* Teams Grid */}
      <div className="space-y-4">
        <div className="flex items-center justify-between">
          <h2 className="text-lg sm:text-xl font-semibold text-white">Teams</h2>
        </div>
        <div className="grid gap-4 sm:gap-6 grid-cols-1 sm:grid-cols-2 lg:grid-cols-3">
          {leagueData.teams.length === 0 ? (
            <div className="text-gray-400 text-sm">No teams yet. Add some from Admin Panel.</div>
          ) : (
            leagueData.teams.map(team => {
              const logoSrc = team.logo && String(team.logo).trim() !== '' 
                ? team.logo 
                : `https://placehold.co/96x96/334155/94a3b8?text=${encodeURIComponent(team.name.charAt(0).toUpperCase())}`;
              
              return (
                <div
                  key={team.id}
                  onClick={() => handleTeamClick(team.id)}
                  className="rounded-lg border border-gray-700 bg-gray-800 p-4 shadow-lg flex items-center gap-4 hover:border-blue-500 hover:bg-gray-700 cursor-pointer focus:outline-none focus:ring-2 focus:ring-blue-500"
                  role="button"
                  tabIndex="0"
                >
                  <img 
                    src={logoSrc} 
                    alt={`${team.name} logo`} 
                    className="h-16 w-16 rounded-md object-cover ring-1 ring-gray-600" 
                    onError={(e) => e.target.src = 'https://placehold.co/96x96/334155/94a3b8?text=?'}
                  />
                  <div className="min-w-0">
                    <div className="font-semibold truncate text-white">{team.name}</div>
                    <div className="text-xs text-gray-400">League: {leagueData.leagues.find(l => String(l.id) === String(team.leagueId))?.name || '-'}</div>
                  </div>
                </div>
              );
            })
          )}
        </div>
      </div>

      {/* Match Schedule */}
      <div className="space-y-4">
        <div className="flex items-center justify-between">
          <h2 className="text-lg sm:text-xl font-semibold text-white">Upcoming Matches</h2>
        </div>
        <div className="overflow-hidden rounded-lg border border-gray-700 bg-gray-800 shadow-lg">
          <div className="overflow-x-auto">
            <table className="min-w-full text-sm">
              <thead className="bg-gray-900">
                <tr className="text-left text-gray-300">
                  <th className="px-4 py-3">Date</th>
                  <th className="px-4 py-3">Match</th>
                  <th className="px-4 py-3">Venue</th>
                  <th className="px-4 py-3">Time</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-700">
                {leagueData.matches.length === 0 ? (
                  <tr>
                    <td colSpan="4" className="px-4 py-4 text-gray-400 text-sm">No matches scheduled.</td>
                  </tr>
                ) : (
                  leagueData.matches
                    .slice()
                    .sort((a,b) => String(a.date).localeCompare(String(b.date)))
                    .map(match => (
                      <tr key={match.id}>
                        <td className="px-4 py-3 whitespace-nowrap text-white">{match.date}</td>
                        <td className="px-4 py-3 text-white">{formatMatchLabel(match.teamAId, match.teamBId)}</td>
                        <td className="px-4 py-3 whitespace-nowrap text-white">{match.venue}</td>
                        <td className="px-4 py-3 whitespace-nowrap text-white">{match.time || '-'}</td>
                      </tr>
                    ))
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>

      {/* Points Table */}
      <div className="space-y-4">
        <div className="flex items-center justify-between">
          <h2 className="text-lg sm:text-xl font-semibold text-white">Points Table</h2>
        </div>
        <div className="overflow-hidden rounded-lg border border-gray-700 bg-gray-800 shadow-lg">
          <div className="overflow-x-auto">
            <table className="min-w-full text-sm">
              <thead className="bg-gray-900">
                <tr className="text-left text-gray-300">
                  <th className="px-4 py-3">Position</th>
                  <th className="px-4 py-3">Team</th>
                  <th className="px-4 py-3">Played</th>
                  <th className="px-4 py-3">Won</th>
                  <th className="px-4 py-3">Lost</th>
                  <th className="px-4 py-3">Points</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-700">
                {leagueData.pointsTable.length === 0 ? (
                  <tr>
                    <td colSpan="6" className="px-4 py-4 text-gray-400 text-sm">No standings yet.</td>
                  </tr>
                ) : (
                  sortStandings(leagueData.pointsTable).map((row, idx) => {
                    const team = getTeamById(row.teamId);
                    return (
                      <tr key={row.teamId}>
                        <td className="px-4 py-3 text-white">{idx + 1}</td>
                        <td className="px-4 py-3 text-white">{team ? team.name : 'Unknown'}</td>
                        <td className="px-4 py-3 text-white">{row.played}</td>
                        <td className="px-4 py-3 text-white">{row.won}</td>
                        <td className="px-4 py-3 text-white">{row.lost}</td>
                        <td className="px-4 py-3 text-white">{row.points}</td>
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
  );
};

export default Home;
