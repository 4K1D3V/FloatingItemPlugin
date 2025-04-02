package gg.kite.core.entity;

public sealed interface AnimationPattern {
    double apply(double time);

    record Sine() implements AnimationPattern {
        @Override public double apply(double time) { return Math.sin(time); }
    }

    record Bounce() implements AnimationPattern {
        @Override public double apply(double time) { return Math.abs(Math.sin(time)); }
    }

    record Linear() implements AnimationPattern {
        @Override public double apply(double time) { return time % 1.0; }
    }

    static AnimationPattern fromString(String name) {
        return switch (name.toUpperCase()) {
            case "BOUNCE" -> new Bounce();
            case "LINEAR" -> new Linear();
            default -> new Sine();
        };
    }
}