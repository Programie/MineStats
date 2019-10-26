package com.selfcoders.org.minestats;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Collector implements Runnable {
    private final InfluxDB influxDB;
    private final Server server;

    Collector(InfluxDB influxDB, Server server) {
        this.influxDB = influxDB;
        this.server = server;
    }

    private Point.Builder getMeasurement(Player player) {
        return Point.measurement("playerstats")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .tag("uuid", player.getUniqueId().toString())
                .tag("playername", player.getName());
    }

    @Override
    public void run() {
        for (Player player : server.getOnlinePlayers()) {
            influxDB.write(getMeasurement(player)
                    .addField("xp", player.getTotalExperience())
                    .addField("level", player.getLevel())
                    .addField("health", player.getHealth())
                    .addField("food", player.getFoodLevel())
                    .build());
        }
    }
}
