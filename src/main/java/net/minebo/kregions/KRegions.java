package net.minebo.kregions;

import net.minebo.cobalt.gson.Gson;
import net.minebo.kregions.commands.RegionCommands;
import net.minebo.kregions.completion.FlagCompletionHandler;
import net.minebo.kregions.completion.RegionCompletionHandler;
import lombok.Getter;
import net.minebo.cobalt.acf.ACFCommandController;
import net.minebo.cobalt.acf.ACFManager;
import net.minebo.kregions.manager.FlagManager;
import net.minebo.kregions.manager.RegionManager;
import org.bukkit.plugin.java.*;

public final class KRegions extends JavaPlugin
{
    @Getter public static KRegions instance;

    @Getter public static ACFManager acfManager;
    
    public void onEnable() {
        (KRegions.instance = this).saveDefaultConfig();

        acfManager = new ACFManager(this);

        ACFCommandController.registerCompletion("flags", new FlagCompletionHandler());
        ACFCommandController.registerCompletion("regions", new RegionCompletionHandler());

        ACFCommandController.registerCommand(new RegionCommands());
        Gson.init();

        FlagManager.init();
        RegionManager.init();
    }

}
