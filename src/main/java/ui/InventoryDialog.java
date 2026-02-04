package ui;

import main.Game;
import main.ItemStack;

import javax.swing.*;

public class InventoryDialog {

    public static void show(Game game) {
        JTextArea area = new JTextArea();
        area.setEditable(false);

        StringBuilder sb = new StringBuilder();
        sb.append("Food: ").append(game.player.food).append("\n");
        sb.append("Fuel: ").append(game.player.fuel).append("\n");
        sb.append("Money: ").append(game.player.money).append("\n\n");

        for (ItemStack stack : game.player.inventory) {
            sb.append(stack.item.name);
            if (stack.count > 1) {
                sb.append(" x").append(stack.count);
            }
            sb.append(" | ").append(stack.item.type).append("\n");
        }

        area.setText(sb.toString());
        JOptionPane.showMessageDialog(null, new JScrollPane(area));
    }
}