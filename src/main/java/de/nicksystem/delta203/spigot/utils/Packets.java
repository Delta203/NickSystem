package de.nicksystem.delta203.spigot.utils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.EnumSet;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Packets {

  public static Field getField(Class<?> c, String name) {
    try {
      Field field = c.getDeclaredField(name);
      field.setAccessible(true);
      return field;
    } catch (NoSuchFieldException | SecurityException ex) {
      return null;
    }
  }

  public static void sendPacket(Packet<?> packet, Player p) {
    ((CraftPlayer) p).getHandle().c.a(packet, null);
  }

  public static void removePlayer(Player p) {
    // for all players
    PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(p.getEntityId());
    for (Player all : Bukkit.getOnlinePlayers()) {
      if (all == p) continue;
      sendPacket(destroy, all);
      all.hidePlayer(p);
    }
    // for player p
    ClientboundPlayerInfoRemovePacket packet =
        new ClientboundPlayerInfoRemovePacket(Collections.singletonList(p.getUniqueId()));
    sendPacket(packet, p);
  }

  public static void addPlayer(Player p) {
    // for all players
    PacketPlayOutNamedEntitySpawn spawn =
        new PacketPlayOutNamedEntitySpawn(((CraftPlayer) p).getHandle());
    for (Player all : Bukkit.getOnlinePlayers()) {
      if (all == p) continue;
      sendPacket(spawn, all);
      all.showPlayer(p);
    }
    // for player p
    ClientboundPlayerInfoUpdatePacket packet =
        new ClientboundPlayerInfoUpdatePacket(
            EnumSet.of(
                ClientboundPlayerInfoUpdatePacket.a.a,
                ClientboundPlayerInfoUpdatePacket.a.b,
                ClientboundPlayerInfoUpdatePacket.a.c,
                ClientboundPlayerInfoUpdatePacket.a.d,
                ClientboundPlayerInfoUpdatePacket.a.e,
                ClientboundPlayerInfoUpdatePacket.a.f),
            Collections.singletonList(((CraftPlayer) p).getHandle()));
    sendPacket(packet, p);
  }
}
