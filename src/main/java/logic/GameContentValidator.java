package logic;

import main.Game;
import main.*;
import content.TagSpec;

import java.util.*;

public class GameContentValidator {

    private final Game game;
    private final List<String> errors = new ArrayList<>();
    private final List<String> warnings = new ArrayList<>();

    private boolean failOnError = true;

    public GameContentValidator(Game game) {
        this.game = game;
    }

    public void setFailOnError(boolean failOnError) {
        this.failOnError = failOnError;
    }

    public void validate() {

        errors.clear();
        warnings.clear();

        validateItems();
        validateCharacters();

        printReport();

        if (!errors.isEmpty() && failOnError) {
            throw new IllegalStateException("Game content validation failed. See log above.");
        }
    }

    private void validateItems() {

        Set<String> ids = new HashSet<>();

        for (Item item : game.items) {

            if (item.id == null || item.id.isBlank()) {
                errors.add("Item with missing id: " + item);
            } else if (!ids.add(item.id)) {
                errors.add("Duplicate item id: " + item.id);
            }

            if (item.name == null || item.name.isBlank()) {
                warnings.add("Item has no name: " + item.id);
            }

            if (item.fireEffect == null || item.fireEffect.isBlank()) {
                warnings.add("Item " + item.id + " has blank fireEffect. Defaulting to clean.");
            }
        }
    }

    private void validateCharacters() {

        for (GameCharacter c : game.characters) {

            if (c.id == null || c.id.isBlank()) {
                errors.add("Character with missing id: " + c.name);
            }

            if (c.visits == null) continue;

            for (Visit v : c.visits) {
                validateVisit(c, v);
            }
        }
    }

    private void validateVisit(GameCharacter c, Visit v) {

        if (v.type == null || v.type.isBlank()) {
            errors.add("Visit with missing type for character: " + c.name);
        }

        validateVisitItems(c, v.sells, "sells");
        validateVisitItems(c, v.buys, "buys");

        validateFireKeys(c, v.timerStartFireRequired);
        validateFireKeys(c, v.visitFireRequired);

        validateTagStrings(c, v.timerStartTags, "timerStartTags");
        validateTagStrings(c, v.visitRequiredTags, "visitRequiredTags");
        validateTagStrings(c, v.excludedTags, "excludedTags");

        validateTagSpecs(c, v.tagsToAdd);
    }

    private void validateVisitItems(GameCharacter c, List<VisitItem> items, String side) {

        if (items == null) return;

        for (VisitItem vi : items) {

            if (vi.item == null || vi.item.isBlank()) {
                errors.add("VisitItem with null/blank item in " + side +
                        " for character: " + c.name);
                continue;
            }

            if (findItem(vi.item) == null) {
                errors.add("VisitItem references unknown item '" + vi.item +
                        "' in " + side + " for character: " + c.name);
            }

            if (vi.rarity != null &&
                    !vi.rarity.equals("coinflip") &&
                    !vi.rarity.equals("rare") &&
                    !vi.rarity.equals("ultra_rare")) {

                warnings.add("Unknown rarity '" + vi.rarity +
                        "' for item " + vi.item +
                        " in character: " + c.name);
            }
        }
    }

    private void validateFireKeys(GameCharacter c, List<String> keys) {

        if (keys == null) return;

        for (String k : keys) {
            if (k == null || k.isBlank()) {
                errors.add("Null/blank fire key in visit for character: " + c.name);
                continue;
            }

            String norm = FireKeyNormalizer.normalize(k);

            if (!norm.contains("_")) {
                warnings.add("Suspicious fire key '" + k +
                        "' for character: " + c.name +
                        " (expected format like strong_clean)");
            }
        }
    }

    private void validateTagStrings(GameCharacter c, List<String> tags, String field) {

        if (tags == null) return;

        for (String t : tags) {
            if (t == null || t.isBlank()) {
                errors.add("Null/blank tag in " + field +
                        " for character: " + c.name);
            }
        }
    }

    private void validateTagSpecs(GameCharacter c, List<TagSpec> specs) {

        if (specs == null) return;

        for (TagSpec spec : specs) {

            if (spec == null) {
                errors.add("Null TagSpec in visit for character: " + c.name);
                continue;
            }

            if (spec.name == null || spec.name.isBlank()) {
                errors.add("TagSpec with missing name in visit for character: " + c.name);
            }

            if (spec.days != null && spec.days < 0) {
                warnings.add("Negative tag duration for '" + spec.name +
                        "' in character: " + c.name);
            }
        }
    }

    private Item findItem(String ref) {
        for (Item i : game.items) {
            if (ref.equals(i.id) || ref.equals(i.name)) return i;
        }
        return null;
    }

    private void printReport() {

        if (errors.isEmpty() && warnings.isEmpty()) {
            System.out.println("[Validator] Content OK.");
            return;
        }

        System.out.println("\n===== GAME CONTENT VALIDATION =====");

        if (!errors.isEmpty()) {
            System.out.println("Errors:");
            for (String e : errors) {
                System.out.println("  - " + e);
            }
        }

        if (!warnings.isEmpty()) {
            System.out.println("Warnings:");
            for (String w : warnings) {
                System.out.println("  - " + w);
            }
        }

        System.out.println("==================================\n");
    }
}