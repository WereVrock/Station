package tag;

import java.util.*;

public class TagManagerTest {
    public static void main(String[] args) {
        TagManager manager = new TagManager();

        System.out.println("=== Adding Tags ===");
        Tag t1 = new Tag("Alpha");
        Tag t2 = new Tag("Beta", 2); // expires after 2 days
        Tag t3 = new Tag("Gamma");
        manager.add(t1);
        manager.add(t2);
        manager.add(t3);
        System.out.println(manager.view());

        System.out.println("\n=== Checking Existence ===");
        System.out.println("Has Alpha? " + manager.has(t1.getUniqueId()));
        System.out.println("Has Beta? " + manager.has(t2.getUniqueId()));
        System.out.println("Has Random? " + manager.has(UUID.randomUUID().toString()));

        System.out.println("\n=== Remove a Tag ===");
        manager.remove(t1.getUniqueId());
        System.out.println(manager.view());

        System.out.println("\n=== Daily Update / Expiration ===");
        System.out.println("Day 1:");
        manager.onNewDay();
        System.out.println(manager.view());
        System.out.println("Day 2:");
        manager.onNewDay();
        System.out.println(manager.view());
        System.out.println("Day 3:");
        manager.onNewDay();
        System.out.println(manager.view());

        System.out.println("\n=== Export / Import ===");
        List<Tag> exported = manager.exportTags();
        System.out.println("Exported: " + exported);
        TagManager newManager = new TagManager();
        newManager.importTags(exported);
        System.out.println("Imported to new manager: " + newManager.view());

        System.out.println("\n=== Clear ===");
        newManager.clear();
        System.out.println("After clear: " + newManager.view());

        System.out.println("\n=== Size ===");
        System.out.println("Size: " + manager.size());
    }
}