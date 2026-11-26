package org.zhbot.prison_of_raeun_afk;

import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import net.runelite.api.coords.WorldPoint;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
public enum Obstacle {
    SHIPWRECK("Shipwreck", 19, new WorldPoint(2727, 10505, 0), new WorldPoint(2727, 10496, 0)),
    WALL_1("Wall 1", 41, new WorldPoint(2760, 10542, 0), new WorldPoint(2760, 10514, 0)),
    WALL_2("Wall 2", 39, new WorldPoint(2719, 10561, 0), new WorldPoint(2747, 10561, 0)),
    SHIPWRECKS("Shipwrecks", 41, new WorldPoint(2691, 10550, 0), new WorldPoint(2705, 10553, 0)),
    WALL_3("Wall 3", 53, new WorldPoint(2680, 10508, 0), new WorldPoint(2680, 10547, 0)),
    END("End", 59, new WorldPoint(2714, 10490, 0), new WorldPoint(2691, 10506, 0)),
    CHEST("Chest", 1, new WorldPoint(2683, 10503, 0), new WorldPoint(2683, 10503, 0));

    private final String name;
    private final Set<WorldPoint> startPoints;
    private final WorldPoint endPoint;
    private final int ticks;

    Obstacle(String name, int ticks, WorldPoint endPoint, WorldPoint... startPoints)
    {
        this.name = name;
        this.ticks = ticks;
        this.endPoint = endPoint;
        this.startPoints = ImmutableSet.copyOf(startPoints);
    }

    private static final Map<WorldPoint, Obstacle> START_POINT_MAP = new HashMap<>();
    private static final Map<WorldPoint, Obstacle> END_POINT_MAP = new HashMap<>();

    static
    {
        for (var obstacle : values())
        {
            for (var startPoint : obstacle.startPoints)
                START_POINT_MAP.put(startPoint, obstacle);

            END_POINT_MAP.put(obstacle.endPoint, obstacle);
        }
    }

    public static Obstacle getByStartPoint(WorldPoint point)
    {
        return START_POINT_MAP.get(point);
    }

    public static Obstacle getByEndPoint(WorldPoint point)
    {
        return END_POINT_MAP.get(point);
    }
}
