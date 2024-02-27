package org.jetbrains.assignment;

public record Point(int x, int y) {
    public Point move(Movement movement) {
        int steps = movement.steps();
        return switch (movement.direction()) {
            case EAST -> new Point(this.x + steps, this.y);
            case WEST -> new Point(this.x - steps, this.y);
            case NORTH -> new Point(this.x, y + steps);
            case SOUTH -> new Point(this.x, y - steps);
        };
    }

    public Movement toMovement(Point that) {
        if (this.equals(that)) {
            throw new IllegalArgumentException("No movement needed. this: %s, that: %s".formatted(this, that));
        }
        if (this.x == that.x) {
            int diff = this.y - that.y;
            return new Movement(
                    diff < 0 ? Direction.NORTH : Direction.SOUTH,
                    Math.abs(diff)
            );
        }

        if (this.y == that.y) {
            int diff = this.x - that.x;
            return new Movement(
                    diff < 0 ? Direction.EAST : Direction.WEST,
                    Math.abs(diff)
            );
        }
        throw new IllegalArgumentException("One transformation at a time. this: %s, that: %s".formatted(this, that));
    }
}
