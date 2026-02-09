package logic.content;

import tag.Tag;

public class TagSpec {

    public String name;
    public Integer duration;

    public Tag toTag() {
        if (duration == null) {
            return new Tag(name);
        }
        return new Tag(name, duration);
    }

    @Override
    public String toString() {
        return "TagSpec{name='" + name + "', duration=" + duration + "}";
    }
}