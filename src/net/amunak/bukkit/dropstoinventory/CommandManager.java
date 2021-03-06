package net.amunak.bukkit.dropstoinventory;

/**
 * Copyright 2014 Eric Coan
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Creator: Revolution
 * Date: 7/9/14.
 * Project: DropsToInventory
 * Usage: Manages Commands
 */
public class CommandManager implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("dti")) {
            try {
                if(args.length == 0) {
                    if (!(sender instanceof Player))
                        return false;
                    Player senderr = (Player) sender;
                    if (senderr.hasPermission("dti.help")) {
                        DropsToInventory.sendMessage(senderr, "Drops to Inventory. Use /dti help for help.");
                    }
                    return true;
                }
            }catch(NullPointerException e) {
                if(!(sender instanceof  Player))
                    return false;
                Player senderr = (Player) sender;
                if(senderr.hasPermission("dti.help")) {
                    DropsToInventory.sendMessage(senderr, "Drops to Inventory. Use /dti help for help.");
                }
                return true;
            }
            if(args.length == 2 && (args[1].equalsIgnoreCase("off") || args[1].equalsIgnoreCase("on"))) {
                if(sender instanceof Player) {
                    Player senderr = (Player) sender;
                    if(!(senderr.hasPermission("dti.change.others"))) {
                        return false;
                    }
                }
                Player p = Bukkit.getPlayer(args[0]);
                if(p != null) {
                    String UUID = p.getUniqueId().toString();
                    List<String> currentBans = DropsToInventory.getInstance().getConfig().getStringList
                            ("blacklistedPlayers");
                    if(args[1].equalsIgnoreCase("off")) {
                        if (!(currentBans.contains(UUID)))
                            currentBans.add(UUID);
                        if(!(BreakListener.playersTurnedOff.contains(UUID)))
                            BreakListener.playersTurnedOff.add(UUID);
                    }else {
                        if(currentBans.contains(UUID))
                            currentBans.remove(UUID);
                        if((BreakListener.playersTurnedOff.contains(UUID)))
                            BreakListener.playersTurnedOff.remove(UUID);
                    }
                    DropsToInventory.getInstance().getConfig().set("blacklistedPlayers", currentBans);
                }
            }else {
                if(!(sender instanceof Player)) return false;

                Player senderr = (Player) sender;
                if(args[0].equalsIgnoreCase("help") && senderr.hasPermission("dti.help")) {
                    DropsToInventory.sendMessage(senderr, "Help page: 1 of 1");
                    DropsToInventory.sendMessage(senderr, "/dti help");
                    if(senderr.hasPermission("dti.change.self"))
                        DropsToInventory.sendMessage(senderr, "/dti <on|off>");
                    if(senderr.hasPermission("dti.change.others"))
                        DropsToInventory.sendMessage(senderr, "/dti <on|off> <playername>");
                    return true;
                }else if(args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("off")) {
                    if(!(senderr.hasPermission("dti.change.self")))
                        return true;
                    String UUID = senderr.getUniqueId().toString();
                    List<String> currentouts = DropsToInventory.getInstance().getConfig().getStringList
                            ("optedOutPlayers");
                    if(args[0].equalsIgnoreCase("off")) {
                        if (!(currentouts.contains(UUID)))
                            currentouts.add(UUID);
                        if(!(BreakListener.playeresOptedOut.contains(UUID)))
                            BreakListener.playeresOptedOut.add(UUID);
                    }else {
                        if(currentouts.contains(UUID))
                            currentouts.remove(UUID);
                        if(BreakListener.playeresOptedOut.contains(UUID))
                            BreakListener.playeresOptedOut.remove(UUID);
                    }
                    DropsToInventory.getInstance().getConfig().set("optedOutPlayers", currentouts);
                    return true;
                }
            }
        }
        return false;
    }
}
