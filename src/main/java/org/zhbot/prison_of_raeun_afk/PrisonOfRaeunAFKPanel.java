package org.zhbot.prison_of_raeun_afk;

import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.gameval.VarbitID;
import net.runelite.client.Notifier;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ProgressBarComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;

public class PrisonOfRaeunAFKPanel extends OverlayPanel {
    private static final long MILLIS_PER_TICK = 600;

    private final Client client;
    private final Notifier notifier;
    private final PrisonOfRaeunAFKPlugin plugin;
    private final PrisonOfRaeunAFKConfig config;
    private final ProgressBarComponent progressBar = new ProgressBarComponent();

    private boolean wasLoggedOut = false;
    private int skipBusy = 0;
    private Obstacle obstacle = null;
    private int startTick = -1;
    private long startMS = -1;

    @Inject
    private PrisonOfRaeunAFKPanel(Client client, Notifier notifier, PrisonOfRaeunAFKPlugin plugin, PrisonOfRaeunAFKConfig config)
    {
        this.client = client;
        this.notifier = notifier;
        this.plugin = plugin;
        this.config = config;

        setPosition(OverlayPosition.TOP_LEFT);
        progressBar.setMinimum(0);
        progressBar.setLabelDisplayMode(ProgressBarComponent.LabelDisplayMode.TEXT_ONLY);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (obstacle == null)
            return null;

        panelComponent.getChildren().clear();

        if (config.overlayPanelTitle())
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text(obstacle.getName())
                    .color(Color.GREEN)
                    .build());

        switch (config.overlayPanelMode())
        {
            case TICKS:
                var ticks = client.getTickCount() - startTick - 1;
                var ticksLeft = obstacle.getTicks() - ticks;

                progressBar.setMaximum(obstacle.getTicks());
                progressBar.setValue(Math.min(ticks, obstacle.getTicks()));
                progressBar.setCenterLabel(ticksLeft + (ticksLeft == 1 ? " tick" : " ticks"));

                break;
            case SECONDS:
                var totalMillis = obstacle.getTicks() * MILLIS_PER_TICK;
                var millis = System.currentTimeMillis() - startMS;
                var millisLeft = totalMillis - millis;

                progressBar.setMaximum(totalMillis);
                progressBar.setValue(Math.min(millis, totalMillis));
                progressBar.setCenterLabel(millisLeft > 0 ? String.format("%.1f seconds", millisLeft / 1000.0) : "Finishing...");
                break;
        }

        panelComponent.getChildren().add(progressBar);

        return super.render(graphics);
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event)
    {
        switch (event.getGameState())
        {
            case HOPPING:
            case LOGIN_SCREEN:
            case LOGGING_IN:
                wasLoggedOut = true;
                break;
            case LOGGED_IN:
                if (wasLoggedOut)
                {
                    skipBusy = 2;
                    wasLoggedOut = false;
                }
                break;
            case LOADING:
                if (wasLoggedOut)
                    skipBusy = 2;
                break;
        }
    }

    @Subscribe
    public void onVarbitChanged(VarbitChanged event)
    {
        if (event.getVarbitId() != VarbitID.BUSY)
            return;

        if (skipBusy > 0)
        {
            skipBusy--;
            return;
        }

        if (!plugin.isInsidePrison())
            return;

        var player = client.getLocalPlayer();
        if (player == null)
            return;

        var location = player.getWorldLocation();
        if (location == null)
            return;

        if (event.getValue() == 0)
        {
            if (config.debug() && obstacle != null)
            {
                var ticksTaken = client.getTickCount() - startTick;
                if (ticksTaken != obstacle.getTicks())
                    client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Mismatched tick count on " + obstacle.getName() + ". Expected " + obstacle.getTicks() + ", got " + ticksTaken, null);
            }

            setObstacle(null);

            var obstacle = Obstacle.getByEndPoint(location);
            if (obstacle == null)
            {
                if (config.debug())
                    client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Unknown end location. X=" + location.getX() + ", Y=" + location.getY(), null);
                return;
            }

            if (config.enabledObstacles().contains(obstacle))
            	notifier.notify(config.obstacleCompleteNotifications(), "Obstacle \"" + obstacle.getName() + "\" complete");
        }
        else
        {
            var obstacle = Obstacle.getByStartPoint(location);
            if (obstacle == null)
            {
                if (config.debug())
                    client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Unknown start location. X=" + location.getX() + ", Y=" + location.getY(), null);
                return;
            }

            if (obstacle.getTicks() > 1)
                setObstacle(obstacle);
        }
    }

    private void setObstacle(Obstacle obstacle)
    {
        this.obstacle = obstacle;
        if (obstacle != null)
        {
            this.startTick = client.getTickCount();
            this.startMS = System.currentTimeMillis();
        }
        else
        {
            this.startTick = -1;
            this.startMS = -1;
        }
    }
}
