package me.paulhobbel.discordrp.common.manager;

import me.paulhobbel.discordrp.common.Log;
import me.paulhobbel.discordrp.common.models.DiscordAsset;
import me.paulhobbel.discordrp.common.config.DiscordRPConfig;
import me.paulhobbel.discordrp.utils.UrlUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DiscordAssetManager {
    static Map<String, DiscordAsset> assets = new HashMap<>();

    public static boolean contains(String key) {
        return assets.containsKey(key);
    }

    public static DiscordAsset get(String key) {
        return assets.get(key);
    }

    public static String getKey(String key) {
        if(assets.containsKey(key))
            return assets.get(key).getName();

        return "default";
    }

    public static void loadAssets() {
        Log.info("Asking dear Discord for available assets");
        assets = new HashMap<>();

        //new Thread(() -> {
            try {
                String url = "https://discordapp.com/api/oauth2/applications/" +
                        (!DiscordRPConfig.applicationId.isEmpty() ? DiscordRPConfig.applicationId : "460129247239864330") +"/assets";
                DiscordAsset[] assets = UrlUtils.fromJsonUrl(url, DiscordAsset[].class);

                for(DiscordAsset asset : assets) {
                    DiscordAssetManager.assets.put(asset.getName(), asset);
                }

                Log.info("Discord gave us " + DiscordAssetManager.assets.size() + " assets to work with hurray!");

            } catch (IOException e) {
                Log.error("An error occured while loading the assets", e);
            }
        //}).start();
    }
}
