package com.ancientshores.AncientRPG.Classes.Commands;

import com.ancientshores.AncientRPG.Spells.Commands.SpellsCommand;
import org.bukkit.entity.Player;

public class ClassSpelllistCommand {
    public static void spellListCommand(String[] args, Player p) {
        int page = 1;
        if (args.length == 2) {
            try {
                page = Integer.parseInt(args[1]);
            } catch (Exception ignored) {

            }
        }
        showSpellList(p, page);
    }

    public static void showSpellList(Player p, int page) {
        SpellsCommand.showSpellList(p, page);
    }
}
