package tag;

import java.util.List;
import java.util.UUID;

public class TagManagerTest2 {

    public static void main(String[] args) {

        System.out.println("=== Add Tags (Name Identity) ===");
        Tag alpha = new Tag("Alpha");
        Tag beta = new Tag("Beta", 2);
        Tag gamma = new Tag("Gamma");

        System.out.println("Add Alpha: " + TagManager.add(alpha));
        System.out.println("Add Beta: " + TagManager.add(beta));
        System.out.println("Add Gamma: " + TagManager.add(gamma));
        System.out.println("Current: " + TagManager.view());

        System.out.println("\n=== Reject Duplicate Name ===");
        Tag alphaDuplicate = new Tag("Alpha");
        System.out.println("Add Alpha again: " + TagManager.add(alphaDuplicate));
        System.out.println("Current: " + TagManager.view());

        System.out.println("\n=== Name-Based Queries ===");
        System.out.println("Has Alpha? " + TagManager.hasByName("Alpha"));
        System.out.println("Has Beta? " + TagManager.hasByName("Beta"));
        System.out.println("Has Missing? " + TagManager.hasByName("Missing"));

        System.out.println("\n=== Get By Name ===");
        System.out.println("Get Alpha: " + TagManager.getByName("Alpha"));
        System.out.println("Get Missing: " + TagManager.getByName("Missing"));

        System.out.println("\n=== Remove By Name ===");
        System.out.println("Remove Alpha: " + TagManager.removeByName("Alpha"));
        System.out.println("After remove: " + TagManager.view());

        System.out.println("\n=== ID Compatibility ===");
        String betaId = beta.getUniqueId();
        System.out.println("Has Beta by ID? " + TagManager.has(betaId));
        System.out.println("Remove Beta by ID: " + TagManager.remove(betaId));
        System.out.println("After remove: " + TagManager.view());

        System.out.println("\n=== Random ID (Should Fail) ===");
        System.out.println("Remove random ID: " + TagManager.remove(UUID.randomUUID().toString()));

        System.out.println("\n=== Expiration Test ===");
        Tag expiring = new Tag("Temp", 2);
        TagManager.add(expiring);
        System.out.println("Day 0: " + TagManager.view());

        System.out.println("Day 1");
        TagManager.onNewDay();
        System.out.println(TagManager.view());

        System.out.println("Day 2");
        TagManager.onNewDay();
        System.out.println(TagManager.view());

        System.out.println("Day 3");
        TagManager.onNewDay();
        System.out.println(TagManager.view());

        System.out.println("\n=== Export / Import ===");
        TagManager.add(new Tag("PersistA"));
        TagManager.add(new Tag("PersistB"));
        List<Tag> exported = TagManager.exportTags();
        System.out.println("Exported: " + exported);

        TagManager.clear();
        System.out.println("After clear: " + TagManager.view());

        TagManager.importTags(exported);
        System.out.println("After import: " + TagManager.view());

        System.out.println("\n=== Size ===");
        System.out.println("Size: " + TagManager.size());

        System.out.println("\n=== Final Clear ===");
        TagManager.clear();
        System.out.println("Final state: " + TagManager.view());
    }
}