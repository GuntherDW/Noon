package com.herowebhosting.feverdream.noon;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Settings {
    public static String config_file = "noon.settings";
    public static String config_comment = "Noon Control Configuration File.";
    public static String config_value_start_day = "always-time";
    // public static int config_value_start_day_default = 0;
    // public static String config_value_start_day_default_str = "day";
    public static boolean config_use_permissions = false;
    // public static String config_value_who = "time-control-who";
    // public static String config_value_who_default = "feverdream,notch";
    // private static Logger log = Logger.getLogger("Minecraft");
    private static String[] validnames = new String[]{"day", "sunset", "night", "sunrise", "tweakday", "day"};


    // public static int dayStart = config_value_start_day_default;
    // public static ArrayList<String> controlWho = new ArrayList(7);
    public static Map<String, String> worldtime = new HashMap<String, String>();
    public static Map<String, Long> worldtimelong = new HashMap<String, Long>();

    public static boolean isValidTime(String name) {
        return Arrays.asList(validnames).contains(name);
    }

    public static long resolveTime(String timename) {
        Long timestart;
        if (timename.equalsIgnoreCase("day")) {
            timestart = 0L;
        } else if (timename.equalsIgnoreCase("sunset")) {
            timestart = 12000L;
        } else if (timename.equalsIgnoreCase("night")) {
            timestart = 13800L;
        } else if (timename.equalsIgnoreCase("sunrise")) {
            timestart = 22200L;
        } else if (timename.equalsIgnoreCase("tweakday")) {
            timestart = 21900L;
        } else {
            timestart = -1L;
        }
        return timestart;

    }

    public static void addTime(String worldname, String time) {
        /* if(!worldtime.containsKey(worldname))
        { */
        worldtime.put(worldname, time);
        worldtimelong.put(worldname, resolveTime(time));
        /* } */
    }

    public static String getTime(String world) {
        if (worldtime.containsKey(world))
            return worldtime.get(world);
        else
            return null;
    }

    public static Long getTimeLong(String world) {
        if (worldtimelong.containsKey(world))
            return worldtimelong.get(world);
        else
            return null;
    }
}