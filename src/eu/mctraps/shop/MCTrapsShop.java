package eu.mctraps.shop;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MCTrapsShop extends JavaPlugin {
    FileConfiguration config;

    Connection connection;
    Statement statement;
    private String host, database, username, password;
    private int port;

    @Override
    public void onEnable() {
        getLogger().info("MCTrapsShop has been enabled");

        saveDefaultConfig();
        getDataFolder().mkdir();
        config = getConfig();

        host = config.getString("database.host");
        port = config.getInt("database.port");
        database = config.getString("database.database");
        username = config.getString("database.username");
        password = config.getString("database.password");

        BukkitRunnable r = new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    openConnection();
                    statement = connection.createStatement();
                } catch(SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };
        r.runTaskAsynchronously(this);

        getCommand("smsshop").setExecutor(new MCTrapsShopCommandExecutor(this));
        getCommand("voucher").setExecutor(new MCTrapsShopCommandExecutor(this));
    }

    @Override
    public void onDisable() {
        getLogger().info("MCTrapsShop has been disabled");
    }

    void openConnection() throws SQLException, ClassNotFoundException {
        if(connection != null && !connection.isClosed()) {
            return;
        }

        synchronized (this) {
            if(connection != null && !connection.isClosed()) {
                return;
            }
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);

            getLogger().info("Successfully connected to database. Hurrey!");
        }
    }

    public static String colorify(String s) {
        if(s != null) {
            return ChatColor.translateAlternateColorCodes('&', s);
        }

        return null;
    }
}
