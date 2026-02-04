package logic;

public final class FireKeyNormalizer {

    private FireKeyNormalizer() {}

    public static String normalize(String key) {
        if (key == null) return null;

        String s = key.trim();
        if (s.isEmpty()) return s;

        // Replace separators with underscore
        s = s.replace("-", "_").replace(" ", "_");

        // Convert camelCase or PascalCase -> snake_case
        s = s.replaceAll("([a-z0-9])([A-Z])", "$1_$2");

        // Lowercase everything
        s = s.toLowerCase();

        // Collapse accidental double underscores
        s = s.replaceAll("_+", "_");

        return s;
    }
}