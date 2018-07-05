package me.paulhobbel.discordrp.utils;

import com.google.common.base.Stopwatch;
import me.paulhobbel.discordrp.DiscordRP;
import me.paulhobbel.discordrp.api.IDiscordRPPlugin;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.discovery.ASMDataTable;

import java.util.Map;

/**
 * Heavily inspired by HWYLA
 */
public class PluginUtils {
    public static void filterAnnotatedPlugins(Map<Class<?>, IDiscordRPPlugin> plugins) {
        for(ASMDataTable.ASMData data : DiscordRP.plugins) {
            try {
                String requiredMod = (String) data.getAnnotationInfo().getOrDefault("value", "");

                if (Loader.isModLoaded(requiredMod)) {
                    Stopwatch stopwatch = Stopwatch.createStarted();

                    Class<?> asmClass = Class.forName(data.getClassName());
                    if(IDiscordRPPlugin.class.isAssignableFrom(asmClass)) {
                        plugins.put(asmClass, (IDiscordRPPlugin) asmClass.newInstance());
                        Log.debug("Successfully discovered plugin for {} from {} in {}", requiredMod, data.getClassName(), stopwatch.stop());
                    } else {
                        Log.error("{} attempted to register a plugin for {} that did not implement IDiscordRPPlugin", data.getClassName(), requiredMod);
                    }
                } else {
                    Log.error("{} attempted to register a plugin for {}, however this mod is not loaded. Skipping plugin", data.getClassName(), requiredMod);
                }
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                Log.error("Error discovering plugin for class {}: {}", data.getClassName(), e.getLocalizedMessage());
            }
        }
    }
}
