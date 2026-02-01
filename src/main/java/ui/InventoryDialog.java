// ===== InventoryDialog.java =====
package ui;

import main.Game;
import main.Item;

import javax.swing.*;

public class InventoryDialog {

    public static void show(Game game) {
        JTextArea area = new JTextArea();
        area.setEditable(false);

        StringBuilder sb = new StringBuilder();
        sb.append("Food: ").append(game.player.food).append("\n");
        sb.append("Fuel: ").append(game.player.fuel).append("\n\n");

        for (Item item : game.player.inventory) {
            sb.append(item.name)
              .append(" | ")
              .append(item.type)
              .append("\n");
        }

        area.setText(sb.toString());
        JOptionPane.showMessageDialog(null, new JScrollPane(area));
    }
}
