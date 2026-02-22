package logic;

import main.FireStatus;
import main.Main;

import java.util.Collection;
import java.util.Objects;

public final class FireStatusMatcher {

    private FireStatusMatcher() {}

    /* =========================================================
       PUBLIC ENTRY USING CURRENT GAME STATUS
       ========================================================= */

    public static boolean matches(String rawInput) {
        return matches(getCurrentStatus(), rawInput);
    }

    public static boolean matches(FireStatus.StrengthEnum strength) {
        return matches(getCurrentStatus(), strength);
    }

    public static boolean matches(FireStatus.StrengthEnum strength, String effect) {
        return matches(getCurrentStatus(), strength, effect);
    }

    public static boolean matchesAny(Collection<String> inputs) {
        return matchesAny(getCurrentStatus(), inputs);
    }

    public static boolean matchesAnyEnums(Collection<FireStatus.StrengthEnum> strengths) {
        return matchesAnyEnums(getCurrentStatus(), strengths);
    }

    /* =========================================================
       EXPLICIT STATUS ENTRY
       ========================================================= */

    public static boolean matches(FireStatus status, String rawInput) {
        if (status == null || rawInput == null || rawInput.isBlank()) {
            return false;
        }

        String normalized = FireKeyNormalizer.normalize(rawInput);
        String[] parts = normalized.split("_");

        if (parts.length == 0) {
            return false;
        }

        if (parts.length == 1) {
            return matchSingleToken(status, parts[0]);
        }

        if (isStrength(parts[0])) {
            String strength = parts[0];
            String effect = joinRemaining(parts);
            return matchStrength(status, strength)
                    && matchEffect(status, effect);
        }

        return matchEffect(status, normalized);
    }

    public static boolean matches(FireStatus status, FireStatus.StrengthEnum strength) {
        if (status == null || strength == null) {
            return false;
        }
        return status.isStrength(strength);
    }

    public static boolean matches(FireStatus status,
                                  FireStatus.StrengthEnum strength,
                                  String effect) {

        if (status == null) {
            return false;
        }

        boolean strengthMatches = true;
        boolean effectMatches = true;

        if (strength != null) {
            strengthMatches = status.isStrength(strength);
        }

        if (effect != null && !effect.isBlank()) {
            effectMatches = matchEffect(status, effect);
        }

        return strengthMatches && effectMatches;
    }

    public static boolean matchesAny(FireStatus status, Collection<String> inputs) {

        if (status == null) {
            return false;
        }

        if (inputs == null || inputs.isEmpty()) {
            return true;
        }

        for (String input : inputs) {
            if (matches(status, input)) {
                return true;
            }
        }

        return false;
    }

    public static boolean matchesAnyEnums(FireStatus status,
                                          Collection<FireStatus.StrengthEnum> strengths) {

        if (status == null) {
            return false;
        }

        if (strengths == null || strengths.isEmpty()) {
            return true;
        }

        for (FireStatus.StrengthEnum strength : strengths) {
            if (matches(status, strength)) {
                return true;
            }
        }

        return false;
    }

    /* =========================================================
       MATCH LOGIC
       ========================================================= */

    private static boolean matchSingleToken(FireStatus status, String token) {
        if (isStrength(token)) {
            return matchStrength(status, token);
        }
        return matchEffect(status, token);
    }

    private static boolean matchStrength(FireStatus status, String strength) {
        return Objects.equals(status.getStrength(), normalizeStrength(strength));
    }

    private static boolean matchEffect(FireStatus status, String effect) {
        String normalizedInput = FireKeyNormalizer.normalize(effect);
        String normalizedStatus = FireKeyNormalizer.normalize(status.getEffect());
        return Objects.equals(normalizedStatus, normalizedInput);
    }

    /* =========================================================
       HELPERS
       ========================================================= */

    private static FireStatus getCurrentStatus() {
        if (Main.game == null) {
            return null;
        }
        return Main.game.getFireStatus();
    }

    private static boolean isStrength(String value) {
        if (value == null) return false;
        String v = value.trim().toLowerCase();
        return v.equals("weak") || v.equals("strong");
    }

    private static String normalizeStrength(String value) {
        String v = value.trim().toLowerCase();
        return v.equals("strong") ? "strong" : "weak";
    }

    private static String joinRemaining(String[] parts) {
        if (parts.length <= 1) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < parts.length; i++) {
            if (i > 1) {
                sb.append("_");
            }
            sb.append(parts[i]);
        }
        return sb.toString();
    }
}