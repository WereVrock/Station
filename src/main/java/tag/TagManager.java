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

    private static final TagManager INSTANCE = new TagManager();

    private final TagList tagList = new TagList();
    private final TagUpdater updater = new TagUpdater();

    private TagManager() {
    }

    // ===== NAME-DRIVEN PRIMARY API =====

    public static boolean add(Tag tag) {
        return INSTANCE.tagList.add(tag);
    }

    public static boolean removeByName(String name) {
        return INSTANCE.tagList.removeByName(name);
    }

    public static boolean hasByName(String name) {
        return INSTANCE.tagList.containsByName(name);
    }

    public static Tag getByName(String name) {
        return INSTANCE.tagList.getByName(name);
    }

    // ===== COMPATIBILITY (ID-BASED) =====

    public static boolean remove(String uniqueId) {
        return INSTANCE.tagList.removeById(uniqueId);
    }

    public static boolean has(String uniqueId) {
        return INSTANCE.tagList.containsById(uniqueId);
    }

    // ===== SYSTEM =====

    public static void onNewDay() {
        INSTANCE.tagList.update(INSTANCE.updater);
    }

    public static List<Tag> exportTags() {
        return INSTANCE.tagList.export();
    }

    public static void importTags(List<Tag> importedTags) {
        INSTANCE.tagList.importTags(importedTags);
    }

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