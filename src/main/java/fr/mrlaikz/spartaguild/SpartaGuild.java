package fr.mrlaikz.spartaguild;

import fr.mrlaikz.spartaguild.commands.GuildCMD;
import fr.mrlaikz.spartaguild.database.Database;
import fr.mrlaikz.spartaguild.database.SQLGetter;
import fr.mrlaikz.spartaguild.listeners.ChatListener;
import fr.mrlaikz.spartaguild.listeners.MessageListener;
import fr.mrlaikz.spartaguild.manager.GuildManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;

public final class SpartaGuild extends JavaPlugin {

    //INSTANCE
    public static SpartaGuild instance;

    //DATABASE
    private Database db;
    private SQLGetter sql;
    private static Economy econ = null;

    //MANAGERS
    private GuildManager guildManager;

    @Override
    public void onEnable() {
        //CONFIG
        saveDefaultConfig();
        instance = this;

        //DATABASE
        try {
            db = new Database(this);
            db.connect();
            sql = new SQLGetter(this);
        } catch(SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        //MANAGERS
        guildManager = new GuildManager(this);
        guildManager.load();

        //LISTENERS
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(new MessageListener(this), this);

        //COMMANDS
        getCommand("guild").setExecutor(new GuildCMD(this));

        //MISC
        setupEconomy();
        getLogger().info("Plugin Actif");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin Inactif");
    }

    public String strConfig(String path) {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString(path));
    }

    public static SpartaGuild getInstance() {
        return instance;
    }

    public Connection getDatabase() {
        return db.getConnection();
    }

    public GuildManager getGuildManager() {
        return guildManager;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;

    }

    public static Economy getEconomy() {
        return econ;
    }

}
