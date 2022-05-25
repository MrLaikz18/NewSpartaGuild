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
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

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
                GPlayer gpl = new GPlayer(uuid, g, rank, false);
                ret.add(gpl);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    //UPDATE GPLAYER
    public void updateGPlayer(GPlayer gp) {
        try {
            PreparedStatement ps = db.prepareStatement("UPDATE gplayers SET guilde = ?, rank = ? WHERE uuid = ?");
            ps.setString(1, gp.getGuildUUID().toString());
            ps.setString(2, gp.getRank().toString());
            ps.setString(3, gp.getUUID().toString());
            ps.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    //UPDATE GUILD
    public void updateGuild(Guild g) {
        try {
            PreparedStatement ps = db.prepareStatement("UPDATE guildes SET balance = ? WHERE uuid = ?");
            ps.setDouble(1, g.getBalance());
            ps.setString(2, g.getUUID().toString());
            ps.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    //CREATE GUILD
    public void addGuild(Guild g) {
        try {
            PreparedStatement ps = db.prepareStatement("INSERT INTO guildes (uuid, balance) VALUES(?, 0)");
            ps.setString(1, g.getUUID().toString());
            ps.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    //REMOVE GUILD
    public void removeGuild(Guild g) {
        try {
            PreparedStatement ps = db.prepareStatement("DELETE FROM guildes WHERE uuid = ?");
            ps.setString(1, g.getUUID().toString());
            ps.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    //CREATE GPLAYER
    public void addGPlayer(GPlayer gp) {
        try {
            PreparedStatement ps = db.prepareStatement("INSERT INTO gplayers (uuid, guild, rank) VALUES(?, ?, ?)");
            ps.setString(1, gp.getUUID().toString());
            ps.setString(2, gp.getGuildUUID().toString());
            ps.setString(3, null);
            ps.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    //REMOVE GPLAYER
    public void removeGPlayer(GPlayer gp) {
        try {
            PreparedStatement ps = db.prepareStatement("DELETE FROM gplayers WHERE uuid = ?");
            ps.setString(1, gp.getUUID().toString());
            ps.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    //ASYNC
    public CompletableFuture<List<Guild>> getAllGuildAsync() {
        return future(() -> getAllGuildes());
    }

    public CompletableFuture<List<GPlayer>> getAllGPlayersAsync() {
        return future(() -> getAllGPlayers());
    }

    public CompletableFuture<Void> updateGPlayerAsync(GPlayer gp) {
        return future(() -> updateGPlayer(gp));
    }

    public CompletableFuture<Void> updateGuildAsync(Guild g) {
        return future(() -> updateGuild(g));
    }

    public CompletableFuture<Void> addGuildAsync(Guild g) {
        return future(() -> addGuild(g));
    }

    public CompletableFuture<Void> removeGuildAsync(Guild g) {
        return future(() -> removeGuild(g));
    }

    public CompletableFuture<Void> addGPlayerAsync(GPlayer gp) {
        return future(() -> addGPlayer(gp));
    }

    public CompletableFuture<Void> removeGPlayerAsync(GPlayer gp) {
        return future(() -> removeGPlayer(gp));
    }

    //FUTURE
    public CompletableFuture<Void> future(Runnable runnable) {
        return CompletableFuture.runAsync(() -> {
            try {
                runnable.run();
            } catch (Exception e) {
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                }
                throw new CompletionException(e);
            }
        });
    }

    public <T> CompletableFuture<T> future(Callable<T> supplier) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return supplier.call();
            } catch (Exception e) {
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                }
                throw new CompletionException(e);
            }
        });
    }

}
