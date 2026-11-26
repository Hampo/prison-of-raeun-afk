package org.zhbot.prison_of_raeun_afk;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class PrisonOfRaeunAFKPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(PrisonOfRaeunAFKPlugin.class);
		RuneLite.main(args);
	}
}