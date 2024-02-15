package de.nicksystem.delta203.spigot.listeners;

import de.nicksystem.delta203.spigot.NickSystem;
import de.nicksystem.delta203.spigot.managers.NickAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Join implements Listener {

  @EventHandler
  public void onJoin(PlayerJoinEvent e) {
    Player p = e.getPlayer();
    if (p.hasPermission("nick.nick")) {
      if (NickSystem.mysql.isAutoNick(p.getUniqueId().toString()) == 1) {
        NickAPI.autoNick(p);
      }
      ItemStack tool = new ItemStack(Material.NAME_TAG);
      ItemMeta meta = tool.getItemMeta();
      assert meta != null;
      meta.setDisplayName("§5§lAutonick");
      tool.setItemMeta(meta);
      p.getInventory().setItem(0, tool);
    }
    e.setJoinMessage("§e" + p.getName() + " joined the game");
  }
}
