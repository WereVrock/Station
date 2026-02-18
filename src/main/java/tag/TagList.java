package tag;

import java.io.Serializable;
import java.util.*;

/**
 * Internal helper for storing and manipulating tags.
 * Name is the true identity of a Tag.
 * Duplicate names are not allowed.
 */
class TagList implements Serializable {
    private static final long serialVersionUID = 1L;

    // Name is identity → use map for correctness + performance
    private final Map<String, Tag> tagsByName = new HashMap<>();

    boolean add(Tag tag) {
        Objects.requireNonNull(tag);

        if (tagsByName.containsKey(tag.getName())) {
            return false; // duplicate name not allowed
        }

        tagsByName.put(tag.getName(), tag);
        return true;
    }

    // ===== NAME-BASED =====

    boolean removeByName(String name) {
        return tagsByName.remove(name) != null;
    }

    boolean containsByName(String name) {
        return tagsByName.containsKey(name);
    }

    Tag getByName(String name) {
        return tagsByName.get(name);
    }

    // ===== ID-BASED (COMPATIBILITY ONLY) =====

    boolean removeById(String uniqueId) {
        String key = findNameById(uniqueId);
        if (key == null) return false;
        tagsByName.remove(key);
        return true;
    }

    boolean containsById(String uniqueId) {
        return findNameById(uniqueId) != null;
    }

    private String findNameById(String uniqueId) {
        for (Tag tag : tagsByName.values()) {
            if (tag.getUniqueId().equals(uniqueId)) {
                return tag.getName();
            }
        }
        return null;
    }

    // ===== SYSTEM =====

    void update(TagUpdater updater) {
        List<Tag> list = new ArrayList<>(tagsByName.values());
        updater.update(list);

        // rebuild map in case expiration removed items
        tagsByName.clear();
        for (Tag tag : list) {
            tagsByName.put(tag.getName(), tag);
        }
    }

    List<Tag> export() {
        return new ArrayList<>(tagsByName.values());
    }

    void importTags(List<Tag> importedTags) {
        tagsByName.clear();

        for (Tag tag : importedTags) {
            // Enforce uniqueness even during import
            if (!tagsByName.containsKey(tag.getName())) {
                tagsByName.put(tag.getName(), tag);
            }
        }
    }

    List<Tag> view() {
        return Collections.unmodifiableList(new ArrayList<>(tagsByName.values()));
    }

    void clear() {
        tagsByName.clear();
    }

    int size() {
        return tagsByName.size();
    }
}