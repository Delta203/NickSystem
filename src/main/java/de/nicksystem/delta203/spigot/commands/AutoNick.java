package de.nicksystem.delta203.spigot.commands;

import de.nicksystem.delta203.spigot.NickSystem;
import de.nicksystem.delta203.spigot.managers.NickAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AutoNick implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (!(sender instanceof Player p)) {
      sender.sendMessage(NickSystem.prefix + "§cDu musst ein Spieler sein!");
      return false;
    }
    if (!p.hasPermission("nick.nick")) return false;
    if (args.length == 1) {
      if (args[0].equalsIgnoreCase("auto")) {
        NickAPI.updateAutoNick(p);
      } else {
        sendHelp(p);
      }
    } else if (args.length == 0) {
      if (NickSystem.manager.nickedPlayers.containsKey(p)) {
        NickAPI.unNick(p);
      } else {
        NickAPI.autoNick(p);
      }
    } else {
      sendHelp(p);
    }

    return false;
  }

  private void sendHelp(Player p) {
    p.sendMessage(NickSystem.prefix + "| §fHilfe");
    p.sendMessage("§e/nick §7Setze dir einen zufälligen Nicknamen");
    p.sendMessage("§e/nick auto §7Aktiviere die Autonick funktion");
  }
}
