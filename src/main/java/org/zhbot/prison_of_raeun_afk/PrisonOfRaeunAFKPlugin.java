package org.zhbot.prison_of_raeun_afk;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.Text;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@PluginDescriptor(
	name = "Prison of Raeun AFK",
	description = "A plugin to help with afking Prison of Raeun agility.",
	tags = {"prison", "of", "raeun", "ræun", "afk"}
)
public class PrisonOfRaeunAFKPlugin extends Plugin
{
	private static final Set<Integer> REGION_IDS = ImmutableSet.of(
			10659,
			10660,
			10915,
			10916,
			10917,
			11172
	);
	private static final Set<Integer> OBSTACLE_IDS = ImmutableSet.of(
			ObjectID.GRAVEYARD_POR_COURSE_START,
			ObjectID.GRAVEYARD_POR_OBSTACLE_1,
			ObjectID.GRAVEYARD_POR_OBSTACLE_2,
			ObjectID.GRAVEYARD_POR_OBSTACLE_3,
			ObjectID.GRAVEYARD_POR_OBSTACLE_4,
			ObjectID.GRAVEYARD_POR_OBSTACLE_5,
			ObjectID.GRAVEYARD_POR_OBSTACLE_6,
			ObjectID.GRAVEYARD_POR_OBSTACLE_7,
			ObjectID.GRAVEYARD_POR_OBSTACLE_8,
			ObjectID.GRAVEYARD_POR_OBSTACLE_9,
			ObjectID.GRAVEYARD_POR_OBSTACLE_10,
			ObjectID.GRAVEYARD_POR_OBSTACLE_11,
			ObjectID.GRAVEYARD_POR_OBSTACLE_12,
			ObjectID.GRAVEYARD_POR_COURSE_END,
			ObjectID.GRAVEYARD_POR_STEPPINGSTONE_CLICK,
			ObjectID.GRAVEYARD_POR_STEPPINGSTONE_CLICK_LARGE
	);

	@Inject
	private Client client;

	@Inject
	private EventBus eventBus;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private PrisonOfRaeunAFKConfig config;

	@Inject
	private PrisonOfRaeunAFKOverlay overlay;

	@Inject
	private PrisonOfRaeunAFKPanel panel;

	@Getter
	private boolean insidePrison;

	@Getter
	private final Set<SteppingStone> steppingStones = new HashSet<>();

	@Getter
	private final Set<GameObject> obstacles = new HashSet<>();

	@Getter
	private GameObject lootChest;

	@Getter
	private boolean lootChestOpen;

	@Override
	protected void startUp() throws Exception
	{
		updateConfig();

		eventBus.register(panel);
	}

	@Override
	protected void shutDown() throws Exception
	{
		clearSteppingStones();
		clearObstacles();
		lootChest = null;

		overlayManager.remove(overlay);

		eventBus.unregister(panel);
		overlayManager.remove(panel);
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (event.getGroup().equals(PrisonOfRaeunAFKConfig.group))
			updateConfig();
	}

	private void updateConfig()
	{
		if (config.overlayPanelEnabled())
			overlayManager.add(panel);
		else
			overlayManager.remove(panel);

		if (config.highlightObstacles() || config.highlightLootChest())
			overlayManager.add(overlay);
		else
			overlayManager.remove(overlay);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() != GameState.LOADING)
			return;

