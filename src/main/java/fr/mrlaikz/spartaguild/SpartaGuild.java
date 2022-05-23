package fr.mrlaikz.spartaguild;

import fr.mrlaikz.spartaguild.commands.GuildCMD;
import fr.mrlaikz.spartaguild.database.Database;
import fr.mrlaikz.spartaguild.database.SQLGetter;
import fr.mrlaikz.spartaguild.listeners.ChatListener;
import fr.mrlaikz.spartaguild.manager.GuildManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;

public final class SpartaGuild extends JavaPlugin {

    //INSTANCE
    public static SpartaGuild instance;

    //DATABASE
    private Database db;
    private SQLGetter sql;

    //MANAGERS
    private GuildManager guildManager;

    @Override
    public void onEnable() {
        //DATABASE
        try {
            db.connect();
            sql = new SQLGetter(this);
        } catch(SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        //COMMANDS
        getCommand("guild").setExecutor(new GuildCMD(this));

        //LISTENERS
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);

        //MANAGERS
        guildManager = new GuildManager(this);

        //MISC
        instance = this;
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

}
