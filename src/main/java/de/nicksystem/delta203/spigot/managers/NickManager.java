package de.nicksystem.delta203.spigot.managers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import com.mojang.authlib.GameProfile;
import de.nicksystem.delta203.spigot.files.NickNamesYML;
import de.nicksystem.delta203.spigot.utils.Packets;
import org.bukkit.entity.Player;

public class NickManager {

  /** List of nicked players */
  public HashMap<Player, String> nickedPlayers;

  /** List of available nicknames */
  public ArrayList<String> nickNames;

  /** Packet name field */
  public Field nameField;

  public NickManager() {
    nickedPlayers = new HashMap<>();
    nickNames = new ArrayList<>();
    nickNames.addAll(NickNamesYML.get().getStringList("NameList"));
    nameField = Packets.getField(GameProfile.class, "name");
  }
}
