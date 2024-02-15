package de.nicksystem.delta203.spigot.listeners;

import de.nicksystem.delta203.spigot.NickSystem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Quit implements Listener {

  @EventHandler
  public void onQuit(PlayerQuitEvent e) {
    Player p = e.getPlayer();
    act(p);
  }

  @EventHandler
  public void onKick(PlayerKickEvent e) {
    Player p = e.getPlayer();
    act(p);
  }

  private void act(Player p) {
    if (NickSystem.manager.nickedPlayers.containsKey(p)) {
      NickSystem.manager.nickNames.add(p.getName());
      NickSystem.manager.nickedPlayers.remove(p);
    }
  }
}
