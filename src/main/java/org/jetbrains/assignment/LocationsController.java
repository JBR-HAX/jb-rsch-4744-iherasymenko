package org.jetbrains.assignment;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.sql2o.Query;
import org.sql2o.Sql2o;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public final class LocationsController {

    private final Sql2o db;

    public LocationsController(Sql2o db) {
        this.db = Objects.requireNonNull(db, "db");
    }

    @PostMapping("/locations")
    public List<Point> locations(@RequestBody List<Movement> movements) {
        List<Point> out = new ArrayList<>();
        Point origin = new Point(0,0 );
        out.add(origin);
        for (Movement movement : movements) {
            origin = origin.move(movement);
            out.add(origin);
        }
        logToDatabase(movements, out);
        return out;
    }

    private void logToDatabase(List<Movement> request, List<Point> response) {
        db.runInTransaction((conn, o) -> {
            Integer sessionId = conn.createQuery("insert into sessions values ()")
                    .executeUpdate()
                    .getKey(Integer.class);

            Query insertRequests = conn.createQuery(
                    "insert into locations_requests(session_id, direction, steps) VALUES (:sessionId, :direction, :steps)"
            );
            for (Movement movement : request) {
                insertRequests.addParameter("sessionId", sessionId);
                insertRequests.addParameter("direction", movement.direction());
                insertRequests.addParameter("steps", movement.steps());
                insertRequests.addToBatch();
            }
            insertRequests.executeBatch();

            Query insertResponses = conn.createQuery(
                    "insert into locations_responses(session_id, x, y) VALUES (:sessionId, :x, :y)"
            );
            for (Point point : response) {
                insertResponses.addParameter("sessionId", sessionId);
                insertResponses.addParameter("x", point.x());
                insertResponses.addParameter("y", point.y());
                insertResponses.addToBatch();
            }
            insertResponses.executeBatch();
        });
    }

}
