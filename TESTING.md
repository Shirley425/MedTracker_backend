# Testing

This project has backend tests in `/Users/qiaoqiao/Ada/MedTracker` and frontend tests in `/Users/qiaoqiao/Ada/medtracker_frontend`.

## Backend

Run the full backend suite:

```bash
./mvnw test
```

Run a single backend test class:

```bash
./mvnw -Dtest=AuthControllerWebMvcTest test
```

### Backend test layers

- `@SpringBootTest`
  - [MedTrackerApplicationTests](/Users/qiaoqiao/Ada/MedTracker/src/test/java/com/qt/MedTracker/MedTrackerApplicationTests.java)
  - Keeps one full-context smoke test so the production wiring still boots.

- Service unit tests
  - [UserServiceTest](/Users/qiaoqiao/Ada/MedTracker/src/test/java/com/qt/MedTracker/User/UserServiceTest.java)
  - [MedicationServiceTest](/Users/qiaoqiao/Ada/MedTracker/src/test/java/com/qt/MedTracker/Medication/MedicationServiceTest.java)
  - [MedicationRecordServiceTest](/Users/qiaoqiao/Ada/MedTracker/src/test/java/com/qt/MedTracker/MedicationRecord/MedicationRecordServiceTest.java)
  - Cover business rules such as authentication, normalization, ownership, Slack linking, reminder eligibility, and record defaults.

- `@WebMvcTest`
  - [AuthControllerWebMvcTest](/Users/qiaoqiao/Ada/MedTracker/src/test/java/com/qt/MedTracker/Auth/AuthControllerWebMvcTest.java)
  - [MedicationControllerWebMvcTest](/Users/qiaoqiao/Ada/MedTracker/src/test/java/com/qt/MedTracker/Medication/MedicationControllerWebMvcTest.java)
  - [SlackInteractionControllerWebMvcTest](/Users/qiaoqiao/Ada/MedTracker/src/test/java/com/qt/MedTracker/SlackAPI/SlackInteractionControllerWebMvcTest.java)
  - Focus on request validation, authorization checks, controller response shapes, and unified API error handling without starting the full application.

- `@DataJpaTest`
  - [UserRepositoryDataJpaTest](/Users/qiaoqiao/Ada/MedTracker/src/test/java/com/qt/MedTracker/User/UserRepositoryDataJpaTest.java)
  - [MedicationRepositoryDataJpaTest](/Users/qiaoqiao/Ada/MedTracker/src/test/java/com/qt/MedTracker/Medication/MedicationRepositoryDataJpaTest.java)
  - [VitalSignRepositoryDataJpaTest](/Users/qiaoqiao/Ada/MedTracker/src/test/java/com/qt/MedTracker/VitalSign/VitalSignRepositoryDataJpaTest.java)
  - [MedicationRecordRepositoryDataJpaTest](/Users/qiaoqiao/Ada/MedTracker/src/test/java/com/qt/MedTracker/MedicationRecord/MedicationRecordRepositoryDataJpaTest.java)
  - Verify repository queries and normalized persistence using the in-memory H2 test database.

## Frontend

Run the frontend suite from the frontend workspace:

```bash
cd /Users/qiaoqiao/Ada/medtracker_frontend
npm test -- --watchAll=false
```

### Frontend coverage highlights

- Route protection and auth session handling
  - [ProtectedRoute.test.jsx](/Users/qiaoqiao/Ada/medtracker_frontend/src/ProtectedRoute.test.jsx)
  - [Login.test.jsx](/Users/qiaoqiao/Ada/medtracker_frontend/src/Components/Login.test.jsx)

- Medication tracker interactions
  - [CurrentMedication.test.jsx](/Users/qiaoqiao/Ada/medtracker_frontend/src/Components/CurrentMedication.test.jsx)
  - Covers taken-record creation and Slack member ID linking.

- Forms and normalized data entry
  - [LogVitalSign.test.jsx](/Users/qiaoqiao/Ada/medtracker_frontend/src/Components/LogVitalSign.test.jsx)

- Dashboard and charts
  - [Dashboard.test.jsx](/Users/qiaoqiao/Ada/medtracker_frontend/src/Components/Dashboard.test.jsx)
  - [MedicationChart.test.jsx](/Users/qiaoqiao/Ada/medtracker_frontend/src/Components/MedicationChart.test.jsx)
  - [VitalSignChart.test.jsx](/Users/qiaoqiao/Ada/medtracker_frontend/src/Components/VitalSignChart.test.jsx)
  - Cover module expansion, data loading, Slack action handling, and empty/error states.

## Notes

- Slack App behavior is covered locally at the controller and service boundary, but a real end-to-end Slack callback still depends on external Slack configuration and a public callback URL.
- Backend `@WebMvcTest` slices intentionally mock JWT infrastructure so controller tests stay fast and focused.
