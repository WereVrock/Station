package tag;

import java.util.*;

/**
 * Global static access point for all tag operations.
 * Backed by a single internal TagManager instance.
 *
 * Note:
 * - This class itself is NOT serialized.
 * - Persistence is handled via exportTags() / importTags().
 */
public final class TagManager {

    // ===== Single Internal Instance =====
    private static final TagManager INSTANCE = new TagManager();

    // ===== Internal State =====
    private final TagList tagList = new TagList();

    /**
     * Stateless helper responsible for day-based tag updates
     * (e.g. expiration countdown).
     */
    private final TagUpdater updater = new TagUpdater();

    // ===== Constructor =====
    private TagManager() {
        // no external instantiation
    }

    // ===== Static API =====

    public static boolean add(Tag tag) {
        return INSTANCE.tagList.add(tag);
    }

    public static boolean remove(String uniqueId) {
        return INSTANCE.tagList.remove(uniqueId);
    }

    public static boolean has(String uniqueId) {
        return INSTANCE.tagList.contains(uniqueId);
    }

    /**
     * Advances time-dependent tag logic by one day.
     * Should be called exactly once per game day.
     */
    public static void onNewDay() {
        INSTANCE.tagList.update(INSTANCE.updater);
    }

    /**
     * Returns a copy of all tags for persistence.
     */
    public static List<Tag> exportTags() {
        return INSTANCE.tagList.export();
    }

    /**
     * Replaces all current tags with imported ones.
     */
    public static void importTags(List<Tag> importedTags) {
        INSTANCE.tagList.importTags(importedTags);
    }

    /**
     * Read-only view of current tags.
     */
    public static List<Tag> view() {
        return INSTANCE.tagList.view();
    }

    public static int size() {
        return INSTANCE.tagList.size();
    }

    public static void clear() {
        INSTANCE.tagList.clear();
    }

    @Override
    public String toString() {
        return "TagManager{tags=" + tagList.view() + "}";
    }
}