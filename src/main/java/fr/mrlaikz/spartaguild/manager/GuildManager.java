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
import java.util.concurrent.atomic.AtomicReference;

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
        sql.getAllGuildAsync().thenAccept(gs -> {
            sql.getAllGPlayersAsync().thenAccept(gp -> {
                for(Guild g : gs) {
                    guildes.put(g.getUUID(), g);
                    for(GPlayer gpp : gp) {
                        gplayers.put(gpp.getUUID(), gpp);
                        if(gpp.getGuildUUID().equals(g.getUUID())) {
                            g.addMember(gpp);
                        }
                    }
                }
            });
        });
    }

    public void addGPlayer(GPlayer gp) {
        gplayers.put(gp.getUUID(), gp);
        sql.addGPlayerAsync(gp);
    }

    public Guild getGuild(UUID uuid) {
        return guildes.get(uuid);
    }

    public void joinGuild(GPlayer gp, Guild guild) {
        Guild g = guildes.get(guild.getUUID());
        GPlayer gpl = gplayers.get(gp.getUUID());
        g.addMember(gpl);
        gpl.setGuild(guild.getUUID());
        gpl.setRank(Rank.MEMBER);
        sql.updateGPlayerAsync(gp);
    }

    public void leaveGuild(GPlayer gp) {
        Guild g = guildes.get(gp.getGuildUUID());
        GPlayer gpl = gplayers.get(gp.getUUID());
        g.removeMember(gpl);
        gpl.setGuild(null);
        gpl.setRank(null);
        sql.updateGPlayerAsync(gp);
    }

    public void createGuild(String name, GPlayer owner) {
        Guild g = new Guild(name, owner);
        guildes.put(g.getUUID(), g);
        sql.addGuildAsync(g);
    }

    public void disbandGuild(UUID uuid) {
        Guild g = guildes.get(uuid);
        g.disband();
        guildes.remove(uuid);
        sql.removeGuildAsync(g);
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

    public void updateGPlayer(GPlayer gp) {

    }


}
