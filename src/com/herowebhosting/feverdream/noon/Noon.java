package com.herowebhosting.feverdream.noon;

import com.guntherdw.bukkit.tweakcraft.Tools.ArgumentParser;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.logging.Logger;

public class Noon extends JavaPlugin {
    // private Timer tick = null;
    // private int rate = 30000;
    private int rate = 1000;
    // private Settings settings = new Settings();
    // private Hashtable<Timer, SunShine> clocks = new Hashtable<Timer, SunShine>();
    private Map<String, NoonTimer> timertasks = new HashMap<String, NoonTimer>();
    private final Logger log = Logger.getLogger("Minecraft");
    public static String lastSetWho = null;
    public static String lastSetHow = null;

    public void reloadConfig() {
        if (!this.timertasks.isEmpty()) {
            for (NoonTimer tick : this.timertasks.values()) {
                Timer timer = tick.getTimertask();
                timer.cancel();
            }

            this.timertasks.clear();
        }

        ConfigurationSection section = getConfig().getConfigurationSection("worlds");


        if (section != null) {
            Set<String> listofworlds = section.getKeys(false);
            // List<String> listofworlds = getConfig().getKeys("worlds");
            for (String wname : listofworlds) {
                World world = getServer().getWorld(wname);
                if (world == null) {
                    log.severe("[Noon] World with name " + wname + " not found!");
                } else {
                    // Settings.addTime(wname, this.getConfiguration().getString("worlds."+wname, "day"));
                    log.info("[Noon] Setting time for world " + world.getName());
                    SunShine ss = new SunShine(world);
                    ss.setWantedTime(Settings.resolveTime(getConfig().getString("worlds." + wname, "day")));

                    this.addTimer(world.getName(), ss);
                }
            }
        }
        if (getConfig().getBoolean("permissions", false)) {
            log.info("[Noon] Permissions support is enabled!");
        }
    }

    public void onEnable() {
        this.reloadConfig();
        log.info("[Noon] Noon (v1.5 by Feverdream-GuntherDW) is on.");
    }

    public void addTimer(String worldname, SunShine ss) {
        if (this.timertasks.containsKey(worldname)) {
            this.timertasks.get(worldname).getSunshine().setWantedTime(ss.wantedTime);
        } else {
            Timer ti = new Timer();
            ti.schedule(ss, 0L, this.rate);
            // this.clocks.put(ti, ss);
            this.timertasks.put(worldname, new NoonTimer(ti, ss));
        }
    }

    public boolean setTime(World world, String time) {
        if (Settings.isValidTime(time)) {
            Settings.addTime(world.getName(), time);
            return true;
        } else
            return false;
    }

    public void onDisable() {
        for (NoonTimer tick : this.timertasks.values()) {
            Timer timer = tick.getTimertask();
            timer.cancel();
        }
        log.info("[Noon] Noon (v1.5 by Feverdream-GuntherDW) is off.");
    }

    public void SetAllTimes(long i) {
        for (NoonTimer nt : this.timertasks.values()) {
            SunShine ss = nt.getSunshine();
            long time = ss.clock.getTime();
            ss.clock.setTime(time - time % 24000L + 24000L + i);
        }
    }

    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        /* if (args.length == 0) {
            return GetNoonStatu(sender);
        } */
        return DoNoon(sender, args);
    }

    public boolean hasPermission(CommandSender player, String perm) {
        return player.hasPermission(perm);
    }

    protected boolean DoNoon(CommandSender agent, String[] realargs) {

        if (realargs.length > 0 && realargs[0].equalsIgnoreCase("reload")) {
            if (agent.hasPermission("noon.reload")) {
                this.reloadConfig();
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