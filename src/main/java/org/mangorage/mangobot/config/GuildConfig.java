package org.mangorage.mangobot.config;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;

import org.mangorage.basicutils.config.Config;
import org.mangorage.basicutils.config.ConfigSetting;
import org.mangorage.basicutils.config.ISetting;
import org.mangorage.mangobot.MangoBotPlugin;
import org.mangorage.mangobot.modules.github.GHIssueStatus;
import org.mangorage.mangobot.modules.github.GHPRStatus;


public class GuildConfig extends Config {

	public static HashMap<String, GuildConfig> configs = new HashMap<String, GuildConfig>();
	public ISetting<String> GIT_REPOS_PR_SCANNED = ConfigSetting.create(this, "GIT_REPOS_PR_SCANNED", "MinecraftForge/MinecraftForge,MangoRageBot/MangoBot,MangoRageBot/MangoBotPlugin,mikumikudanceminecraftmoddingdiscord/PMXMC");
	public ISetting<String> GIT_REPOS_PR_SCANNED_CHANNELID = ConfigSetting.create(this, "GIT_REPOS_PR_SCANNED_CHANNELID", "empty");
	public ISetting<String> GIT_REPOS_ISSUE_SCANNED = ConfigSetting.create(this, "GIT_REPOS_ISSUE_SCANNED", "MinecraftForge/MinecraftForge,MangoRageBot/MangoBot,MangoRageBot/MangoBotPlugin,mikumikudanceminecraftmoddingdiscord/PMXMC");
	public ISetting<String> GIT_REPOS_ISSUE_SCANNED_CHANNELID = ConfigSetting.create(this, "GIT_REPOS_ISSUE_SCANNED_CHANNELID", "empty");

	private GuildConfig(String guildID) {
		super(Path.of("config/" + guildID + "/config.env"));
		configs.put(guildID, this);

		if (!GIT_REPOS_PR_SCANNED_CHANNELID.get().equals("empty")) {
			GHPRStatus.indexed_channels.add(GIT_REPOS_PR_SCANNED_CHANNELID.get());
		}

		if (!GIT_REPOS_ISSUE_SCANNED_CHANNELID.get().equals("empty")) {
			GHIssueStatus.indexed_channels.add(GIT_REPOS_ISSUE_SCANNED_CHANNELID.get());
		}

	}

	public static GuildConfig guildsConfig(String guildID) {
		if (configs.containsKey(guildID)) {
			return configs.get(guildID);
		}

		Path root = MangoBotPlugin.CONFIG.getFile().getParent();
		File path = new File(root + "/config/" + guildID + "/config.env");
		path.getParentFile().mkdirs();
		return new GuildConfig(guildID);
	}

	public static void loadServerConfigs() {
		Path root = MangoBotPlugin.CONFIG.getFile().getParent();
		File path = new File(root + "/config/");

		if (path.exists()) {
			File[] listOfFiles = path.listFiles();

			if (listOfFiles != null) {
				for (File file: listOfFiles) {
					if (file.isDirectory()) {
						guildsConfig(file.getName());
					}
				}
			}
		} else {
			System.out.println("No Guild Configs!");
		}

	}

}