package tag;

import java.util.List;
import java.util.UUID;

public class TagManagerTestNormalized {

    public static void main(String[] args) {
        System.out.println("=== Add Tags ===");
        Tag t1 = new Tag("Alpha-Beta");
        Tag t2 = new Tag("Gamma/Delta", 2); // expires in 2 days
        Tag t3 = new Tag("Epsilon | Zeta");
        Tag t4 = new Tag("alpha beta"); // should collide with t1
        TagManager.add(t1);
        TagManager.add(t2);
        TagManager.add(t3);

        System.out.println("Initial Tags: " + TagManager.view());

        System.out.println("\n=== Test Duplicate Normalization ===");
        boolean added = TagManager.add(t4);
        System.out.println("Adding 'alpha beta' again (normalized collision with 'Alpha-Beta') -> " + added);
        System.out.println("Tags after duplicate attempt: " + TagManager.view());

        System.out.println("\n=== Lookup Normalized Names ===");
        System.out.println("Has 'alpha-beta'? " + TagManager.hasByName("alpha-beta"));
        System.out.println("Has 'ALPHA/BETA'? " + TagManager.hasByName("ALPHA/BETA"));
        System.out.println("Has 'epsilon|zeta'? " + TagManager.hasByName("epsilon|zeta"));
        System.out.println("Has 'gamma delta'? " + TagManager.hasByName("gamma delta"));
        System.out.println("Has 'missing'? " + TagManager.hasByName("missing"));

        System.out.println("\n=== Get By Normalized Name ===");
        System.out.println("Get 'alpha beta': " + TagManager.getByName("alpha beta"));
        System.out.println("Get 'GAMMA/DELTA': " + TagManager.getByName("GAMMA/DELTA"));
        System.out.println("Get 'epsilon zeta': " + TagManager.getByName("epsilon zeta"));

        System.out.println("\n=== Remove By Name ===");
        boolean removed = TagManager.removeByName("ALPHA/BETA");
        System.out.println("Removed 'ALPHA/BETA': " + removed);
        System.out.println("Tags after removal: " + TagManager.view());

        System.out.println("\n=== ID-Based Operations ===");
        String gammaId = t2.getUniqueId();
        System.out.println("Has Gamma/Delta by ID? " + TagManager.has(gammaId));
        boolean idRemoved = TagManager.remove(gammaId);
        System.out.println("Remove Gamma/Delta by ID: " + idRemoved);
        System.out.println("Tags after ID removal: " + TagManager.view());

        System.out.println("\n=== Expiration Test ===");
        Tag expiring = new Tag("Temp", 2);
        TagManager.add(expiring);
        System.out.println("Day 0: " + TagManager.view());
        for (int day = 1; day <= 3; day++) {
            System.out.println("Day " + day + ":");
            TagManager.onNewDay();
            System.out.println(TagManager.view());
        }

        System.out.println("\n=== Export / Import ===");
        TagManager.add(new Tag("PersistA"));
        TagManager.add(new Tag("PersistB"));
        List<Tag> exported = TagManager.exportTags();
        System.out.println("Exported: " + exported);

        TagManager.clear();
        System.out.println("After clear: " + TagManager.view());

        TagManager.importTags(exported);
        System.out.println("After import: " + TagManager.view());

        System.out.println("\n=== Random ID Test ===");
        System.out.println("Remove random ID: " + TagManager.remove(UUID.randomUUID().toString()));

        System.out.println("\n=== Final Size & Clear ===");
        System.out.println("Size: " + TagManager.size());
        TagManager.clear();
        System.out.println("Final state: " + TagManager.view());
    }
}