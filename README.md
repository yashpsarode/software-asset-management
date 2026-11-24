# Software Asset Management Platform

> Built adhering to the **Ponytail — Lazy Senior Developer** philosophy: **YAGNI**, **Zero Unnecessary Dependencies**, **Simplicity > Cleverness**, and **Readable, Lean Code**.

---

## 🎯 Features Implemented

1. **Software License Lifecycle & Expiry Tracking**:
   - Centralized management of software licenses across organizational teams (Engineering, Marketing, IT Operations).
   - Real-time seat allocation & utilization tracking (`totalSeats`, `allocatedSeats`, `availableSeats`).
   - Automated status monitoring (`ACTIVE`, `EXPIRING_SOON` within 30 days, and `EXPIRED`).
   - Daily automated audit scheduler (`@Scheduled`) with manual on-demand trigger endpoints.

2. **Role-Based Access Control (RBAC)**:
   - **`ROLE_EMPLOYEE`**: Submit software requests, view own requests and assigned licenses.
   - **`ROLE_MANAGER`**: Review & approve Level-1 software requests for their respective team, view team licenses.
   - **`ROLE_ADMIN`**: Full administrative governance, Level-2 final approvals, license creation/deletion, team budgeting, and license renewals.

3. **Multi-Level Approval Workflows**:
   - `PENDING_MANAGER_APPROVAL` -> `PENDING_ADMIN_APPROVAL` -> `APPROVED` / `REJECTED`.
   - Team-scoped manager verification to ensure department-level authorization before escalation to IT Admin.
   - Automatic seat allocation upon final IT approval.

4. **Renewal History Management**:
   - Complete audit trail of license renewals with previous vs. new expiry dates, incremental costs, renewing admin user, and justification notes.
   - Cost analytics and renewal forecasting.

---



## 🚀 Getting Started

### Default Test Users (Pre-seeded)
All pre-seeded test accounts use the password: `password123`

| Username | Role | Department / Team |
| :--- | :--- | :--- |
| `admin` | `ROLE_ADMIN` | IT Operations |
| `manager_eng` | `ROLE_MANAGER` | Engineering |
| `dev_john` | `ROLE_EMPLOYEE` | Engineering |
| `sarah_mkt` | `ROLE_EMPLOYEE` | Marketing |

### API Documentation & Swagger UI
- **Swagger UI**: `http://localhost:8082/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8082/v3/api-docs`
- **H2 Console**: `http://localhost:8082/h2-console` (JDBC URL: `jdbc:h2:mem:assetdb`, Username: `sa`, Password: empty)

---

## 📡 Key REST Endpoints

### 1. Authentication (`/api/auth`)
- `POST /api/auth/login`: Authenticate and obtain JWT bearer token.
- `POST /api/auth/register`: Register new employee or manager with team mapping.
- `GET /api/auth/me`: Current user profile and assigned team.

### 2. Software License Management (`/api/licenses`)
- `GET /api/licenses`: Retrieve licenses (filters: `teamId`, `status`, `search`).
- `GET /api/licenses/summary`: Dashboard metrics (total spend, seat utilization, expiry counts).
- `GET /api/licenses/expiring?days=30`: Expiring soon warning list.
- `POST /api/licenses`: Add new software asset *(Admin only)*.
- `PUT /api/licenses/{id}`: Update license parameters *(Admin only)*.
- `DELETE /api/licenses/{id}`: Decommission software license *(Admin only)*.
- `POST /api/licenses/monitor-audit`: Manual trigger for expiry calculation *(Admin only)*.

### 3. Renewal History Management (`/api/renewals`)
- `POST /api/renewals/renew/{licenseId}`: Renew license with new expiry date, cost, and remarks *(Admin/Manager)*.
- `GET /api/renewals/license/{licenseId}`: View complete renewal audit timeline for a license.
- `GET /api/renewals`: Full historical log across all software assets.

### 4. Multi-Level Approval Workflows (`/api/requests`)
- `POST /api/requests`: Employee submits software request with justification.
- `GET /api/requests`: View requests (Employees see own; Managers see team; Admin sees all).
- `GET /api/requests/pending-manager`: Team manager queue.
- `POST /api/requests/manager-review/{requestId}`: Manager approves (advancing to Admin) or rejects.
- `GET /api/requests/pending-admin`: IT admin queue.
- `POST /api/requests/admin-review/{requestId}`: IT admin final approval & optional seat allocation.
