package org.zhbot.prison_of_raeun_afk;

import net.runelite.client.config.*;

import java.awt.*;
import java.util.EnumSet;
import java.util.Set;

@ConfigGroup(PrisonOfRaeunAFKConfig.group)
public interface PrisonOfRaeunAFKConfig extends Config
{
	String group = "prison-of-raeun";

	@ConfigSection(
			name = "Notifications",
			description = "Configure notifications",
			position = 0
	)
	String notificationsSection = "notificationsSection";

	@ConfigItem(
			keyName = "obstacleCompleteNotifications",
			name = "Obstacle Complete",
			description = "Configuration for obstacle complete notifications",
			section = notificationsSection,
			position = 0
	)
	default Notification obstacleCompleteNotifications()
	{
		return Notification.ON;
	}

	@ConfigItem(
			keyName = "enabledObstacles",
			name = "Enabled",
			description = "Which obstacles to give a notification for.",
			section = notificationsSection,
			position = 1
	)
	default Set<Obstacle> enabledObstacles()
	{
		var values = EnumSet.allOf(Obstacle.class);
		values.removeIf(x -> x.getTicks() == 1);
		return values;
	}

	@ConfigSection(
			name = "Overlay panel",
			description = "Configure the overlay panel",
			position = 1
	)
	String overlayPanelSection = "overlayPanelSection";

	@ConfigItem(
			keyName = "overlayPanelEnabled",
			name = "Enabled",
			description = "If the overlay panel should be shown",
			section = overlayPanelSection,
			position = 0
	)
	default boolean overlayPanelEnabled()
	{
		return true;
	}

	@ConfigItem(
			keyName = "overlayPanelTitle",
			name = "Show Title",
			description = "If the overlay panel should have a title showing the current obstacle name",
			section = overlayPanelSection,
			position = 1
	)
	default boolean overlayPanelTitle()
	{
		return true;
	}

	@ConfigItem(
			keyName = "overlayPanelMode",
			name = "Duration Format",
			description = "What format to show an obstacle's duration",
			section = overlayPanelSection,
			position = 1
	)
	default DurationMode overlayPanelMode()
	{
		return DurationMode.TICKS;
	}

	@ConfigSection(
			name = "Obstacles",
			description = "Configure obstacle settings",
			position = 2
	)
	String obstaclesSection = "obstaclesSection";

	@ConfigItem(
			keyName = "highlightObstacles",
			name = "Highlight Obstacles",
			description = "Highlight course obstacles stones.",
			section = obstaclesSection,
			position = 0
	)
	default boolean highlightObstacles()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
			keyName = "obstacleColour",
			name = "Obstacle Colour",
			description = "The colour for course obstacles.",
			section = obstaclesSection,
			position = 1
	)
	default Color obstacleColour()
	{
		return new Color(0, 255, 0, 50);
	}

	@ConfigItem(
			keyName = "highlightSafeSteppingStones",
			name = "Highlight Safe Stepping Stones",
			description = "Highlight safe stepping stones.",
			section = obstaclesSection,
			position = 2
	)
	default boolean highlightSafeSteppingStones()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
			keyName = "safeSteppingStoneColour",
			name = "Stepping Stone Colour",
			description = "The colour for safe stepping stones.",
			section = obstaclesSection,
			position = 3
	)
	default Color safeSteppingStoneColour()
	{
		return new Color(0, 255, 0, 50);
	}

	@ConfigItem(
			keyName = "preventUnsafeSteppingStoneJump",
			name = "Prevent Unsafe Jumps",
			description = "Deprioritizes the jump option on unsafe stepping stones.",
			section = obstaclesSection,
			position = 4
	)
	default boolean preventUnsafeSteppingStoneJump()
	{
		return true;
	}

	@ConfigSection(
			name = "Loot Chest",
			description = "Configure the loot chest",
			position = 3
	)
	String lootChestSection = "lootChestSection";

	@ConfigItem(
			keyName = "highlightLootChest",
			name = "Highlight Loot Chest",
			description = "Highlight the loot chest.",
			section = lootChestSection,
			position = 0
	)
	default boolean highlightLootChest()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
			keyName = "lootChestColour",
			name = "Loot Chest Colour",
			description = "The colour for course obstacles.",
			section = lootChestSection,
			position = 1
	)
	default Color lootChestColour()
	{
		return new Color(255, 0, 255, 50);
	}

	@ConfigItem(
			keyName = "preventExitWithoutLoot",
			name = "Prevent Exit Without Loot",
			description = "Deprioritizes the course end obstacle when the chest isn't looted.",
			section = lootChestSection,
			position = 3
	)
	default boolean preventExitWithoutLoot()
	{
		return true;
	}

	@ConfigSection(
			name = "Chat Messages",
			description = "Configure chat messages",
			position = 4
	)
	String chatMessagesSection = "chatMessagesSection";

	@ConfigItem(
			keyName = "hideLapCount",
			name = "Hide Lap Count",
			description = "Hide the lap count game message.",
			section = chatMessagesSection,
			position = 0
	)
	default boolean hideLapCount()
	{
		return false;
	}

	@ConfigItem(
			keyName = "hideLapDuration",
			name = "Hide Lap Duration",
			description = "Hide the lap duration game message.",
			section = chatMessagesSection,
			position = 1
	)
	default boolean hideLapDuration()
	{
		return false;
	}

	@ConfigSection(
			name = "Debug",
			description = "Debug",
			closedByDefault = true,
			position = 5
	)
	String debugSection = "debugSection";

	@ConfigItem(
			keyName = "debug",
			name = "Debug",
			description = "Enable debug mode",
			section = debugSection,
			position = 0
	)
	default boolean debug()
	{
		return false;
	}
}
