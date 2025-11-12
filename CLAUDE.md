# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**기업분석 웹페이지** - A corporate analysis web application for the 임베디드융합개발자 과정 project. This platform allows users to manage and analyze company-related information including financial data, disclosure information, stock prices, and news.

**Target Users**: Business professionals and investors who need to monitor corporate financial information, disclosure data, and stock trends for investment decisions.

## Tech Stack

### Frontend (React 18.x + Vite)
- **Framework**: React 19.2.0 with Vite 7.2.2
- **Routing**: React Router DOM v7
- **HTTP Client**: Axios 1.13.2
- **Charts**: Recharts 3.4.1 (for stock price visualization)
- **Code Quality**: ESLint 9.39.1 + Prettier 3.6.2

### Backend (Spring Boot - Not Yet Implemented)
- Spring Boot 3.x with JDK 17
- Spring Security + JWT authentication
- MyBatis/Maven
- MySQL 8.0.xx database

### External APIs
- **DART**: 전자공시시스템 (Electronic Disclosure System)
- **금융위원회**: 주식시세정보 API (Stock market data)
- **한국수출입은행**: 환율 API (Exchange rate data)
- **Naver**: 검색 API (News search)

## Development Commands

### Frontend (React + Vite)
```bash
cd frontend

# Install dependencies
npm install

# Development server (http://localhost:5173)
npm run dev

# Production build
npm run build

# Preview production build
npm run preview

# Lint check
npm run lint
```

### Backend (Spring Boot - When Implemented)
```bash
cd backend

# Run Spring Boot application
./mvnw spring-boot:run

# Expected to run on http://localhost:8080
```

### Database Setup
```sql
CREATE DATABASE company_analyzer
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
```

## Environment Configuration

### Frontend Environment Variables
Copy `.env.example` to `.env` in the `frontend/` directory:

```bash
cd frontend
cp .env.example .env
```

