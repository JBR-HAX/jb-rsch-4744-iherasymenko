package org.jetbrains.assignment;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
public final class MovesController {

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
        return out;
    }

}
