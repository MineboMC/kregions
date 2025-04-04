package net.minebo.kregions.completion;

import co.aikar.commands.CommandCompletionContext;
import co.aikar.commands.CommandCompletions;
import co.aikar.commands.InvalidCommandArgument;
import net.minebo.kregions.manager.FlagManager;
import net.minebo.kregions.model.Flag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FlagCompletionHandler implements CommandCompletions.CommandCompletionHandler {
    @Override
    public Collection<String> getCompletions(CommandCompletionContext context) throws InvalidCommandArgument {
        List<String> completions = new ArrayList<>();

        for(Flag flag : FlagManager.flags) {
            completions.add(flag.name);
        }

        return completions;
    }
}
