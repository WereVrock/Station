package tag;

import java.util.Iterator;
import java.util.List;

/**
 * Internal helper for daily tag expiration updates.
 */
class TagUpdater {
    void update(List<Tag> tags) {
        Iterator<Tag> it = tags.iterator();
        while (it.hasNext()) {
            Tag t = it.next();
            t.advanceDay();
            if (t.isExpired()) it.remove();
        }
    }
}