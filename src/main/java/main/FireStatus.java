package main;

import java.io.Serializable;
import java.util.Objects;

public class FireStatus implements Serializable {

    public enum Strength {
        STRONG,
        WEAK
    }

    private final Strength strength;
    private final String effect; // "clean" or special item string

    public FireStatus(Strength strength, String effect) {
        this.strength = strength;
        this.effect = (effect == null || effect.isBlank()) ? "clean" : effect;
    }

    public Strength getStrength() {
        return strength;
    }

    public String getEffect() {
        return effect;
    }

    public boolean isClean() {
        return "clean".equalsIgnoreCase(effect);
    }

    @Override
    public String toString() {
        return strength.name().toLowerCase() + "_" + effect;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FireStatus)) return false;
        FireStatus that = (FireStatus) o;
        return strength == that.strength &&
                Objects.equals(effect, that.effect);
    }

    @Override
    public int hashCode() {
        return Objects.hash(strength, effect);
    }
}