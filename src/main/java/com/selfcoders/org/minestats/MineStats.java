package com.selfcoders.org.minestats;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public final class MineStats extends JavaPlugin {
    private InfluxDB influxDB;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        FileConfiguration config = getConfig();
        ConfigurationSection influxDbConfig = config.getConfigurationSection("influxdb");

        influxDB = InfluxDBFactory.connect(influxDbConfig.getString("url"), influxDbConfig.getString("username"), influxDbConfig.getString("password"));
        influxDB.setDatabase(influxDbConfig.getString("database"));

        long interval = config.getLong("interval", 1200);

        getServer().getScheduler().runTaskTimerAsynchronously(this, new Collector(influxDB, getServer()), interval, interval);
    }

    @Override
    public void onDisable() {
        if (influxDB != null) {
            getLogger().info("Closing connection to InfluxDB");

            influxDB.close();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("stats")) {
            return false;
        }

        QueryHelper queryHelper = new QueryHelper(influxDB);

        ArrayList<String> messages;
        messages = new ArrayList<>();

        messages.add(ChatColor.YELLOW + "----------" + ChatColor.WHITE + " Level Highscore " + ChatColor.YELLOW + "----------");

        Set<Map.Entry<String, Integer>> entrySet = queryHelper.getHighscore(10).entrySet();
        int padLength = String.valueOf(entrySet.size()).length();
        int entryNumber = 1;
        for (Map.Entry<String, Integer> entry : entrySet) {
            messages.add(ChatColor.WHITE + StringUtils.leftPad(String.valueOf(entryNumber), padLength) + " " + ChatColor.AQUA + entry.getKey() + ": " + ChatColor.DARK_GREEN + entry.getValue());
            entryNumber++;
        }

        messages.add(ChatColor.YELLOW + "----------" + ChatColor.WHITE + " Current Levels " + ChatColor.YELLOW + "----------");

        for (Map.Entry<String, Integer> entry : queryHelper.getCurrentLevel(10).entrySet()) {
            messages.add(ChatColor.AQUA + entry.getKey() + ": " + ChatColor.DARK_GREEN + entry.getValue());
        }

        sender.sendMessage(String.join("\n", messages));

        return true;
    }
}
