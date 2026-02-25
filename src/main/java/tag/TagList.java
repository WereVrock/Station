package tag;

import java.io.Serializable;
import java.util.*;

class TagList implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Map<String, Tag> tagsByNormalizedName = new HashMap<>();

    private String normalize(String name) {
        if (name == null) return null;

        return name
                .toLowerCase(Locale.ROOT)
                .replaceAll("[\\s/|\\-]", "")
                .trim();
    }

    boolean add(Tag tag) {
        Objects.requireNonNull(tag);

        String key = normalize(tag.getName());

        if (tagsByNormalizedName.containsKey(key)) {
            return false;
        }

        tagsByNormalizedName.put(key, tag);
        return true;
    }

    boolean removeByName(String name) {
        return tagsByNormalizedName.remove(normalize(name)) != null;
    }

    boolean containsByName(String name) {
        return tagsByNormalizedName.containsKey(normalize(name));
    }

    Tag getByName(String name) {
        return tagsByNormalizedName.get(normalize(name));
    }

    boolean removeById(String uniqueId) {
        String key = findKeyById(uniqueId);
        if (key == null) return false;
        tagsByNormalizedName.remove(key);
        return true;
    }

    boolean containsById(String uniqueId) {
        return findKeyById(uniqueId) != null;
    }

    private String findKeyById(String uniqueId) {
        for (Map.Entry<String, Tag> e : tagsByNormalizedName.entrySet()) {
            if (e.getValue().getUniqueId().equals(uniqueId)) {
                return e.getKey();
            }
        }
        return null;
    }

    void update(TagUpdater updater) {
        List<Tag> list = new ArrayList<>(tagsByNormalizedName.values());
        updater.update(list);

        tagsByNormalizedName.clear();

        for (Tag tag : list) {
            tagsByNormalizedName.put(normalize(tag.getName()), tag);
        }
    }

    List<Tag> export() {
        return new ArrayList<>(tagsByNormalizedName.values());
    }

    void importTags(List<Tag> importedTags) {
        tagsByNormalizedName.clear();

        for (Tag tag : importedTags) {
            String key = normalize(tag.getName());

            if (!tagsByNormalizedName.containsKey(key)) {
                tagsByNormalizedName.put(key, tag);
            }
        }
    }

    List<Tag> view() {
        return Collections.unmodifiableList(new ArrayList<>(tagsByNormalizedName.values()));
    }

    void clear() {
        tagsByNormalizedName.clear();
    }

    int size() {
        return tagsByNormalizedName.size();
    }
}
