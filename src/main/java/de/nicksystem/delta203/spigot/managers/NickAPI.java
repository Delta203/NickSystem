package de.nicksystem.delta203.spigot.managers;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.nicksystem.delta203.spigot.NickSystem;
import de.nicksystem.delta203.spigot.utils.GameProfileBuilder;
import de.nicksystem.delta203.spigot.utils.Packets;
import de.nicksystem.delta203.spigot.utils.UUIDFetcher;
import java.util.Collection;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NickAPI {

  /**
   * Default usernames of active minecraft accounts. Please note that the username must be a valid
   * account!
   */
  private static final String[] defaultSkins = {
    "80083",
    "_solo",
    "1037cky",
    "17hunter00",
    "1johnclaude",
    "1zak11",
    "22chad22",
    "5k337",
    "93simon",
    "9falcon",
    "A5002",
    "A51assassin",
    "Aaroncvx",
    "ABCCOOL",
    "abcgodyo",
    "Ac1Baddie",
    "ACarroll",
    "accountgmail",
    "TheBabyKitten",
    "ACS1997"
  };

  /**
   * Change the auto nick state of a player which is stored in a database.
   *
   * @param p the player to be updated
   */
  public static void updateAutoNick(Player p) {
    if (NickSystem.mysql.isAutoNick(p.getUniqueId().toString()) == 1) { // player has auto nick
      NickSystem.mysql.setAutoNick(p.getUniqueId().toString(), 0);
      p.sendMessage(NickSystem.prefix + "§4Der automatische Nickname wurde§7: §cDeaktiviert");
    } else { // player has no auto nick
      NickSystem.mysql.setAutoNick(p.getUniqueId().toString(), 1);
      p.sendMessage(NickSystem.prefix + "§4Der automatische Nickname wurde§7: §aAktiviert");
    }
  }

  /**
   * Change the nickname of a player to a random name from an editable list of nicknames.
   *
   * @param p the player to be nicked
   */
  public static void autoNick(Player p) {
    // check if player is not already nicked
    if (NickSystem.manager.nickedPlayers.containsKey(p)) return;
    // check if there is an available nickname
    if (NickSystem.manager.nickNames.isEmpty()) {
      p.sendMessage(NickSystem.prefix + "§cEs wurde kein freier Nickname gefunden!");
      return;
    }
    NickSystem.manager.nickedPlayers.put(p, p.getName());
    String name =
        NickSystem.manager.nickNames.get(new Random().nextInt(NickSystem.manager.nickNames.size()));
    NickSystem.manager.nickNames.remove(name);
    changeName(p, name);
    p.sendMessage("\n" + NickSystem.prefix + "§4Aktueller Nickname§7: §6" + name + "\n ");
  }

  /**
   * Change the nickname of a player to its originally name and put the fake nickname into the list
   * of nicknames.
   *
   * @param p the player to be name changed
   */
  public static void unNick(Player p) {
    // check if player is nicked
    if (!NickSystem.manager.nickedPlayers.containsKey(p)) return;
    String name = NickSystem.manager.nickedPlayers.get(p);
    NickSystem.manager.nickedPlayers.remove(p);
    NickSystem.manager.nickNames.add(p.getName());
    changeName(p, name);
    p.sendMessage(NickSystem.prefix + "§4Dein Nickname wurde entfernt!");
  }

  /**
   * Change the nickname of a player.
   *
   * @param p the player to be name changed
   * @param name the nickname the player should get
   */
  private static void changeName(Player p, String name) {
    // nick change
    CraftPlayer cp = (CraftPlayer) p;
    try {
      NickSystem.manager.nameField.set(cp.getProfile(), name);
    } catch (IllegalAccessException ignored) {
    }
    p.setCustomName(name);
    p.setDisplayName(name);
    p.setPlayerListName(name);
    // TODO: Set player scoreboard if needed (e.g. sorted tab list)
    // skin change
    GameProfile gp = cp.getProfile();
    try {
      gp = GameProfileBuilder.fetch(UUIDFetcher.getUUID(name));
    } catch (Exception e) {
      try { // load alternative skin profiles
        gp =
            GameProfileBuilder.fetch(
                UUIDFetcher.getUUID(defaultSkins[new Random().nextInt(defaultSkins.length)]));
      } catch (Exception ex) {
        p.sendMessage(NickSystem.prefix + "§cDer Skin konnte nicht geladen werden!");
      }
    }
    Collection<Property> collection = gp.getProperties().get("textures");
    cp.getProfile().getProperties().removeAll("textures");
    cp.getProfile().getProperties().putAll("textures", collection);
    // update player
    Packets.removePlayer(p);
    Bukkit.getScheduler().scheduleSyncDelayedTask(NickSystem.plugin, () -> Packets.addPlayer(p), 1);
  }
}
