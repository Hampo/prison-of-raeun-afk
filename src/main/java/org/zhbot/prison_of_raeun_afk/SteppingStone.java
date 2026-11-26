package org.zhbot.prison_of_raeun_afk;

import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.eventbus.Subscribe;

import java.util.Set;

public class SteppingStone {
    public static final Set<Integer> STEPPINGSTONE_IDS = ImmutableSet.of(
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_0_WEST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_0_CENTER,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_0_EAST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_1_WEST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_1_CENTER,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_1_EAST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_2_WEST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_2_CENTER,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_2_EAST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_3_WEST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_3_CENTER,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_3_EAST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_4_WEST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_4_CENTER,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_4_EAST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_5_WEST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_5_CENTER,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_5_EAST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_6_WEST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_6_CENTER,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_6_EAST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_7_WEST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_7_CENTER,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_7_EAST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_8_WEST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_8_CENTER,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_8_EAST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_9_WEST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_9_CENTER,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_9_EAST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_10_WEST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_10_CENTER,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_10_EAST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_11_WEST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_11_CENTER,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_11_EAST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_12_WEST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_12_CENTER,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_12_EAST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_13_WEST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_13_CENTER,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_13_EAST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_14_WEST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_14_CENTER,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_14_EAST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_15_WEST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_15_CENTER,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_15_EAST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_16_WEST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_16_CENTER,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_16_EAST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_17_WEST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_17_CENTER,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_17_EAST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_18_WEST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_18_CENTER,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_18_EAST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_19_WEST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_19_CENTER,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_19_EAST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_20_WEST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_20_CENTER,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_20_EAST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_21_WEST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_21_CENTER,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_21_EAST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_22_WEST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_22_CENTER,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_22_EAST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_23_WEST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_23_CENTER,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_23_EAST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_24_WEST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_24_CENTER,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_24_EAST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_25_WEST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_25_CENTER,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_25_EAST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_26_WEST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_26_CENTER,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_26_EAST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_27_WEST,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_27_CENTER,
            ObjectID.GRAVEYARD_POR_STEPPINGSTONE_27_EAST
    );

    @Getter
    private GameObject object;
    @Setter
    @Getter
    private GameObject clickbox;

    private ObjectComposition objectComposition;

    private int varbitID;

    @Getter
    private boolean safe;

    private boolean initialized;

    public SteppingStone(final Client client, final GameObject object)
    {
        this.object = object;
        this.clickbox = null;

        objectComposition = client.getObjectDefinition(object.getId());
        varbitID = objectComposition.getVarbitId();
    }

    public SteppingStone(final GameObject clickbox)
    {
        this.object = null;
        this.clickbox = clickbox;
        this.objectComposition = null;
        this.varbitID = -1;
    }

    public void setObject(final Client client, final GameObject object)
    {
        this.object = object;

        objectComposition = client.getObjectDefinition(object.getId());
        varbitID = objectComposition.getVarbitId();

        initialized = false;
    }

    public void clearObject()
    {
        this.object = null;
        this.objectComposition = null;
        this.varbitID = -1;
        initialized = false;
        safe = false;
    }

    public boolean isEmpty()
    {
        return object == null && clickbox == null;
    }

    @Subscribe
    public void onGameTick(GameTick event)
    {
        if (initialized)
            return;

        if (object == null)
            return;

        initialized = true;
        checkSafe();
    }

    @Subscribe
    public void onVarbitChanged(VarbitChanged event)
    {
        if (!initialized || event.getVarbitId() != varbitID)
            return;

        checkSafe();
    }

    private void checkSafe()
    {
        if (objectComposition == null)
            return;

        var impostorIds = objectComposition.getImpostorIds();
        if (impostorIds == null)
        {
            safe = objectComposition.getId() == ObjectID.GRAVEYARD_POR_STEPPINGSTONE_SAFE;
            return;
        }

        var impostor = objectComposition.getImpostor();
        safe = impostor != null && impostor.getId() == ObjectID.GRAVEYARD_POR_STEPPINGSTONE_SAFE;
    }
}
