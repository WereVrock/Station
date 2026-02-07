package tag;

import java.io.Serializable;
import java.util.*;

/**
 * Public interface for all tag operations.
 * Internally uses TagList and TagUpdater for modularity.
 */
public class TagManager implements Serializable {
    private static final long serialVersionUID = 1L;

    private final TagList tagList = new TagList();
    private final TagUpdater updater = new TagUpdater();

    public boolean add(Tag tag) {
        return tagList.add(tag);
    }

    public boolean remove(String uniqueId) {
        return tagList.remove(uniqueId);
    }

    public boolean has(String uniqueId) {
        return tagList.contains(uniqueId);
    }

    public void onNewDay() {
        tagList.update(updater);
    }

    public List<Tag> exportTags() {
        return tagList.export();
    }

    public void importTags(List<Tag> importedTags) {
        tagList.importTags(importedTags);
    }

    public List<Tag> view() {
        return tagList.view();
    }

    public int size() {
        return tagList.size();
    }

    public void clear() {
        tagList.clear();
    }

    @Override
    public String toString() {
        return "TagManager{tags=" + tagList.view() + "}";
    }

    public boolean hasTag(String tagID) {
       return tagList.contains(tagID);  }
}