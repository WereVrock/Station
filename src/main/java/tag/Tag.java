package tag;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Tag implements Serializable, Comparable<Tag> {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final String uniqueId;
    private final int expirationDays; // -1 = never expires
    private int ageInDays = 0;

    public Tag(String name) {
        this(name, -1);
    }

    public Tag(String name, int expirationDays) {
        this.name = Objects.requireNonNull(name);
        this.expirationDays = expirationDays;
        this.uniqueId = UUID.randomUUID().toString();
    }

    public String getName() {
        return name;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public int getExpirationDays() {
        return expirationDays;
    }

    public int getAgeInDays() {
        return ageInDays;
    }

    void advanceDay() {
        if (expirationDays >= 0) ageInDays++;
    }

    boolean isExpired() {
        return expirationDays >= 0 && ageInDays >= expirationDays;
    }

    @Override
    public int compareTo(Tag other) {
        return this.name.compareTo(other.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;
        Tag tag = (Tag) o;
        return name.equals(tag.name) && uniqueId.equals(tag.uniqueId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, uniqueId);
    }

    @Override
    public String toString() {
        return "Tag[name=" + name + ", id=" + uniqueId + ", age=" + ageInDays + "]";
    }
}