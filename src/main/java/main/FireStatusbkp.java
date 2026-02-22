package main;

import logic.FireKeyNormalizer;
import java.io.Serializable;
import java.util.Objects;

public class FireStatusbkp implements Serializable {

    public enum Strength {
        STRONG,
        WEAK
    }

    private final Strength strength;
    private final String effect;

    public FireStatusbkp(Strength strength, String effect) {
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
        String normalizedEffect = FireKeyNormalizer.normalize(effect);
        return strength.name().toLowerCase() + "_" + normalizedEffect;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FireStatusbkp)) return false;
        FireStatusbkp that = (FireStatusbkp) o;
        return strength == that.strength &&
                Objects.equals(effect, that.effect);
    }

    @Override
    public int hashCode() {
        return Objects.hash(strength, effect);
    }
}