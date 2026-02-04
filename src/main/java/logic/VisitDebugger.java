package logic;

import main.*;
import java.util.List;

public class VisitDebugger {

    private final Game game;
    private boolean debugRejected = true;

    public VisitDebugger(Game game) {
        this.game = game;
    }

    public void setDebugRejected(boolean debugRejected) {
        this.debugRejected = debugRejected;
    }

    public void debugVisit(GameCharacter character, Visit visit, List<Item> sells, List<Item> buys, String fireEffect) {
        System.out.println("===== VISIT TRIGGERED =====");
        System.out.println("Character: " + character.name);
        System.out.println("Type: " + visit.type);
        System.out.println("Fire Effect: " + fireEffect);

        System.out.println("Items For Sale:");
        if (sells.isEmpty()) System.out.println("  none");
        else sells.forEach(i -> System.out.println("  " + i.name));

        System.out.println("Items Wanted:");
        if (buys.isEmpty()) System.out.println("  none");
        else buys.forEach(i -> System.out.println("  " + i.name));

        System.out.println("===========================");
    }

    public void debugRejected(GameCharacter character, Visit visit, String reason, String fireEffect, MatchResult match) {
        if (!debugRejected) return;

        System.out.println("----- VISIT REJECTED -----");
        System.out.println("Character: " + character.name);
        if (visit != null) System.out.println("Type: " + visit.type);
        System.out.println("Reason: " + reason);
        System.out.println("Current Fire: " + fireEffect);
        System.out.println("World Tags: " + game.worldTags);

        if (match != null) {
            System.out.println("Required Fire: " + match.requiredFire);
            System.out.println("Required Tags: " + match.requiredTags);
            System.out.println("Fire Match: " + match.fireOk);
            System.out.println("Tag Match: " + match.tagsOk);
        }

        System.out.println("--------------------------");
    }
}