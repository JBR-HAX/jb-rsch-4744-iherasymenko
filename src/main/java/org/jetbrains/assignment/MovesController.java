package org.jetbrains.assignment;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.sql2o.Query;
import org.sql2o.Sql2o;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@RestController
public final class MovesController {

    private final Sql2o db;

    public MovesController(Sql2o db) {
        this.db = Objects.requireNonNull(db, "db");
    }

    @PostMapping("/moves")
    public List<Movement> moves(@RequestBody List<Point> points) {
        if (points.size() < 2) {
            throw new IllegalArgumentException("points.size() should be > 1, given: " + points.size());
        }
        List<Movement> out = new ArrayList<>();
        Iterator<Point> iter = points.iterator();
        Point origin = iter.next();
        while (iter.hasNext()) {
            Point point = iter.next();
            out.add(origin.toMovement(point));
            origin = point;
        }
        logToDatabase(points, out);
        return out;
    }

    private void logToDatabase(List<Point> request, List<Movement> response) {
        db.runInTransaction((conn, o) -> {
            Integer sessionId = conn.createQuery("insert into sessions values ()")
                    .executeUpdate()
                    .getKey(Integer.class);

            Query insertRequest = conn.createQuery(
                    "insert into MOVES_REQUESTS(session_id, x, y) VALUES (:sessionId, :x, :y)"
            );
            for (Point point : request) {
                insertRequest.addParameter("sessionId", sessionId);
                insertRequest.addParameter("x", point.x());
                insertRequest.addParameter("y", point.y());
                insertRequest.addToBatch();
            }
            insertRequest.executeBatch();

            Query insertRespones = conn.createQuery(
                    "insert into MOVES_RESPONSES(session_id, direction, steps) VALUES (:sessionId, :direction, :steps)"
            );
            for (Movement movement : response) {
                insertRespones.addParameter("sessionId", sessionId);
                insertRespones.addParameter("direction", movement.direction());
                insertRespones.addParameter("steps", movement.steps());
                insertRespones.addToBatch();
            }
            insertRespones.executeBatch();
        });
    }

}
