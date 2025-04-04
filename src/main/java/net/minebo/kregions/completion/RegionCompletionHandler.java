package net.minebo.kregions.completion;

import co.aikar.commands.CommandCompletionContext;
import co.aikar.commands.CommandCompletions;
import co.aikar.commands.InvalidCommandArgument;
import net.minebo.kregions.manager.RegionManager;
import net.minebo.kregions.model.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RegionCompletionHandler  implements CommandCompletions.CommandCompletionHandler {
    @Override
    public Collection<String> getCompletions(CommandCompletionContext context) throws InvalidCommandArgument {
        List<String> completions = new ArrayList<>();

        for(Region region : RegionManager.getRegions()) {
            completions.add(region.getName());
        }

        return completions;
    }
}
