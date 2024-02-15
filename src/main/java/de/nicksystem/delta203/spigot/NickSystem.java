package de.nicksystem.delta203.spigot;

import de.nicksystem.delta203.spigot.commands.AutoNick;
import de.nicksystem.delta203.spigot.files.NickNamesYML;
import de.nicksystem.delta203.spigot.listeners.Join;
import de.nicksystem.delta203.spigot.listeners.Quit;
import de.nicksystem.delta203.spigot.listeners.Tool;
import de.nicksystem.delta203.spigot.managers.NickManager;
import de.nicksystem.delta203.spigot.mysql.MySQl;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class NickSystem extends JavaPlugin {

  public static NickSystem plugin;
  public static String prefix = "§f[§5Nick§f] §7";
  public static NickManager manager;
  public static MySQl mysql;

  @Override
  public void onEnable() {
    plugin = this;
    NickNamesYML.create();
    NickNamesYML.load();
    manager = new NickManager();
    mysql = new MySQl("localhost", 3306, "test", "root", "");
    mysql.connect();
    mysql.createTable();

    Objects.requireNonNull(getCommand("nick")).setExecutor(new AutoNick());
    Bukkit.getPluginManager().registerEvents(new Join(), this);
    Bukkit.getPluginManager().registerEvents(new Quit(), this);
    Bukkit.getPluginManager().registerEvents(new Tool(), this);
    Bukkit.getConsoleSender().sendMessage(prefix + "§aPlugin erfolgreich geladen.");
  }
}
