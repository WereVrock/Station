package tag;

import java.util.*;

public class TagManagerTest {
    public static void main(String[] args) {
        

        System.out.println("=== Adding Tags ===");
        Tag t1 = new Tag("Alpha");
        Tag t2 = new Tag("Beta", 2); // expires after 2 days
        Tag t3 = new Tag("Gamma");
        TagManager.add(t1);
        TagManager.add(t2);
        TagManager.add(t3);
        System.out.println(TagManager.view());

        System.out.println("\n=== Checking Existence ===");
        System.out.println("Has Alpha? " + TagManager.has(t1.getUniqueId()));
        System.out.println("Has Beta? " + TagManager.has(t2.getUniqueId()));
        System.out.println("Has Random? " + TagManager.has(UUID.randomUUID().toString()));

        System.out.println("\n=== Remove a Tag ===");
        TagManager.remove(t1.getUniqueId());
        System.out.println(TagManager.view());

        System.out.println("\n=== Daily Update / Expiration ===");
        System.out.println("Day 1:");
        TagManager.onNewDay();
        System.out.println(TagManager.view());
        System.out.println("Day 2:");
        TagManager.onNewDay();
        System.out.println(TagManager.view());
        System.out.println("Day 3:");
        TagManager.onNewDay();
        System.out.println(TagManager.view());

        System.out.println("\n=== Export / Import ===");
        List<Tag> exported = TagManager.exportTags();
        System.out.println("Exported: " + exported);
        TagManager.clear();
        TagManager.importTags(exported);
        System.out.println("Imported to new manager: " + TagManager.view());

        System.out.println("\n=== Clear ===");
        TagManager.clear();
        System.out.println("After clear: " + TagManager.view());

        System.out.println("\n=== Size ===");
        System.out.println("Size: " + TagManager.size());
    }
}