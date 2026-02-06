package tag;

import java.io.Serializable;
import java.util.*;

/**
 * Internal helper for storing and manipulating tags.
 */
class TagList implements Serializable {
    private static final long serialVersionUID = 1L;
    private final List<Tag> tags = new ArrayList<>();

    boolean add(Tag tag) {
        return tags.add(tag);
    }

    boolean remove(String uniqueId) {
        return tags.removeIf(t -> t.getUniqueId().equals(uniqueId));
    }

    boolean contains(String uniqueId) {
        return tags.stream().anyMatch(t -> t.getUniqueId().equals(uniqueId));
    }

    void update(TagUpdater updater) {
        updater.update(tags);
    }

    List<Tag> export() {
        return new ArrayList<>(tags);
    }

    void importTags(List<Tag> importedTags) {
        tags.clear();
        tags.addAll(importedTags);
    }

    List<Tag> view() {
        return Collections.unmodifiableList(tags);
    }

    void clear() {
        tags.clear();
    }

    int size() {
        return tags.size();
    }
}