package com.selfcoders.org.minestats;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

class QueryHelper {
    private InfluxDB influxDB;

    QueryHelper(InfluxDB influxDB) {
        this.influxDB = influxDB;
    }

    Map<String, Integer> getCurrentLevel(int limit) {
        Map<String, Integer> perPlayerLevel = new HashMap<>();

        QueryResult queryResult = influxDB.query(new Query("SELECT last(level) AS level FROM playerstats GROUP BY playername"));

        for (QueryResult.Result result : queryResult.getResults()) {
            for (QueryResult.Series series : result.getSeries()) {
                perPlayerLevel.put(series.getTags().get("playername"), ((Number) series.getValues().get(0).get(series.getColumns().indexOf("level"))).intValue());
            }
        }

        // Thanks to https://stackoverflow.com/a/23846961
        return perPlayerLevel.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(limit)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    Map<String, Integer> getHighscore(int limit) {
        Map<String, Integer> perPlayerLevel = new HashMap<>();

        QueryResult queryResult = influxDB.query(new Query("SELECT max(level) AS level FROM playerstats GROUP BY playername"));

        for (QueryResult.Result result : queryResult.getResults()) {
            for (QueryResult.Series series : result.getSeries()) {
                perPlayerLevel.put(series.getTags().get("playername"), ((Number) series.getValues().get(0).get(series.getColumns().indexOf("level"))).intValue());
            }
        }

        // Thanks to https://stackoverflow.com/a/23846961
        return perPlayerLevel.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(limit)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
