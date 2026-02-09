package content;

import tag.Tag;

public class TagSpec {

    public String name;
    public Integer days;

    public Tag toTag() {
        if (days == null) {
            return new Tag(name);
        }
        return new Tag(name, days);
    }

    @Override
    public String toString() {
        return "TagSpec{name='" + name + "', duration=" + days + "}";
    }
}