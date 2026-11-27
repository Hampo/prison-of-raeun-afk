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
    STEPPING_STONES("Stepping Stones", 46, new WorldPoint(2752, 10512, 0), new WorldPoint(2733, 10512, 0)),
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

    public enum SteppingStone
    {
        STONE_1(new WorldPoint(2733, 10512, 0), 46),
        STONE_2(new WorldPoint(2735, 10512, 0), 41),
        STONE_3(new WorldPoint(2737, 10512, 0), 36),
        STONE_4(new WorldPoint(2739, 10512, 0), 31),
        STONE_5(new WorldPoint(2741, 10512, 0), 26),
        STONE_6(new WorldPoint(2743, 10512, 0), 21),
        STONE_7(new WorldPoint(2745, 10512, 0), 16),
        STONE_8(new WorldPoint(2747, 10512, 0), 11),
        STONE_9(new WorldPoint(2749, 10512, 0), 6);

        private final WorldPoint location;
        @Getter
        private final int ticksRemaining;

        SteppingStone(WorldPoint location, int ticksRemaining)
        {
            this.location = location;
            this.ticksRemaining = ticksRemaining;
        }

        private static final Map<WorldPoint, SteppingStone> LOCATION_MAP = new HashMap<>();

        static
        {
            for (var stone : values())
                LOCATION_MAP.put(stone.location, stone);
        }

        public static SteppingStone getByLocation(WorldPoint location)
        {
            return LOCATION_MAP.get(location);
        }

        public static int getTotalTicks()
        {
            return STONE_1.ticksRemaining;
        }
    }
}
