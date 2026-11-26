package org.zhbot.prison_of_raeun_afk;

import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.util.ColorUtil;

import javax.inject.Inject;
import java.awt.*;

public class PrisonOfRaeunAFKOverlay extends Overlay {
    private final Client client;
    private final PrisonOfRaeunAFKPlugin plugin;
    private final PrisonOfRaeunAFKConfig config;

    @Inject
    private PrisonOfRaeunAFKOverlay(Client client, PrisonOfRaeunAFKPlugin plugin, PrisonOfRaeunAFKConfig config)
    {
        this.client = client;
        this.plugin = plugin;
        this.config = config;

        setLayer(OverlayLayer.ABOVE_SCENE);
        setPosition(OverlayPosition.DYNAMIC);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!plugin.isInsidePrison())
            return null;

        if (config.highlightObstacles())
        {
            final var obstacleColour = config.obstacleColour();
            for (final var obstacle : plugin.getObstacles())
                renderObject(graphics, obstacle, obstacleColour);

            if (config.highlightSafeSteppingStones())
            {
                final var steppingStones = plugin.getSteppingStones();
                if (!steppingStones.isEmpty())
                {
                    final var safeColour = config.safeSteppingStoneColour();
                    for (final var steppingStone : steppingStones)
                    {
                        if (!steppingStone.isSafe())
                            continue;

                        var clickbox = steppingStone.getClickbox();
                        if (clickbox == null)
                            continue;

                        renderObject(graphics, clickbox, safeColour);
                    }
                }
            }
        }

        if (config.highlightLootChest() && plugin.getLootChest() != null && !plugin.isLootChestOpen())
            renderObject(graphics, plugin.getLootChest(), config.lootChestColour());

        return null;
    }

    private void renderObject(Graphics2D graphics, GameObject gameObject, Color color)
    {
        var area = gameObject.getClickbox();
        var mousePosition = client.getMouseCanvasPosition();

        var borderColour = ColorUtil.colorWithAlpha(color, 255);
        OverlayUtil.renderHoverableArea(graphics, area, mousePosition, color, borderColour, borderColour.darker());
    }
}
