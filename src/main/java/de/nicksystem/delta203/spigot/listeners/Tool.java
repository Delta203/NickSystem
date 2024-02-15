package de.nicksystem.delta203.spigot.listeners;

import de.nicksystem.delta203.spigot.managers.NickAPI;
import java.util.Objects;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class Tool implements Listener {

  @EventHandler
  public void onInteract(PlayerInteractEvent e) {
    if (e.getHand() != EquipmentSlot.HAND) return;
    if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
      Player p = e.getPlayer();
      try {
        if (Objects.requireNonNull(p.getInventory().getItemInMainHand().getItemMeta())
            .getDisplayName()
            .equalsIgnoreCase("§5§lAutonick")) {
          if (!p.hasPermission("nick.nick")) return;
          p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1, 2);
          NickAPI.updateAutoNick(p);
        }
      } catch (Exception ignored) {
      }
    }
  }
}
