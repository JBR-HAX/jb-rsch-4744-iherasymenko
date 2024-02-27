package org.jetbrains.assignment;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public final class LocationsController {

    @PostMapping("/locations")
    public List<Point> locations(@RequestBody List<Movement> movements) {
        List<Point> out = new ArrayList<>();
        Point origin = new Point(0,0 );
        out.add(origin);
        for (Movement movement : movements) {
            origin = origin.move(movement);
            out.add(origin);
        }
        return out;
    }

}
