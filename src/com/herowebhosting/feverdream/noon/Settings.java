package com.herowebhosting.feverdream.noon;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Settings {

    private static String[] validnames = new String[]{"day", "sunset", "night", "sunrise", "tweakday", "day"};
    public static Map<String, String> worldtime = new HashMap<String, String>();
    public static Map<String, Long> worldtimelong = new HashMap<String, Long>();

    public static boolean isValidTime(String name) {
        return Arrays.asList(validnames).contains(name);
    }

    public static long resolveTime(String timename) {

        Long timestart = null;

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

    public static String resolveTime(long timeLong) {

        if (timeLong == 0L)
            return "day";
        else if (timeLong == 12000L)
            return "sunset";
        else if (timeLong == 13800L)
            return "night";
        else if (timeLong == 22200L)
            return "tweakday";
        else if (timeLong == -1L)
            return null;
        else
            return null;

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

    public static void removeTime(String world) {
        if (worldtime.containsKey(world))
            worldtime.remove(world);

        if (worldtimelong.containsKey(world))
            worldtimelong.remove(world);
    }
}