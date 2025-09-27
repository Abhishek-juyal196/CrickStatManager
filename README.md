# Cricket League & Match Scorecard Management System

A comprehensive backend system for cricket scorers to manage leagues, teams, matches, and real-time scoring built with Spring Boot 3.x and PostgreSQL.

## 🏏 Features

### Core Functionality
- **League Management**: Create and manage cricket leagues with start/end dates
- **Team & Player Management**: Register teams with players and their details
- **Match Scheduling**: Schedule matches between teams
- **Real-time Scoring**: Ball-by-ball scoring with live statistics
- **Statistics & Analytics**: Comprehensive player and team statistics
- **Awards System**: Man of the Match, Orange Cap, Purple Cap calculations

### Advanced Features
- **Undo/Redo Scoring**: Stack-based ball management for error correction
- **Real-time Statistics**: Event-driven updates for live scoring
- **Partnership Tracking**: Monitor batting partnerships
- **Run Rate Calculations**: Current Run Rate (CRR) and Required Run Rate (RRR)
- **Net Run Rate (NRR)**: Team performance metrics
- **Over Progression**: Automatic over and ball counting
- **Cricket Rules Validation**: Ensures valid scoring according to cricket rules

## 🛠️ Tech Stack

- **Backend**: Spring Boot 3.x (Java 17+)
- **Database**: PostgreSQL 15+
- **Build Tool**: Maven
- **API Documentation**: OpenAPI 3 (Swagger)
- **Testing**: JUnit 5, Mockito, TestContainers
- **Validation**: Jakarta Validation API

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 15+
- Git

## 🚀 Getting Started

### 1. Clone the Repository
```bash
git clone <repository-url>
cd cricket-league-management
```

### 2. Database Setup
```bash
# Create PostgreSQL database
createdb cricket_league

# Update application.yml with your database credentials
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/cricket_league
    username: your_username
    password: your_password
```

### 3. Run the Application
```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

### 4. Access the Application
- **API Base URL**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs

## 📊 Database Schema

### Core Entities
- **leagues**: Tournament details with start/end dates
- **teams**: Team information and league association
- **players**: Player details with batting/bowling styles
- **matches**: Match details with venue and date
- **innings**: Batting/bowling innings data
- **balls**: Ball-by-ball scoring data
- **match_scorecards**: Aggregated match statistics

### Key Relationships
- League → Teams (One-to-Many)
- Team → Players (One-to-Many)
- Match → Innings (One-to-Many)
- Innings → Balls (One-to-Many)
- Match → Scorecard (One-to-One)

## 🔌 API Endpoints

### League Management
- `POST /api/leagues` - Create new league
- `GET /api/leagues` - List all leagues
- `GET /api/leagues/{id}` - Get league by ID
- `PUT /api/leagues/{id}` - Update league
- `DELETE /api/leagues/{id}` - Delete league
- `POST /api/leagues/{id}/start` - Start league
- `POST /api/leagues/{id}/complete` - Complete league
- `GET /api/leagues/{id}/standings` - Get points table

### Team & Player Management
- `POST /api/teams` - Create team
- `GET /api/teams` - List all teams
- `GET /api/teams/{id}` - Get team by ID
- `GET /api/teams/{id}/players` - Get team squad
- `POST /api/teams/{id}/players` - Add player to team
- `DELETE /api/teams/{teamId}/players/{playerId}` - Remove player

### Match & Scoring
- `POST /api/matches` - Schedule match
- `GET /api/matches` - List all matches
- `GET /api/matches/{id}` - Get match by ID
- `GET /api/matches/{id}/scorecard` - Get live scorecard
- `POST /api/matches/{id}/start` - Start match
- `POST /api/matches/{id}/complete` - Complete match
- `POST /api/matches/{id}/innings/{inningsNumber}/balls` - Add ball data
- `PUT /api/matches/{id}/undo` - Undo last ball

### Statistics & Awards
- `GET /api/statistics/players/{playerId}/batting` - Player batting stats
- `GET /api/statistics/players/{playerId}/bowling` - Player bowling stats
- `GET /api/statistics/leagues/{leagueId}/leaderboards/batting` - Batting leaderboard
- `GET /api/statistics/leagues/{leagueId}/leaderboards/bowling` - Bowling leaderboard
- `GET /api/awards/matches/{matchId}/man-of-the-match` - Man of the Match
- `GET /api/awards/leagues/{leagueId}/orange-cap` - Orange Cap leaderboard
- `GET /api/awards/leagues/{leagueId}/purple-cap` - Purple Cap leaderboard

### Real-time Scoring
- `GET /api/scoring/innings/{inningsId}/current-run-rate` - Current run rate
- `GET /api/scoring/innings/{inningsId}/required-run-rate` - Required run rate
- `GET /api/scoring/innings/{inningsId}/partnership` - Partnership tracking
- `GET /api/scoring/innings/{inningsId}/over-progression` - Over progression
- `GET /api/scoring/matches/{matchId}/statistics` - Match statistics

## 🧪 Testing

### Run Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=LeagueServiceTest

# Run integration tests
mvn test -Dtest=*IntegrationTest
```

### Test Coverage
- **Unit Tests**: Service layer business logic
- **Integration Tests**: Repository layer with TestContainers
- **Controller Tests**: REST endpoints with MockMVC

## 📈 Business Logic

### Scoring System
- **Ball Validation**: Ensures valid cricket scoring rules
- **Over Progression**: Automatic 6-ball over calculation
- **Extras Handling**: Wide, No Ball, Bye, Leg Bye tracking
- **Wicket Types**: All standard cricket dismissal types

### Statistics Calculation
- **Batting Stats**: Runs, Average, Strike Rate, Centuries
- **Bowling Stats**: Wickets, Economy, Average, 5-wicket hauls
- **Team Stats**: Net Run Rate, Win/Loss records
- **Real-time Updates**: Live statistics during matches

### Awards System
- **Man of the Match**: Points system (1pt/run, 20pts/wicket)
- **Orange Cap**: Top run scorer
- **Purple Cap**: Top wicket taker
- **Tournament Awards**: MVP, Best Team awards

## 🔧 Configuration

### Application Properties
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/cricket_league
    username: ${DB_USERNAME:cricket_user}
    password: ${DB_PASSWORD:cricket_pass}
  
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true

server:
  port: 8080

springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html
```

### Environment Variables
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password

## 🚀 Deployment

### Docker Deployment
```bash
# Build Docker image
docker build -t cricket-league-management .

# Run with PostgreSQL
docker-compose up -d
```

### Production Considerations
- Configure proper database connection pooling
- Set up monitoring and logging
- Configure security settings
- Set up backup strategies

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Check the API documentation at `/swagger-ui.html`
- Review the test cases for usage examples

## 🔮 Future Enhancements

- [ ] WebSocket support for real-time updates
- [ ] PDF scorecard generation
- [ ] Mobile app integration
- [ ] Advanced analytics dashboard
- [ ] Multi-language support
- [ ] Video integration for match highlights
