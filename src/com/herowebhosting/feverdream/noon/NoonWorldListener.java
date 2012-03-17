package com.herowebhosting.feverdream.noon;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldUnloadEvent;

/**
 * @author GuntherDW
 */
public class NoonWorldListener implements Listener {

    private Noon plugin;

    public NoonWorldListener(Noon instance) {
        this.plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldUnload(WorldUnloadEvent event) {
        if(event.isCancelled()) return;

        final World world = event.getWorld();

        if(plugin.hasTimer(world.getName())) {
            Settings.removeTime(world.getName());
            plugin.removeTimer(world.getName());
        }
    }
}
