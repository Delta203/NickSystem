package de.nicksystem.delta203.spigot.files;

import de.nicksystem.delta203.spigot.NickSystem;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class NickNamesYML {

  private static final File file = new File(NickSystem.plugin.getDataFolder(), "nicknames.yml");
  private static FileConfiguration cfg;

  public static FileConfiguration get() {
    return cfg;
  }

  public static void load() {
    try {
      cfg.load(file);
    } catch (IOException | InvalidConfigurationException e) {
      e.printStackTrace();
    }
  }

  public static void save() {
    try {
      cfg.save(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void create() {
    if (!NickSystem.plugin.getDataFolder().exists()) {
      NickSystem.plugin.getDataFolder().mkdir();
    }

    try {
      if (!file.exists()) {
        java.nio.file.Files.copy(
            Objects.requireNonNull(NickSystem.plugin.getResource("nicknames.yml")), file.toPath());
      }
      cfg = YamlConfiguration.loadConfiguration(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
