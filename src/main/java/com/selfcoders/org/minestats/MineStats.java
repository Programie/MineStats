package com.selfcoders.org.minestats;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;

public final class MineStats extends JavaPlugin {
    private InfluxDB influxDB;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        FileConfiguration config = getConfig();
        ConfigurationSection influxDbConfig = config.getConfigurationSection("influxdb");

        influxDB = InfluxDBFactory.connect(influxDbConfig.getString("url"), influxDbConfig.getString("username"), influxDbConfig.getString("password"));
        influxDB.setDatabase(influxDbConfig.getString("database"));
        influxDB.enableBatch();

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
}
