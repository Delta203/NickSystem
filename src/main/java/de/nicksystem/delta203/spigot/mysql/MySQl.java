package de.nicksystem.delta203.spigot.mysql;

import de.nicksystem.delta203.spigot.NickSystem;
import org.bukkit.Bukkit;

import java.sql.*;

public class MySQl {

  private final String host;
  private final int port;
  private final String database;
  private final String name;
  private final String password;

  public Connection connection;

  public MySQl(String host, int port, String database, String name, String password) {
    this.host = host;
    this.port = port;
    this.database = database;
    this.name = name;
    this.password = password;
  }

  public boolean isConnected() {
    return connection != null;
  }

  public void connect() {
    if (isConnected()) return;
    try {
      connection =
          DriverManager.getConnection(
              "jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true",
              name,
              password);
      Bukkit.getConsoleSender()
          .sendMessage(NickSystem.prefix + "§aMySQl Verbindung erfolgreich hergestellt.");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void disconnect() {
    if (!isConnected()) return;
    try {
      connection.close();
      Bukkit.getConsoleSender()
          .sendMessage(NickSystem.prefix + "§cMySQl Verbindung erfolgreich aufgelöst.");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void createTable() {
    try {
      connection
          .prepareStatement(
              "CREATE TABLE IF NOT EXISTS NickSystem (PlayerUUID VARCHAR(100), AutoNick INT(16))")
          .executeUpdate();
      Bukkit.getConsoleSender()
          .sendMessage(NickSystem.prefix + "§aMySQl Tabelle erfolgreich erstellt.");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public int isAutoNick(String uuid) {
    try {
      PreparedStatement ps =
          NickSystem.mysql.connection.prepareStatement(
              "SELECT AutoNick FROM NickSystem WHERE PlayerUUID = ?");
      ps.setString(1, uuid);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        return rs.getInt("AutoNick");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return -1;
  }

  public void setAutoNick(String uuid, int autoNick) {
    if (isAutoNick(uuid) == -1) {
      try {
        PreparedStatement ps =
            connection.prepareStatement(
                "INSERT INTO NickSystem (PlayerUUID,AutoNick) VALUES (?,?)");
        ps.setString(1, uuid);
        ps.setInt(2, autoNick);
        ps.executeUpdate();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } else {
      try {
        PreparedStatement ps =
            connection.prepareStatement("UPDATE NickSystem SET AutoNick = ? WHERE PlayerUUID = ?");
        ps.setInt(1, autoNick);
        ps.setString(2, uuid);
        ps.executeUpdate();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }
}