Required variables:
- `VITE_API_BASE_URL`: Backend API URL (default: http://localhost:8080/api)
- `VITE_DART_API_KEY`: DART API key from https://opendart.fss.or.kr/
- `VITE_NAVER_CLIENT_ID`: Naver API client ID
- `VITE_NAVER_CLIENT_SECRET`: Naver API client secret
- `VITE_ENV`: Environment (development/production)

**Note**: In Vite, access environment variables using `import.meta.env.VITE_*`

## Project Architecture

### Frontend Directory Structure
```
frontend/
├── src/
│   ├── components/     # Reusable UI components (empty - to be implemented)
│   ├── pages/          # Page components (empty - to be implemented)
│   ├── services/       # API call logic (empty - to be implemented)
│   ├── utils/          # Utility functions
│   ├── App.jsx         # Main App component (currently default Vite template)
│   └── main.jsx        # Application entry point
├── public/             # Static files
├── .env                # Environment variables (not committed)
└── .env.example        # Environment variable template
```

### Key Features to Implement

**Authentication System**:
- User registration and login with JWT
- AuthContext for global authentication state
- Axios interceptors for token management

**Board Features** (All using table-based layouts):
1. **경제지표 게시판**: Economic indicators (Exchange rate data)
2. **기업정보 게시판**: Company information (DART API integration)
3. **관심기업 게시판**: Favorite companies list
4. **관심기업 상세페이지**: Detailed company dashboard with tabs:
   - 공시정보 (Disclosure info)
   - 주가차트 (Stock price charts using Recharts Area Chart)
   - 관련기사 (Related news via Naver Search API)
   - 메모 (User notes)

**Stock Chart Features**:
- 금융위원회 주식시세정보 API integration
- Recharts Area Chart for visualization
- Period selection: 30일/60일/90일 (30/60/90 days)

**News Search**:
- Naver Search API integration
- Hashtag-based search functionality

### Backend Architecture (To Be Implemented)
```
backend/
├── src/main/java/
│   ├── controller/     # REST API endpoints
│   ├── service/        # Business logic
│   ├── repository/     # JPA/MyBatis repositories
│   ├── entity/         # Database entities
│   ├── dto/            # Data Transfer Objects
│   ├── config/         # Spring Security, JWT configuration
│   └── exception/      # GlobalExceptionHandler
└── src/main/resources/
    └── application.yml # Configuration
```

### Database Schema (To Be Implemented)
- **Member**: User accounts
- **Company**: Company information
- **Stock**: Favorite companies per user
- **Memo**: User notes for companies

## Design System

**Reference**: Design inspired by 한국수출입은행 (Korea Eximbank) and IBK기업은행 (IBK) websites for professional financial service UI.

**Color Palette**: Professional financial service theme with specific colors for primary, secondary, success, warning, danger, and neutral tones (details in `prd.md` Part 1).

**Layout Components**:
- Header with navigation
- Footer with company info
- Main content area with responsive grid
- Table-based layouts with sticky headers and hover effects
- Tab-based integrated dashboard for detailed views

**Responsive Breakpoints**: Mobile, tablet, and desktop configurations defined in CSS variables.

## Important Implementation Notes

### Current State
- Frontend is **scaffolded but not implemented** - only default Vite + React template exists
- Backend directory **does not exist yet**
- Components, pages, and services directories are **empty**
- Database **not yet created**

### When Implementing Features

**Frontend Development**:
1. Create reusable components in `src/components/` following the design system
2. Implement pages in `src/pages/` with React Router
3. Create API service modules in `src/services/` using Axios
4. Set up AuthContext for global authentication state
5. Configure Axios interceptors for JWT token handling

**Backend Development**:
1. Create Spring Boot project structure in `backend/` directory
2. Implement Spring Security + JWT authentication
3. Create REST API endpoints following the API specification in `prd.md` Part 4
4. Integrate external APIs (DART, 금융위원회, 한국수출입은행, Naver)
5. Set up JPA/MyBatis repositories with MySQL database

**API Integration**:
- All external API keys must be obtained from respective platforms
- Backend should proxy external API calls to avoid exposing keys in frontend
- Implement rate limiting and caching for external API calls

**Security Considerations**:
- JWT tokens for authentication (not session-based)
- API keys must be stored in backend environment variables, never in frontend
- Implement CORS configuration for frontend-backend communication
- Use HTTPS in production

## Documentation

**Primary Documentation**: `prd.md` (150+ pages across 6 parts)
- Part 1: Project overview and UI/UX design guide
- Part 2: Authentication and board features
- Part 3: Stock charts and news search
- Part 4: Database and API specifications (20+ endpoints)
- Part 5: Frontend and backend implementation guide
- Part 6: Deployment, testing, and monitoring

**Frontend README**: `frontend/README.md` contains setup instructions and development guidelines.

## Development Timeline

**Duration**: 10 weeks (임베디드융합개발자 과정 project)

**Key Milestones**:
- Environment setup and API key acquisition
- Database schema implementation
- Authentication system
- Board features (economic indicators, company info, favorites)
- Stock chart integration with 금융위원회 API
- News search with Naver API
- Deployment to Vercel (frontend) and AWS EC2 (backend)

## Code Style

- **ESLint + Prettier**: Configured for consistent code formatting
- **Naming Conventions**:
  - React components: PascalCase (e.g., `Button.jsx`, `CompanyTable.jsx`)
  - Files/directories: camelCase or kebab-case as appropriate
  - API services: Descriptive names (e.g., `getCompanyInfo`, `fetchStockData`)
- **Component Structure**: Functional components with React Hooks
- **State Management**: React Context for global state (AuthContext)

## Git Workflow with GitHub CLI

### GitHub CLI Setup

**Installation**:
- Windows: `winget install --id GitHub.cli`
- macOS: `brew install gh`
- Linux: See https://github.com/cli/cli#installation

**Authentication**:
```bash
# Authenticate with GitHub
gh auth login

# Verify authentication status
gh auth status
```

### Session Start Protocol
Every development session **MUST** start with:
```bash
git status && git branch
gh auth status  # Verify GitHub CLI authentication
```
This ensures you know the current branch, working tree state, and GitHub connection before making any changes.

### Jira-Based Branching Strategy

**Branch Naming Convention**:
```
feature/[JIRA-ID]-[brief-description]
bugfix/[JIRA-ID]-[brief-description]
hotfix/[JIRA-ID]-[brief-description]
```

**Examples**:
- `feature/CORP-123-auth-system`
- `feature/CORP-124-stock-chart`
- `bugfix/CORP-125-login-validation`

### Development Workflow (GitHub CLI)

**1. Start Working on Jira Ticket**:
```bash
# Check current status
git status && git branch

# Create and checkout feature branch
git checkout -b feature/CORP-123-auth-system

# Verify you're on the correct branch
git branch

# Optionally create GitHub issue linked to Jira ticket
gh issue create --title "CORP-123: Implement authentication system" \
  --body "Jira Ticket: CORP-123

  Implement JWT-based authentication with:
  - Login component
  - Registration component
  - AuthContext
  - Axios interceptors"
```

**2. Incremental Development**:
```bash
# Make changes to code

# Review changes before staging
git diff

# Stage specific files
git add src/components/Login.jsx src/services/authService.js

# Create descriptive commit
git commit -m "feat(auth): implement JWT login flow

- Add Login component with form validation
- Create authService with axios interceptors
- Configure JWT token management in AuthContext

🎫 CORP-123"
```

**3. Continue Working** (Multiple commits encouraged):
```bash
# More changes...
git add src/components/Register.jsx

git commit -m "feat(auth): add user registration component

- Implement registration form with validation
- Add password strength indicator
- Connect to backend registration API

🎫 CORP-123"
```

**4. Before Risky Operations** (Create restore points):
```bash
# Commit before major refactoring or risky changes
git add .
git commit -m "chore: checkpoint before auth refactoring

🎫 CORP-123"
```

**5. Push and Create Pull Request**:
```bash
# Push feature branch to remote
git push -u origin feature/CORP-123-auth-system

# Create Pull Request using GitHub CLI
gh pr create --title "feat(auth): Implement JWT authentication system" \
  --body "## Summary
- Implemented Login component with form validation
- Created authService with Axios interceptors
- Set up AuthContext for global auth state
- Added user registration flow

## Jira Ticket
🎫 CORP-123

## Test Plan
- [ ] Login form validation works correctly
- [ ] JWT tokens are stored and refreshed properly
- [ ] Registration flow completes successfully
- [ ] Axios interceptors handle 401 errors

## Screenshots
[Add screenshots if applicable]" \
  --assignee @me

# Or use interactive mode
gh pr create
```

**6. Review and Merge Pull Request**:
```bash
# View PR status
gh pr status

# View PR diff
gh pr diff

# Request review from team members
gh pr review --approve

# Merge PR after approval
gh pr merge --squash --delete-branch

# Or merge with merge commit
gh pr merge --merge --delete-branch

# Update local main branch
git checkout main
git pull origin main
```

### Commit Message Format

Follow conventional commits with Jira ticket reference:

```
<type>(<scope>): <subject>

<body>

🎫 <JIRA-ID>
```

**Types**:
- `feat`: New feature
- `fix`: Bug fix
- `refactor`: Code refactoring
- `style`: Formatting, styling
- `docs`: Documentation changes
- `test`: Adding tests
- `chore`: Maintenance tasks

**Example**:
```
feat(stock): implement stock price chart with Recharts

- Integrate 금융위원회 API for stock data
- Add AreaChart component with responsive design
- Implement period selection (30/60/90 days)
- Add loading states and error handling

🎫 CORP-124
```

### Useful GitHub CLI Commands

**Pull Request Operations**:
```bash
# List all PRs
gh pr list

# View specific PR
gh pr view 123

# Checkout PR locally
gh pr checkout 123

# Review PR
gh pr review 123 --approve
gh pr review 123 --request-changes --body "Please fix..."

# Close PR without merging
gh pr close 123

# Reopen PR
gh pr reopen 123
```

**Issue Operations**:
```bash
# List issues
gh issue list

# Create issue
gh issue create --title "Bug: Login fails" --body "Description..."

# View issue
gh issue view 456

# Close issue
gh issue close 456

# Link issue to PR
gh pr create --body "Fixes #456"
```

**Repository Operations**:
```bash
# View repository info
gh repo view

# Clone repository
gh repo clone owner/repo

# Fork repository
gh repo fork

# Browse repository in browser
gh browse
```

**Workflow/Actions**:
```bash
# View workflow runs
gh run list

# View specific run
gh run view 789

# Watch run in real-time
gh run watch 789
```

### Git Safety Rules

**NEVER**:
- ❌ Work directly on `main` branch
- ❌ Force push (`git push --force`) to main
- ❌ Commit sensitive data (.env files, API keys)
- ❌ Skip `git diff` review before committing
- ❌ Use vague commit messages ("fix", "update", "changes")
- ❌ Merge PRs without review (except solo projects)

**ALWAYS**:
- ✅ Create feature branches for all work
- ✅ Run `git status && git branch && gh auth status` at session start
- ✅ Review changes with `git diff` before staging
- ✅ Write descriptive commit messages with Jira ticket ID
- ✅ Commit incrementally (not giant commits)
- ✅ Create restore points before risky operations
- ✅ Use `gh pr create` for Pull Requests
- ✅ Delete branches after merge using `--delete-branch` flag

### Branch Cleanup

After successful merge via GitHub CLI:
```bash
# Branches are auto-deleted with --delete-branch flag
gh pr merge --squash --delete-branch

# Update local repository
git checkout main
git pull origin main

# Verify local branches
git branch -a

# Manually delete local branch if needed
git branch -d feature/CORP-123-auth-system

# Prune remote-tracking branches
git remote prune origin
```

### Recovery and Rollback

**Undo last commit (keep changes)**:
```bash
git reset --soft HEAD~1
```

**Discard uncommitted changes**:
```bash
git restore <file>  # Specific file
git restore .       # All files
```

**View commit history**:
```bash
git log --oneline --graph --all
gh pr list --state all  # View all PR history
```

**Revert merged PR**:
```bash
# Find the PR number
gh pr list --state merged

# Create revert PR
gh pr view 123 --json mergeCommit --jq .mergeCommit.oid | \
  xargs -I {} git revert {} --no-commit
git commit -m "revert: undo PR #123"
gh pr create --title "revert: Undo PR #123"
```

### Working Solo vs Team

**Solo Development** (Current):
```bash
# Simplified workflow - merge directly if needed
git checkout main
git merge feature/CORP-123-auth-system --no-ff
git push origin main
git branch -d feature/CORP-123-auth-system
```

**Team Collaboration** (Future):
```bash
# Always use PR workflow
gh pr create
# Wait for review
gh pr merge --squash --delete-branch
```

## Testing Strategy (To Be Implemented)

- **Frontend**: Unit tests for components, integration tests for API calls
- **Backend**: Unit tests for services, integration tests for REST endpoints
- **E2E**: User journey testing for critical flows