		clearSteppingStones();
		clearObstacles();
		lootChest = null;
	}

	@Provides
	PrisonOfRaeunAFKConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PrisonOfRaeunAFKConfig.class);
	}

	@Subscribe
	public void onGameObjectSpawned(GameObjectSpawned event)
	{
		final var object = event.getGameObject();
		if (object == null)
			return;

		if (SteppingStone.STEPPINGSTONE_IDS.contains(object.getId()))
		{
			addSteppingStoneByObject(object);
		}
		else if (object.getId() == ObjectID.GRAVEYARD_POR_STEPPINGSTONE_CLICK)
		{
			addSteppingStoneByClickbox(object);
		}
		else if (OBSTACLE_IDS.contains(object.getId()))
		{
			obstacles.add(object);
		}
		else if (object.getId() == ObjectID.GRAVEYARD_POR_CHEST_PARENT)
		{
			lootChest = object;
		}
	}

	@Subscribe
	public void onGameObjectDespawned(GameObjectDespawned event)
	{
		final var object = event.getGameObject();
		if (object == null)
			return;

		if (SteppingStone.STEPPINGSTONE_IDS.contains(object.getId()))
		{
			removeSteppingStoneByObject(object);
		}
		else if (object.getId() == ObjectID.GRAVEYARD_POR_STEPPINGSTONE_CLICK)
		{
			removeSteppingStoneByClickbox(object);
		}
		else if (OBSTACLE_IDS.contains(object.getId()))
		{
			obstacles.remove(object);
		}
		else if (object.getId() == ObjectID.GRAVEYARD_POR_CHEST_PARENT && lootChest == object)
		{
			lootChest = null;
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		insidePrison = checkInsidePrison();
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		if (event.getVarbitId() != 16914) // POR_AGILITY_CHEST_OPENED
			return;

		lootChestOpen = event.getValue() != 0;
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
		if (lootChestOpen || !config.preventExitWithoutLoot())
			return;

		if (event.getType() != MenuAction.GAME_OBJECT_FIRST_OPTION.getId())
			return;

		final var action = Text.removeTags(client.macroExpand(event.getOption()));
		if (!action.equals("Jump"))
			return;

		final var entry = event.getMenuEntry();
		if (entry.getIdentifier() != ObjectID.GRAVEYARD_POR_COURSE_END)
			return;

		entry.setDeprioritized(true);
	}

	private boolean checkInsidePrison()
	{
		var player = client.getLocalPlayer();
		if (player == null)
			return false;

		var location = player.getWorldLocation();
		if (location == null)
			return false;

		return REGION_IDS.contains(location.getRegionID());
	}

	private void addSteppingStoneByObject(GameObject object)
	{
		var existing = findSteppingStone(object.getWorldLocation());

		if (existing != null)
		{
			existing.setObject(client, object);
		}
		else
		{
			var steppingStone = new SteppingStone(client, object);
			eventBus.register(steppingStone);
			steppingStones.add(steppingStone);
		}
	}

	private void addSteppingStoneByClickbox(GameObject clickbox)
	{
		var existing = findSteppingStone(clickbox.getWorldLocation());

		if (existing != null)
		{
			existing.setClickbox(clickbox);
		}
		else
		{
			var steppingStone = new SteppingStone(clickbox);
			eventBus.register(steppingStone);
			steppingStones.add(steppingStone);
		}
	}

	private SteppingStone findSteppingStone(WorldPoint location)
	{
		return steppingStones.stream()
				.filter(x ->
						(x.getObject() != null
								&& x.getObject().getWorldLocation().equals(location))
								||
								(x.getClickbox() != null
										&& x.getClickbox().getWorldLocation().equals(location)))
				.findFirst()
				.orElse(null);
	}

	private void removeSteppingStoneByObject(GameObject object)
	{
		steppingStones.removeIf(x ->
		{
			if (x.getObject() != object)
				return false;

			x.clearObject();

			if (x.isEmpty())
			{
				eventBus.unregister(x);
				return true;
			}

			return false;
		});
	}

	private void removeSteppingStoneByClickbox(GameObject clickbox)
	{
		steppingStones.removeIf(x ->
		{
			if (x.getClickbox() != clickbox)
				return false;

			x.setClickbox(null);

			if (x.isEmpty())
			{
				eventBus.unregister(x);
				return true;
			}

			return false;
		});
	}

	private void clearSteppingStones()
	{
		for (final var steppingStone : steppingStones)
			eventBus.unregister(steppingStone);

		steppingStones.clear();
	}

	private void clearObstacles()
	{
		obstacles.clear();
	}
}
