package net.minebo.kregions.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.minebo.kregions.manager.FlagManager;
import net.minebo.kregions.manager.RegionManager;
import net.minebo.kregions.model.Region;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.*;

@CommandAlias("regions|region|rg")
public class RegionCommands extends BaseCommand {

    @Default
    @CatchUnknown
    public void onHelpCommand(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("create")
    @CommandPermission("basic.admin")
    @Syntax("<name>")
    public void onRegionCreateCommand(final Player sender, String name) {

        if(RegionManager.getRegionByName(name) != null) {
            sender.sendMessage(ChatColor.RED + "That region already exists!");
            return;
        }

        new Region(name, sender.getLocation(), sender.getLocation());
        sender.sendMessage(ChatColor.YELLOW + "Created a new region, " + ChatColor.WHITE + name + ChatColor.YELLOW + ".");
    }

    @Subcommand("delete")
    @CommandPermission("basic.admin")
    @Syntax("<name>")
    @CommandCompletion("@regions")
    public void onRegionDeleteCommand(final Player sender, String name) {

        if(RegionManager.getRegionByName(name) == null) {
            sender.sendMessage(ChatColor.RED + "That region doesn't exist!");
            return;
        }

        RegionManager.regions.remove(RegionManager.getRegionByName(name));
        sender.sendMessage(ChatColor.YELLOW + "Deleted a region, " + ChatColor.WHITE + name + ChatColor.YELLOW + ".");
        RegionManager.saveRegions();
    }

    @Subcommand("pos1")
    @CommandPermission("basic.admin")
    @Syntax("<name>")
    @CommandCompletion("@regions @flags")
    public void onRegionSetPos1Command(final Player sender, String name) {

        if(RegionManager.getRegionByName(name) == null) {
            sender.sendMessage(ChatColor.RED + "That region doesn't exist!");
            return;
        }

        sender.sendMessage(ChatColor.YELLOW + "Set " + ChatColor.WHITE + name + ChatColor.YELLOW + "'s 1st position.");
        RegionManager.getRegionByName(name).setX1(sender.getLocation().getBlockX());
        RegionManager.getRegionByName(name).setY1(0);
        RegionManager.getRegionByName(name).setZ1(sender.getLocation().getBlockZ());
        RegionManager.saveRegions();
    }

    @Subcommand("pos2")
    @CommandPermission("basic.admin")
    @Syntax("<name>")
    @CommandCompletion("@regions @flags")
    public void onRegionSetPos2Command(final Player sender, String name) {

        if(RegionManager.getRegionByName(name) == null) {
            sender.sendMessage(ChatColor.RED + "That region doesn't exist!");
            return;
        }

        sender.sendMessage(ChatColor.YELLOW + "Set " + ChatColor.WHITE + name + ChatColor.YELLOW + "'s 2nd position.");
        RegionManager.getRegionByName(name).setX2(sender.getLocation().getBlockX());
        RegionManager.getRegionByName(name).setY2(999);
        RegionManager.getRegionByName(name).setZ2(sender.getLocation().getBlockZ());
        RegionManager.saveRegions();
    }

    @Subcommand("flag add")
    @CommandPermission("basic.admin")
    @Syntax("<name> <flag>")
    @CommandCompletion("@regions @flags")
    public void onRegionFlagAddCommand(final Player sender, String name, String flag) {

        if(RegionManager.getRegionByName(name) == null) {
            sender.sendMessage(ChatColor.RED + "That region doesn't exist!");
            return;
        }

        if(RegionManager.getRegionByName(name).containsFlag(FlagManager.getFlagByName(flag))) {
            sender.sendMessage(ChatColor.RED + "That region already has that flag.");
            return;
        }

        sender.sendMessage(ChatColor.YELLOW + "Added the flag " + ChatColor.WHITE + flag + ChatColor.YELLOW + " to " + ChatColor.WHITE + name + ChatColor.YELLOW + ".");
        RegionManager.getRegionByName(name).addFlag(FlagManager.getFlagByName(flag));
        RegionManager.saveRegions();
    }

    @Subcommand("flag remove")
    @CommandPermission("basic.admin")
    @Syntax("<name> <flag>")
    @CommandCompletion("@regions @flags")
    public void onRegionFlagRemoveCommand(final Player sender, String name, String flag) {

        if(RegionManager.getRegionByName(name) == null) {
            sender.sendMessage(ChatColor.RED + "That region doesn't exist!");
            return;
        }

        if(!RegionManager.getRegionByName(name).containsFlag(FlagManager.getFlagByName(flag))) {
            sender.sendMessage(ChatColor.RED + "That region doesn't have that flag.");
            return;
        }

        sender.sendMessage(ChatColor.YELLOW + "Removed the flag " + ChatColor.WHITE + flag + ChatColor.YELLOW + " from " + ChatColor.WHITE + name + ChatColor.YELLOW + ".");
        RegionManager.getRegionByName(name).removeFlag(FlagManager.getFlagByName(flag));
        RegionManager.saveRegions();
    }

    @Subcommand("rename")
    @CommandPermission("basic.admin")
    @Syntax("<name> <newname>")
    @CommandCompletion("@regions")
    public void onRegionRenameCommand(final Player sender, String name, String newname) {

        if(RegionManager.getRegionByName(name) == null) {
            sender.sendMessage(ChatColor.RED + "That region doesn't exist!");
            return;
        }

        if(RegionManager.getRegionByName(newname) != null) {
            sender.sendMessage(ChatColor.RED + "That region already exists!");
            return;
        }

        RegionManager.getRegionByName(name).setName(newname);
        sender.sendMessage(ChatColor.YELLOW + "Changed the name to " + ChatColor.WHITE + name + ChatColor.YELLOW + ".");
        RegionManager.saveRegions();
    }

    @Subcommand("list")
    @CommandPermission("basic.admin")
    public void onRegionListCommand(final Player sender) {
        sender.sendMessage(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "----------------------");
        if (RegionManager.getRegions().isEmpty()) {
            sender.sendMessage(ChatColor.RED + "No regions exist!");
        }
        RegionManager.getRegions().forEach(r -> {
            sender.sendMessage(ChatColor.GOLD + r.getName() + ":");
            if(!r.getFlags().isEmpty()) {
                r.getFlags().forEach(f -> {
                    sender.sendMessage(" * " + ChatColor.YELLOW + f.name);
                });
            } else {
                sender.sendMessage(" * " + ChatColor.RED + "No flags.");
            }
        });
        sender.sendMessage(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "----------------------");
    }

    @Subcommand("identify")
    public void onRegionIdentifyCommand(final Player sender) {
        Region region = RegionManager.getRegionByLocation(sender.getLocation());

        if(region == null) {
            sender.sendMessage(ChatColor.YELLOW + "You are currently in: " + ChatColor.RED + "No Region");
            return;
        }

        sender.sendMessage(ChatColor.YELLOW + "You are currently in: " + ChatColor.GOLD + region.getName());

    }

}
