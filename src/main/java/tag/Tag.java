package tag;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Tag implements Serializable, Comparable<Tag> {
    private static final long serialVersionUID = 1L;
    public static boolean showIDOnToString=false;

    private final String name;
    private final String uniqueId;
    private final int expirationDays; // -1 = never expires
    private int ageInDays;

    public Tag(String name) {
        this(name, -1);
    }

    public Tag(String name, int expirationDays) {
        this.name = Objects.requireNonNull(name);
        this.expirationDays = expirationDays;
        this.uniqueId = UUID.randomUUID().toString();

        // Permanent tags are timeless
        this.ageInDays = (expirationDays < 0) ? -1 : 0;
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
        if (expirationDays >= 0) {
            ageInDays++;
        }
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
        String ageStr;
        if(ageInDays==-1)ageStr="non-expiring";
        else ageStr = ageInDays+"";
        String idStr="";
        if(showIDOnToString)idStr=" id=" + uniqueId;
        return "Tag[name=" + name +  ", age=" + ageStr + idStr+"]";
    }
}
