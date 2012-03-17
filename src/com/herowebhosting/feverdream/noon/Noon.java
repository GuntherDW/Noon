package com.herowebhosting.feverdream.noon;

import com.guntherdw.bukkit.tweakcraft.Tools.ArgumentParser;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Logger;

public class Noon extends JavaPlugin {
    private int rate = 30; // SECONDS
    private final Logger log = Logger.getLogger("Minecraft");
    private HashMap<String, Integer> timerTasks = new HashMap<String, Integer>();
    private YamlConfiguration globalConfig = new YamlConfiguration();
    private File configFile = null;
    private BukkitScheduler scheduler = null;

    public void reloadNoonConfig() {
        this.scheduler.cancelTasks(this);
        try {
            globalConfig.load(configFile);
        } catch (Exception e) {
            log.severe("[Noon] Exception thrown while loading config!");
            e.printStackTrace();
            return;
        }

        ConfigurationSection section = globalConfig.getConfigurationSection("worlds");

        if (section != null) {
            Set<String> listofworlds = section.getKeys(false);
            for (String wname : listofworlds) {
                World world = this.getServer().getWorld(wname);
                if (world == null) {
                    log.severe("[Noon] World with name " + wname + " not found!");
                } else {
                    // Settings.addTime(wname, this.getConfiguration().getString("worlds."+wname, "day"));
                    SunShine ss = new SunShine(world);
                    String wantedTime = globalConfig.getString("worlds." + wname, "day");
                    log.info("[Noon] Setting time for world " + world.getName() + " to " + wantedTime);
                    ss.setWantedTime(Settings.resolveTime(wantedTime));
                    Settings.addTime(wname, wantedTime);
                    this.addTimer(world.getName(), ss);
                }
            }
        }
    }

    public void onEnable() {
        this.configFile = new File(this.getDataFolder(), "config.yml");
        this.scheduler = getServer().getScheduler();
        this.reloadNoonConfig();
        log.info("[Noon] Noon (v1.5 by Feverdream-GuntherDW) is on.");
    }

    public void addTimer(String worldname, SunShine ss) {
        if (this.timerTasks.containsKey(worldname)) {
            scheduler.cancelTask(this.timerTasks.get(worldname));
            this.timerTasks.remove(worldname);
        }
        int taskId = scheduler.scheduleAsyncRepeatingTask(this, ss, 0L, this.rate * 20);
        this.timerTasks.put(worldname, taskId);

    }

    public boolean setTime(World world, String time) {
        if (Settings.isValidTime(time)) {
            Settings.addTime(world.getName(), time);
            return true;
        } else
            return false;
    }

    public void onDisable() {
        this.getServer().getScheduler().cancelTasks(this);
        log.info("[Noon] Noon (v1.5 by Feverdream-GuntherDW) is off.");
    }

    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        return DoNoon(sender, args);
    }

    public boolean hasPermission(CommandSender player, String perm) {
        return player.hasPermission(perm);
    }

    protected boolean DoNoon(CommandSender agent, String[] realargs) {

        if (realargs.length > 0 && realargs[0].equalsIgnoreCase("reload")) {
            if (agent.hasPermission("noon.reload")) {
                this.reloadNoonConfig();
                agent.sendMessage("[Noon] Reloading config...");
            } else {
                agent.sendMessage("You don't have permissions for that!");
            }

            return true;
        }

        ArgumentParser parser = new ArgumentParser(realargs);
        String[] args = parser.getNormalArgs();
        String w = parser.getString("w", null);
        World world = null;

        if (w == null) {
            if (agent instanceof Player) {
                world = ((Player) agent).getWorld();
            } else {
                agent.sendMessage("[Noon] No world specified!");
                return false;
            }
        } else {
            world = this.getServer().getWorld(w);
        }

        if (args.length != 1) {
            if (world != null) {
                String set = Settings.getTime(world.getName());
                if (set != null)
                    agent.sendMessage("[Noon] '" + world.getName() + "' has been set to '" + set + "'");
                else
                    agent.sendMessage("[Noon] '" + world.getName() + "' doesn't have a noon setting!");
            } else {
                agent.sendMessage("[Noon] No world specified!");
            }
            return true;
        }

        String name = null;
        boolean allowed = false;
        if (agent instanceof Player) {
            name = ((Player) agent).getName().toLowerCase();
        } else {
            name = "CONSOLE";
        }

        String cmd = args[0].trim();
        if (world == null) {
            agent.sendMessage("[Noon] No world specified!");
            return true;
        }

        if (hasPermission(agent, "noon.noon")) {

            String lastSetWho = null, lastSetHow = null;

            lastSetWho = name;
            if (cmd.equalsIgnoreCase("day")) {
                lastSetHow = "day";
                // Settings.dayStart = 0;
            } else if (cmd.equalsIgnoreCase("sunset")) {
                lastSetHow = "sunset";
                // Settings.dayStart = 12000;
            } else if (cmd.equalsIgnoreCase("night")) {
                lastSetHow = "night";
                // Settings.dayStart = 13800;
            } else if (cmd.equalsIgnoreCase("sunrise")) {
                lastSetHow = "sunrise";
                // Settings.dayStart = 22200;
            } else if (cmd.equalsIgnoreCase("tweakday")) {
                lastSetHow = "tweakday";
                // Settings.dayStart = 21900;
            } else {
                lastSetHow = null;
            }
            // SetAllTimes(Settings.dayStart);
            if (lastSetHow != null && Settings.resolveTime(lastSetHow) >= 0) {
                Settings.addTime(world.getName(), lastSetHow);
                SunShine ss = new SunShine(world);
                // ss.
                ss.setWantedTime(Settings.resolveTime(lastSetHow));
                this.addTimer(world.getName(), ss);
                agent.sendMessage("[Noon] Setting noontime for world " + world.getName() + " to " + lastSetHow); // +" : "+Settings.resolveTime(lastSetHow));
            } else {
                agent.sendMessage("[Noon] Invalid noontime!");
            }

            return true;

        } else {
            agent.sendMessage(ChatColor.RED + "Noon: You do not have access to control time.");
            return true;
        }
    }
}