# MineStats

A Minecraft Bukkit plugin which sends statistics to InfluxDB.

[![actions status](https://github.com/Programie/MineStats/actions/workflows/build.yml/badge.svg)](https://github.com/Programie/MineStats/actions/workflows/build.yml)
[![download from GitHub](https://img.shields.io/badge/download-Releases-blue?logo=github)](https://github.com/Programie/MineStats/releases/latest)
[![view on Website](https://img.shields.io/badge/view-Website-blue)](https://selfcoders.com/projects/minestats)

MineStats allows your Minecraft Server to collect some useful statistics about the players on it.

For storage, it uses an InfluxDB instance. So you might also use applications like [Grafana](https://grafana.com) to visualize the collected statistics.

## Permissions

* `minestats.stats` - Allow to show current levels and highscore (default: `true`)

## Commands

You can use the `/stats` command to display the current levels and highscore.

## Collected data

The following data is collected per player:

* XP (total earned XP points since the last death)
* Current level
* Health
* Food/Hunger

The player name as well as the UUID of the player is added to the collected metrics.

By default, the data is collected every 1200 ticks (about every 60 seconds), but it can be changed in the config file using the `interval` option.

## Build

You can build the project in the following 2 steps:

 * Check out the repository
 * Build the jar file using maven: *mvn clean package*

**Note:** JDK 1.8 and Maven is required to build the project!
