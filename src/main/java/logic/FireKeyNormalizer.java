package logic;

public final class FireKeyNormalizer {

    private FireKeyNormalizer() {}

    public static String normalize(String key) {
        if (key == null) return null;

        String trimmed = key.trim();
        if (trimmed.isEmpty()) return trimmed;

        // convert camelCase strongClean -> strong_clean
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < trimmed.length(); i++) {
            char c = trimmed.charAt(i);

            if (Character.isUpperCase(c)) {
                sb.append("_").append(Character.toLowerCase(c));
            } else {
                sb.append(Character.toLowerCase(c));
            }
        }

        return sb.toString();
    }
}