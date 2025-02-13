package com.ancientshores.AncientRPG.Classes.Spells.Commands;

import org.bukkit.entity.Player;

import com.ancientshores.AncientRPG.AncientRPG;
import com.ancientshores.AncientRPG.Classes.Spells.CommandDescription;
import com.ancientshores.AncientRPG.Classes.Spells.ParameterType;
import com.ancientshores.AncientRPG.Listeners.AncientRPGEntityListener;

public class InvulnerableCommand extends ICommand {
	@CommandDescription(description = "<html>Makes the player invulnerable for the specified amount of time</html>",
			argnames = {"player", "duration"}, name = "Invulnerable", parameters = {ParameterType.Player, ParameterType.Number})
	public InvulnerableCommand() {
		this.paramTypes = new ParameterType[]{ParameterType.Player, ParameterType.Number};
	}

	@Override
	public boolean playCommand(final EffectArgs ca) {
		try {
			if (ca.getParams().get(0) instanceof Player[] && ca.getParams().get(1) instanceof Number) {
				final Player[] target = (Player[]) ca.getParams().get(0);
				final int time = (int) ((Number) ca.getParams().get(1)).doubleValue();
				ca.getSpellInfo().working = true;
				int t = Math.round(time / 50);
				if (t == 0) {
					t = Integer.MAX_VALUE;
				}
				if (target != null && target.length > 0 && target[0] != null) {
					AncientRPG.plugin.scheduleThreadSafeTask(AncientRPG.plugin, new Runnable() {

						@Override
						public void run() {
							if (time > 0) {
								for (final Player p : target) {
									if (p == null) {
										continue;
									}
									AncientRPGEntityListener.setinvulnerable(p, true);
								}
							}
							ca.getSpellInfo().working = false;
						}
					});
					AncientRPG.plugin.scheduleThreadSafeTask(AncientRPG.plugin, new Runnable() {

						@Override
						public void run() {
							for (final Player p : target) {
								if (p == null) {
									continue;
								}
								AncientRPGEntityListener.setinvulnerable(p, false);
							}
						}

					}, t);
					return true;
				} else if (ca.getSpell().active) {
					ca.getCaster().sendMessage("Target not found");
				}
			}
		} catch (IndexOutOfBoundsException ignored) {
		}
		return false;
	}
}