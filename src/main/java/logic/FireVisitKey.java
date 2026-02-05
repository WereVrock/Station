package logic;

import main.FireStatus;

public final class FireVisitKey {

    private final FireStatus.Strength strength;
    private final String effect;

    private FireVisitKey(FireStatus.Strength strength, String effect) {
        this.strength = strength;
        this.effect = FireKeyNormalizer.normalize(effect);
    }

    public static FireVisitKey from(FireStatus status) {
        return new FireVisitKey(status.getStrength(), status.getEffect());
    }

    public FireStatus.Strength getStrength() {
        return strength;
    }

    public String getEffect() {
        return effect;
    }

    // This is the ONLY string the legacy system sees
    public String legacyKey() {
        return strength.name().toLowerCase() + "_" + effect;
    }

    // helper if you want tags later
    public String strengthKey() {
        return strength.name().toLowerCase();
    }

    public String effectKey() {
        return effect;
    }

    @Override
    public String toString() {
        return legacyKey();
    }
}
