package com.ancientshores.AncientRPG.Experience;

import com.ancientshores.AncientRPG.API.AncientGainExperienceEvent;
import com.ancientshores.AncientRPG.API.AncientLevelupEvent;
import com.ancientshores.AncientRPG.AncientRPG;
import com.ancientshores.AncientRPG.Party.AncientRPGParty;
import com.ancientshores.AncientRPG.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class AncientRPGExperience implements Serializable, ConfigurationSerializable {
    /**
     *
     */
    public static float multiplier = 1;
    private static final long serialVersionUID = 1L;
    public int level;
    public int xp;
    public final String name;
    public static final HashSet<Entity> alreadyDead = new HashSet<Entity>();

    static {
        ConfigurationSerialization.registerClass(AncientRPGExperience.class);
    }

    // Enabled
    private static final String XPConfigEnabled = "XP.XPsystem enabled";
    public static boolean enabled = true;
    private static final String XPConfigWorlds = "XP.XPsystem enabled world";
    public static String[] worlds = new String[0];
    public static final HashMap<Integer, Integer> levelMap = new HashMap<Integer, Integer>();
    public final static String nodeXPAdmin = "AncientRPG.XP.Admin";

    // Experience of creeps
    private static final String XPConfigSpider = "XP.Experience of spider";
    private static int XPOfSpider = 10;
    private static final String XPConfigCaveSpider = "XP.Experience of cave spider";
    private static int XPOfCaveSpider = 10;
    private static final String XPConfigSkeleton = "XP.Experience of skeleton";
    private static int XPOfSkeleton = 6;
    private static final String XPConfigZombie = "XP.Experience of zombie";
    private static int XPOfZombie = 7;
    private static final String XPConfigCreeper = "XP.Experience of creeper";
    private static int XPOfCreeper = 10;
    private static final String XPConfigEnderman = "XP.Experience of enderman";
    private static int XPOfEnderman = 12;
    private static final String XPConfigPigzombie = "XP.Experience of pigzombie";
    private static int XPOfPigzombie = 10;
    private static final String XPConfigGhast = "XP.Experience of ghast";
    private static int XPOfGhast = 15;
    private static final String XPConfigWolf = "XP.Experience of wolf";
    private static int XPOfWolf = 10;
    private static final String XPConfigSlime = "XP.Experience of slime";
    private static int XPOfSlime = 10;
    private static final String XPConfigGiant = "XP.Experience of giant";
    private static int XPOfGiant = 20;
    private static final String XPConfigIronGolem = "XP.Experience of iron golem";
    private static int XPOfIronGolem = 10;
    private static final String XPConfigSnowman = "XP.Experience of snowman";
    private static int XPOfSnowman = 10;
    private static final String XPConfigOcelot = "XP.Experience of ocelot";
    private static int XPOfOcelot = 10;
    private static final String XPConfigSilverfish = "XP.Experience of silverfish";
    private static int XPOfSilverfish = 5;
    private static final String XPConfigEnderdragon = "XP.Experience of enderdragon";
    private static int XPOfEnderdragon = 30;
    private static final String XPConfigWitch = "XP.Experience of witch";
    private static int XPOfWitch = 10;
    private static final String XPConfigWither = "XP.Experience of wither";
    private static int XPOfWither = 30;

    // Experience of mining/woodcutting
    private static final String XPConfigStone = "XP.Experience of stone";
    public static int XPOfStone = 1;
    private static final String XPConfigCoal = "XP.Experience of coal";
    public static int XPOfCoal = 2;
    private static final String XPConfigIron = "XP.Experience of iron";
    public static int XPOfIron = 4;
    private static final String XPConfigLapis = "XP.Experience of lapis";
    public static int XPOfLapis = 3;
    private static final String XPConfigGold = "XP.Experience of gold";
    public static int XPOfGold = 6;
    private static final String XPConfigDiamond = "XP.Experience of diamond";
    public static int XPOfDiamond = 10;
    private static final String XPConfigRedstone = "XP.Experience of redstone";
    public static int XPOfRedstone = 4;
    private static final String XPConfigGlowstone = "XP.Experience of glowstone";
    public static int XPOfGlowstone = 7;
    private static final String XPConfigNetherrack = "XP.Experience of netherrack";
    public static int XPOfNetherrack = 7;
    private static final String XPConfigWood = "XP.Experience of wood";
    public static int XPOfWood = 10;

    // Levels
    private static final String XPConfigPlayer = "XP.Experience of player";
    private static int XPOfPlayer = 30;
    private static final String XPConfigBlaze = "XP.Experience of blaze";
    private static int XPOfBlaze = 120;
    private static final String XPConfigMaxLevel = "XP.max level";
    private static int MaxLevel = 10;

    public AncientRPGExperience(String playerName) {
        level = 1;
        xp = 0;
        name = playerName;
    }

    public AncientRPGExperience(Map<String, Object> map) {
        this.level = (Integer) map.get("level");
        this.xp = (Integer) map.get("xp");
        this.name = (String) map.get("xpname");
    }

    public static boolean isWorldEnabled(Player p) {
        if (worlds.length == 0 || (worlds.length >= 1 && (worlds[0] == null || worlds[0].equals("")))) {
            return true;
        }
        for (String s : worlds) {
            if (s == null) {
                continue;
            }
            if (p.getWorld().getName().equalsIgnoreCase(s) || s.equalsIgnoreCase("all")) {
                return true;
            }
        }
        return false;
    }

    public void addXP(int xp, boolean party) {
        xp *= multiplier;
        Player p = AncientRPG.plugin.getServer().getPlayer(name);
        if (p == null || !isWorldEnabled(p)) {
            return;
        }
        try {
            if (AncientRPGParty.splitxp && party) {
                AncientRPGParty mParty = AncientRPGParty.getPlayersParty(p);
                if (mParty != null) {
                    HashSet<Player> inrangeps = new HashSet<Player>();
                    Collection<Player> players = mParty.Member;
                    for (Player mp : players) {
                        if (mp == p) {
                            continue;
                        }
                        if (mp.isOnline() && mp.getLocation().getWorld().getName().equals(p.getWorld().getName())
                                && Math.abs(mp.getLocation().distance(p.getLocation())) < AncientRPGParty.splitxprange) {
                            inrangeps.add(mp);
                        }
                    }
                    int times = inrangeps.size() + 1;
                    int nxp = (xp / times);
                    xp = nxp;
                    for (Player pp : inrangeps) {
                        PlayerData.getPlayerData(pp.getName()).getXpSystem().addXP(nxp, false);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        AncientGainExperienceEvent gainevent = new AncientGainExperienceEvent(this.xp, p, xp);
        Bukkit.getServer().getPluginManager().callEvent(gainevent);
        if (gainevent.cancelled) {
            return;
        }
        this.xp += xp;
        int oldlevel = level;
        level = 1;
        for (int i = 1; i <= MaxLevel; i++) {
            if (this.xp >= getExperienceOfLevel(i)) {
                level = i;
            } else {
                break;
            }
        }
        if (level != oldlevel) {
            if (p != null) {
                AncientLevelupEvent event = new AncientLevelupEvent(this.level, p, this.xp, xp);
                Bukkit.getServer().getPluginManager().callEvent(event);
                if (event.cancelled) {
                    this.level = oldlevel;
                    return;
                }
                PlayerData.getPlayerData(p.getName()).getHpsystem().setMaxHp();
                PlayerData.getPlayerData(p.getName()).getManasystem().setMaxMana();
                /*AncientRPGClass mClass = AncientRPGClass.classList.get(PlayerData.getPlayerData(p.getName()).getClassName().toLowerCase());
                if (mClass != null)
				{
					PlayerData.getPlayerData(p.getName()).getHpsystem().hpReg = mClass.hpreglevel.get(level).intValue();
				}
				if (AncientRPGExperience.isEnabled())
				{
					PlayerData pd = PlayerData.getPlayerData(name);
					AncientRPGClass newClass = AncientRPGClass.classList.get(pd.getClassName().toLowerCase());
					if (newClass != null)
					{
						pd.getHpsystem().maxhp = newClass.hplevel.get(pd.getXpSystem().level).intValue();
						pd.getHpsystem().hpReg = newClass.hpreglevel.get(pd.getXpSystem().level).intValue();
					}
					pd.getManasystem().setMaxMana();
				}*/
                p.sendMessage(ChatColor.GOLD + "[" + AncientRPG.brand + "] " + ChatColor.YELLOW + "You reached level " + level);
            }
        }
    }

    public void recalculateLevel() {
        for (int i = 1; i <= MaxLevel; i++) {
            if (this.xp >= getExperienceOfLevel(i)) {
                level = i;
            } else {
                break;
            }
        }
    }

    public static void processCommand(CommandSender sender, String[] args) {
        if (args.length > 1 && args[0].equalsIgnoreCase("setxp")) {
            SetXpCommand.setXp(sender, args);
            return;
        }
        if (args.length > 1 && args[0].equalsIgnoreCase("addxp")) {
            AddXpCommand.addXp(sender, args);
            return;
        }
        if (args.length > 1 && args[0].equalsIgnoreCase("setxpmultiplier")) {
            SetXpMultiplierCommand.setXpMultiplier(sender, args);
            return;
        }
        if (!(sender instanceof Player) || !isWorldEnabled((Player) sender)) {
            return;
        }
        PlayerData pd = PlayerData.getPlayerData(sender.getName());
        sender.sendMessage(ChatColor.GOLD + "-----------------------------------------------------");
        sender.sendMessage(ChatColor.GOLD + "[" + AncientRPG.brand + "] " + ChatColor.YELLOW + "Experience Information");
        sender.sendMessage("You are level " + pd.getXpSystem().level);
        sender.sendMessage("You have already " + pd.getXpSystem().xp + " XP");
        int xpofcurrentLevel = pd.getXpSystem().xp - pd.getXpSystem().getExperienceOfLevel(pd.getXpSystem().level);
        sender.sendMessage("You have " + xpofcurrentLevel + " of the current level");
        if (pd.getXpSystem().level < MaxLevel) {
            int xpOfNextLevel = pd.getXpSystem().getExperienceOfLevel(pd.getXpSystem().level + 1) - pd.getXpSystem().xp;
            sender.sendMessage("To reach the next level you need " + xpOfNextLevel + " more xp");
        }
        sender.sendMessage(ChatColor.GOLD + "-----------------------------------------------------");
    }

    int getExperienceOfLevel(int level) {
        return levelMap.get(level);
    }

    public static void writeConfig(AncientRPG plugin) {
        File newfile = new File(plugin.getDataFolder().getPath() + File.separator + "xpconfig.yml");
        if (!newfile.exists()) {
            try {
                newfile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        YamlConfiguration yc = new YamlConfiguration();
        yc.set(XPConfigEnabled, enabled);
        if (yc.get(XPConfigWorlds) == null) {
            yc.set(XPConfigWorlds, "");
        }
        yc.set(XPConfigSpider, XPOfSpider);
        yc.set(XPConfigSkeleton, XPOfSkeleton);
        yc.set(XPConfigZombie, XPOfZombie);
        yc.set(XPConfigCreeper, XPOfCreeper);
        yc.set(XPConfigEnderman, XPOfEnderman);
        yc.set(XPConfigPigzombie, XPOfPigzombie);
        yc.set(XPConfigGhast, XPOfGhast);
        yc.set(XPConfigSilverfish, XPOfSilverfish);
        yc.set(XPConfigIronGolem, XPOfIronGolem);
        yc.set(XPConfigSnowman, XPOfSnowman);
        yc.set(XPConfigOcelot, XPOfOcelot);
        yc.set(XPConfigBlaze, XPOfBlaze);
        yc.set(XPConfigSlime, XPOfSlime);
        yc.set(XPConfigWolf, XPOfWolf);
        yc.set(XPConfigGiant, XPOfGiant);
        yc.set(XPConfigEnderdragon, XPOfEnderdragon);
        yc.set(XPConfigStone, XPOfStone);
        yc.set(XPConfigCoal, XPOfCoal);
        yc.set(XPConfigLapis, XPOfLapis);
        yc.set(XPConfigIron, XPOfIron);
        yc.set(XPConfigGold, XPOfGold);
        yc.set(XPConfigDiamond, XPOfDiamond);
        yc.set(XPConfigRedstone, XPOfRedstone);
        yc.set(XPConfigGlowstone, XPOfGlowstone);
        yc.set(XPConfigNetherrack, XPOfNetherrack);
        yc.set(XPConfigWood, XPOfWood);
        yc.set(XPConfigMaxLevel, MaxLevel);
        yc.set(XPConfigPlayer, XPOfPlayer);
        yc.set(XPConfigCaveSpider, XPOfCaveSpider);
        yc.set(XPConfigWitch, XPOfWitch);
        yc.set(XPConfigWither, XPOfWither);
        for (int i = 1; i <= MaxLevel; i++) {
            if (yc.get(("XP.Experience of level " + i)) == null) {
                if (levelMap.containsKey(i)) {
                    yc.set(("XP.Experience of level " + i), levelMap.get(i));
                } else {
                    yc.set(("XP.Experience of level " + i), 600 * (i - 1));
                }
            }
        }
        try {
            yc.save(newfile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void loadConfig(AncientRPG plugin) {
        File newfile = new File(plugin.getDataFolder().getPath() + File.separator + "xpconfig.yml");
        if (newfile.exists()) {
            YamlConfiguration yc = new YamlConfiguration();
            try {
                yc.load(newfile);
                MaxLevel = yc.getInt(XPConfigMaxLevel, MaxLevel);
                enabled = yc.getBoolean(XPConfigEnabled, enabled);
                worlds = yc.getString(XPConfigWorlds, "").split(",");
                for (int i = 0; i < worlds.length; i++) {
                    worlds[i] = worlds[i].trim();
                }
                XPOfSpider = yc.getInt(XPConfigSpider, XPOfSpider);
                XPOfCaveSpider = yc.getInt(XPConfigCaveSpider, XPOfCaveSpider);
                XPOfSkeleton = yc.getInt(XPConfigSkeleton, XPOfSkeleton);
                XPOfZombie = yc.getInt(XPConfigZombie, XPOfZombie);
                XPOfCreeper = yc.getInt(XPConfigCreeper, XPOfCreeper);
                XPOfEnderman = yc.getInt(XPConfigEnderman, XPOfEnderman);
                XPOfPigzombie = yc.getInt(XPConfigPigzombie, XPOfPigzombie);
                XPOfSilverfish = yc.getInt(XPConfigSilverfish, XPOfSilverfish);
                XPOfIronGolem = yc.getInt(XPConfigIronGolem, XPOfIronGolem);
                XPOfSnowman = yc.getInt(XPConfigSnowman, XPOfSnowman);
                XPOfOcelot = yc.getInt(XPConfigOcelot, XPOfOcelot);
                XPOfBlaze = yc.getInt(XPConfigBlaze, XPOfBlaze);
                XPOfGhast = yc.getInt(XPConfigGhast, XPOfGhast);
                XPOfSlime = yc.getInt(XPConfigSlime, XPOfSlime);
                XPOfWolf = yc.getInt(XPConfigWolf, XPOfWolf);
                XPOfGiant = yc.getInt(XPConfigGiant, XPOfGiant);
                XPOfEnderdragon = yc.getInt(XPConfigEnderdragon, XPOfEnderdragon);
                XPOfStone = yc.getInt(XPConfigStone, XPOfStone);
                XPOfCoal = yc.getInt(XPConfigCoal, XPOfCoal);
                XPOfLapis = yc.getInt(XPConfigLapis, XPOfLapis);
                XPOfGold = yc.getInt(XPConfigGold, XPOfGold);
                XPOfIron = yc.getInt(XPConfigIron, XPOfIron);
                XPOfDiamond = yc.getInt(XPConfigDiamond, XPOfDiamond);
                XPOfGlowstone = yc.getInt(XPConfigGlowstone, XPOfGlowstone);
                XPOfNetherrack = yc.getInt(XPConfigNetherrack, XPOfNetherrack);
                XPOfRedstone = yc.getInt(XPConfigRedstone, XPOfRedstone);
                XPOfWood = yc.getInt(XPConfigWood, XPOfWood);
                XPOfPlayer = yc.getInt(XPConfigPlayer, XPOfPlayer);
                XPOfWitch = yc.getInt(XPConfigWitch, XPOfWitch);
                XPOfWither = yc.getInt(XPConfigWither, XPOfWither);
                for (int i = 1; i <= MaxLevel; i++) {
                    levelMap.put(i, yc.getInt(("XP.Experience of level " + i), 600 * (i - 1)));
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            MaxLevel = plugin.getConfig().getInt(XPConfigMaxLevel, MaxLevel);
            enabled = plugin.getConfig().getBoolean(XPConfigEnabled, enabled);
            worlds = plugin.getConfig().getString(XPConfigWorlds, "").split(",");
            for (int i = 0; i < worlds.length; i++) {
                worlds[i] = worlds[i].trim();
            }
            XPOfSpider = plugin.getConfig().getInt(XPConfigSpider, XPOfSpider);
            XPOfCaveSpider = plugin.getConfig().getInt(XPConfigCaveSpider, XPOfCaveSpider);
            XPOfSkeleton = plugin.getConfig().getInt(XPConfigSkeleton, XPOfSkeleton);
            XPOfZombie = plugin.getConfig().getInt(XPConfigZombie, XPOfZombie);
            XPOfCreeper = plugin.getConfig().getInt(XPConfigCreeper, XPOfCreeper);
            XPOfEnderman = plugin.getConfig().getInt(XPConfigEnderman, XPOfEnderman);
            XPOfPigzombie = plugin.getConfig().getInt(XPConfigPigzombie, XPOfPigzombie);
            XPOfSilverfish = plugin.getConfig().getInt(XPConfigSilverfish, XPOfSilverfish);
            XPOfIronGolem = plugin.getConfig().getInt(XPConfigIronGolem, XPOfIronGolem);
            XPOfSnowman = plugin.getConfig().getInt(XPConfigSnowman, XPOfSnowman);
            XPOfOcelot = plugin.getConfig().getInt(XPConfigOcelot, XPOfOcelot);
            XPOfBlaze = plugin.getConfig().getInt(XPConfigBlaze, XPOfBlaze);
            XPOfGhast = plugin.getConfig().getInt(XPConfigGhast, XPOfGhast);
            XPOfSlime = plugin.getConfig().getInt(XPConfigSlime, XPOfSlime);
            XPOfWolf = plugin.getConfig().getInt(XPConfigWolf, XPOfWolf);
            XPOfGiant = plugin.getConfig().getInt(XPConfigGiant, XPOfGiant);
            XPOfEnderdragon = plugin.getConfig().getInt(XPConfigEnderdragon, XPOfEnderdragon);
            XPOfStone = plugin.getConfig().getInt(XPConfigStone, XPOfStone);
            XPOfCoal = plugin.getConfig().getInt(XPConfigCoal, XPOfCoal);
            XPOfLapis = plugin.getConfig().getInt(XPConfigLapis, XPOfLapis);
            XPOfGold = plugin.getConfig().getInt(XPConfigGold, XPOfGold);
            XPOfIron = plugin.getConfig().getInt(XPConfigIron, XPOfIron);
            XPOfDiamond = plugin.getConfig().getInt(XPConfigDiamond, XPOfDiamond);
            XPOfGlowstone = plugin.getConfig().getInt(XPConfigGlowstone, XPOfGlowstone);
            XPOfNetherrack = plugin.getConfig().getInt(XPConfigNetherrack, XPOfNetherrack);
            XPOfRedstone = plugin.getConfig().getInt(XPConfigRedstone, XPOfRedstone);
            XPOfWood = plugin.getConfig().getInt(XPConfigWood, XPOfWood);
            XPOfPlayer = plugin.getConfig().getInt(XPConfigPlayer, XPOfPlayer);
            XPOfWitch = plugin.getConfig().getInt(XPConfigWitch, XPOfWitch);
            XPOfWither = plugin.getConfig().getInt(XPConfigWither, XPOfWither);
            for (int i = 1; i <= MaxLevel; i++) {
                levelMap.put(i, plugin.getConfig().getInt(("XP.Experience of level " + i), 600 * (i - 1)));
            }
        }
    }

    public static void processEntityDamageByEntityEvent(final EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if (damager instanceof Projectile) {
            damager = ((Projectile) event.getDamager()).getShooter();
        }
        if (event.getCause() == DamageCause.CUSTOM) {
            return;
        }
        if (damager instanceof Player && !alreadyDead.contains(event.getEntity()) && event instanceof EntityDamageByEntityEvent) {
            if (event.getEntity() instanceof LivingEntity && ((LivingEntity) event.getEntity()).getHealth() - event.getDamage() <= 0) {
                alreadyDead.add(event.getEntity());
                Bukkit.getScheduler().scheduleSyncDelayedTask(AncientRPG.plugin, new Runnable() {

                    @Override
                    public void run() {
                        alreadyDead.remove(event.getEntity());
                    }

                }, 500);
                Player mPlayer = (Player) damager;
                int xp = getXPOfEntity(event.getEntity());
                PlayerData pd = PlayerData.getPlayerData(mPlayer.getName());
                pd.getXpSystem().addXP(xp, true);
            }
        }
    }

    public static int getXPOfEntity(Entity e) {
        if (e instanceof Zombie) {
            return XPOfZombie;
        } else if (e instanceof CaveSpider) {
            return XPOfCaveSpider;
        } else if (e instanceof Spider) {
            return XPOfSpider;
        } else if (e instanceof Skeleton) {
            return XPOfSkeleton;
        } else if (e instanceof Creeper) {
            return XPOfCreeper;
        } else if (e instanceof Enderman) {
            return XPOfEnderman;
        } else if (e instanceof PigZombie) {
            return XPOfPigzombie;
        } else if (e instanceof Ghast) {
            return XPOfGhast;
        } else if (e instanceof Slime) {
            return XPOfSlime;
        } else if (e instanceof Wolf) {
            return XPOfWolf;
        } else if (e instanceof Giant) {
            return XPOfGiant;
        } else if (e instanceof EnderDragon) {
            return XPOfEnderdragon;
        } else if (e instanceof Player) {
            return XPOfPlayer;
        } else if (e instanceof Ocelot) {
            return XPOfOcelot;
        } else if (e instanceof Snowman) {
            return XPOfSnowman;
        } else if (e instanceof IronGolem) {
            return XPOfIronGolem;
        } else if (e instanceof Silverfish) {
            return XPOfSilverfish;
        } else if (e instanceof Witch) {
            return XPOfWitch;
        } else if (e instanceof Wither) {
            return XPOfWither;
        }
        return 0;
    }

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("level", level);
        map.put("xp", xp);
        map.put("xpname", name);
        return map;
    }

    public static boolean isEnabled() {
        return enabled;
    }
}