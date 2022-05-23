package fr.mrlaikz.spartaguild.manager;

import fr.mrlaikz.spartaguild.SpartaGuild;
import fr.mrlaikz.spartaguild.database.SQLGetter;
import fr.mrlaikz.spartaguild.enums.Rank;
import fr.mrlaikz.spartaguild.objects.GPlayer;
import fr.mrlaikz.spartaguild.objects.Guild;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GuildManager {

    private SpartaGuild plugin;
    private SQLGetter sql;

    public GuildManager(SpartaGuild plugin) {
        this.plugin = plugin;
        this.sql = new SQLGetter(plugin);
    }

    private HashMap<UUID, Guild> guildes = new HashMap<UUID, Guild>();
    private HashMap<UUID, GPlayer> gplayers = new HashMap<UUID, GPlayer>();
    private List<Player> spy = new ArrayList<Player>();

    public void load() {
        List<Guild> gds = sql.getAllGuildes();
        List<GPlayer> gps = sql.getAllGPlayers();
        for(Guild g : gds) {
            guildes.put(g.getUUID(), g);
            for(GPlayer pl : gps) {
                gplayers.put(pl.getUUID(), pl);
                if(pl.getGuildUUID().equals(g.getUUID())) {
                    g.addMember(pl);
                }
            }
        }
    }

    public void addGPlayer(GPlayer gp) {
        gplayers.put(gp.getUUID(), gp);
        //UPDATE SQL
    }

    public Guild getGuild(UUID uuid) {
        return guildes.get(uuid);
    }

    public void joinGuild(GPlayer gp, Guild guild) {
        guild.addMember(gp);
        gp.setGuild(guild.getUUID());
        gp.setRank(Rank.MEMBER);
    }

    public void createGuild(String name, GPlayer owner) {
        Guild g = new Guild(name, owner);
        guildes.put(g.getUUID(), g);
        //UPDATE SQL
    }

    public void disbandGuild(UUID uuid) {
        Guild g = guildes.get(uuid);
        guildes.remove(uuid);
        g.disband();
        //UPDATE SQL
    }

    public GPlayer getGPlayer(UUID uuid) {
        return gplayers.get(uuid);
    }

    public void guildChat(Guild guild, GPlayer sender, String msg) {
        for(GPlayer gp : guild.getMembers()) {
            Player p = Bukkit.getPlayer(gp.getUUID());
            p.sendMessage(plugin.strConfig("chat.prefix").replace("%guild%", guild.getName()) + sender.getRank().getName() + msg);
        }
        for(Player p : spy) {
            p.sendMessage(plugin.strConfig("chat.spyprefix").replace("%guild%", guild.getName()) + sender.getRank().getName() + msg);
        }
    }

    //GUILD SPY
    public void addSpy(Player p) {
        spy.add(p);
    }

    public void removeSpy(Player p) {
        spy.remove(p);
    }

    public boolean isSpy(Player p) {
        return spy.contains(p);
    }



}
