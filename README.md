# Booking Api

## How to use?

### Running the application 

1. Start the application by running: "./gradlew bootRun"
2. Open in your browser http://localhost:8080/h2-console/
3. Look for this log: "H2 console available at '/h2-console'. Database available at...."
4. Fill in h2 console JDBC URL with the url given by the log and log in.
5. Have a happy usage using the following curls below.

curl -X POST \
-H "Content-Type: application/json" \
-d '{ "searchType": "Availability", "eatersIds": ["2"], "startTime": "2024-10-20T18:00:00" }' \
http://localhost:8080/v1/restaurant/search

curl -X POST \
-H "Content-Type: application/json" \
-d '{ "restaurantId": "1", "eaters": ["2"], "time": "2024-10-20 18:00:00" }' \
http://localhost:8080/v1/reservation

curl -X DELETE "http://localhost:8080/v1/reservation/1" \
-H "userId: 2"

### Developing and making changes

Rename the following resources to make the test schema and data effective for integration tests.
- scr/main/resources/schema.sql --> scr/main/resources/schema_off.sql
- scr/test/resources/schema_off.sql --> scr/test/resources/schema.sql

Run "./gradlew test"

There is currently a failing test: 
- RestaurantControllersSmokeTest > testSearchRestaurants_MissingProperty() FAILED


## Technical stack and design
- 
- Java, Spring boot, and relational database.
- Hexagonal design. More info: https://alistair.cockburn.us/hexagonal-architecture/.
- Spring controllers. Ex: https://www.baeldung.com/spring-controllers.
- Raw repositories with JdbcTemplate. Ex: https://spring.io/guides/gs/managing-transactions.
- Decoupled domain from spring. Except @Component for injections and @Transactional for transactions.
- H2 memory database for integration and smoke tests.

## Some pending tasks

- Fix format at /restaurant/search 2024-10-20T18:00:00.
- Standardize test case names inside content to follow setup, exercise and verify clearly separated.
- Implement test for CreateReservation.java and DeleteReservation.java (quite important as it's the domain).
- Implement correct handling invalid requests when properties are missing or have incorrect types.
- Add descriptive messages in 4xx responses.
- Move some domain logic in use cases to entities, entities at the moment are anemic.
- Better error mapping at controllers, currently the exception name it used as a simplification.
- Use optional instead of lists in repositories responses.
- Use uuid for ids of all entities and remove auto-incremental ids.
- Add indexes at the database to improve query performance.
