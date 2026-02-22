package main;

import logic.FireKeyNormalizer;
import java.io.Serializable;
import java.util.Objects;

public class FireStatus implements Serializable {

    // strength is now a String, not enum
    private String strength; // expected values: "weak" or "strong"
    private final String effect;

    public enum StrengthEnum {
        WEAK,
        STRONG
    }

    public FireStatus(StrengthEnum strength, String effect) {
        this(strength.toString(), effect);
    }

    public FireStatus(String strength, String effect) {
        this.strength = normalizeStrength(strength);
        this.effect = (effect == null || effect.isBlank()) ? "clean" : effect;
    }

    public void setStrength(StrengthEnum strength) {
        this.strength = strength.name().toLowerCase();
    }

    public boolean isStrength(StrengthEnum strength) {
        return this.strength.equals(strength.name().toLowerCase());
    }


    /* ---------------- strength setters ---------------- */
    public void setStrenghtToWeak() {
        this.strength = "weak";
    }

    public void setStrengthTosTrong() {
        this.strength = "strong";
    }

    public void SetStrenght(String strength) {
        this.strength = normalizeStrength(strength);
    }

    /* ---------------- strength checks ---------------- */
    public boolean ISstrenghtweak() {
        return "weak".equals(strength);
    }

    public boolean isstrengthstrong() {
        return "strong".equals(strength);
    }

    public boolean checkStrenght(String strength) {
        return this.strength.equals(normalizeStrength(strength));
    }

    /* ---------------- getters ---------------- */
    public String getStrength() {
        return strength;
    }

    public String getEffect() {
        return effect;
    }

    public boolean isClean() {
        return "clean".equalsIgnoreCase(effect);
    }

    /* ---------------- utils ---------------- */
    private String normalizeStrength(String value) {
        if (value == null) {
            return "weak";
        }
        String normalized = value.trim().toLowerCase();
        return normalized.equals("strong") ? "strong" : "weak";
    }

    @Override
    public String toString() {
        String normalizedEffect = FireKeyNormalizer.normalize(effect);
        return strength + "_" + normalizedEffect;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FireStatus)) {
            return false;
        }
        FireStatus that = (FireStatus) o;
        return Objects.equals(strength, that.strength)
                && Objects.equals(effect, that.effect);
    }

    @Override
    public int hashCode() {
        return Objects.hash(strength, effect);
    }

}
