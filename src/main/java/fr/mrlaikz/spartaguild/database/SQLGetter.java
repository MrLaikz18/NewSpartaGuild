package fr.mrlaikz.spartaguild.database;

import fr.mrlaikz.spartaguild.enums.Rank;
import fr.mrlaikz.spartaguild.SpartaGuild;
import fr.mrlaikz.spartaguild.objects.GPlayer;
import fr.mrlaikz.spartaguild.objects.Guild;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SQLGetter {

    private SpartaGuild plugin;
    private Connection db;

    public SQLGetter(SpartaGuild plugin) {
        this.plugin = plugin;
        this.db = plugin.getDatabase();
    }

    //GET ALL GUILDES
    public List<Guild> getAllGuildes() {
        List<Guild> ret = new ArrayList<Guild>();
        try {
            PreparedStatement ps = db.prepareStatement("SELECT * FROM guildes");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String name = rs.getString("name");
                UUID uuid = UUID.fromString(rs.getString("uuid"));
                double bal = rs.getDouble("balance");
                Guild g = new Guild(name, uuid, bal);
                ret.add(g);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    //GET ALL GPLAYERS
    public List<GPlayer> getAllGPlayers() {
        List<GPlayer> ret = new ArrayList<GPlayer>();
        try {
            PreparedStatement ps = db.prepareStatement("SELECT * FROM gplayers");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                UUID uuid = UUID.fromString(rs.getString("uuid"));
                UUID g = UUID.fromString(rs.getString("guilde"));
                Rank rank = Rank.valueOf(rs.getString("rank"));
                boolean chat = rs.getBoolean("chat");
                GPlayer gpl = new GPlayer(uuid, g, rank, chat);
                ret.add(gpl);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

}
