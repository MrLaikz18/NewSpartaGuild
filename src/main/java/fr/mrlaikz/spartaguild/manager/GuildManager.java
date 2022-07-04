package fr.mrlaikz.spartaguild.manager;

import com.google.gson.Gson;
import fr.iban.bukkitcore.CoreBukkitPlugin;
import fr.mrlaikz.spartaguild.SpartaGuild;
import fr.mrlaikz.spartaguild.database.SQLGetter;
import fr.mrlaikz.spartaguild.enums.Rank;
import fr.mrlaikz.spartaguild.objects.GPlayer;
import fr.mrlaikz.spartaguild.objects.Guild;
import fr.mrlaikz.spartaguild.objects.InviteMessage;
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

    //DATA
    private HashMap<UUID, Guild> guildes = new HashMap<UUID, Guild>();
    private HashMap<UUID, GPlayer> gplayers = new HashMap<UUID, GPlayer>();
    private List<Player> spy = new ArrayList<Player>();

    //TEST
    public void info(Player p) {
        p.sendMessage(""+guildes.keySet());
        p.sendMessage("§cPLAYERS");
        for(UUID u : gplayers.keySet()) {
            p.sendMessage(Bukkit.getOfflinePlayer(u).getName() + " -> " + gplayers.get(u).getGuildUUID() + " -> " + gplayers.get(u).getRank().getName());
        }
        p.sendMessage("§cGUILDES");
        for(UUID v : guildes.keySet()) {
            p.sendMessage(guildes.get(v).getName());
        }
    }

    //LOADERS
    public void load() {
        sql.loadTables();
        sql.getAllGuildAsync().thenAccept(gs -> {
            sql.getAllGPlayersAsync().thenAccept(gp -> {
                for(Guild g : gs) {
                    guildes.put(g.getUUID(), g);
                    for (GPlayer gpp : gp) {
                        gplayers.put(gpp.getUUID(), gpp);
                        if (gpp.getGuildUUID().equals(g.getUUID())) {
                            g.addMember(gpp);
                        }
                    }
                }
            });
        });
    }

    //ADD INFOS
    public void addGPlayer(GPlayer gp) {
        gplayers.put(gp.getUUID(), gp);
        sql.addGPlayerAsync(gp);
    }

    public Guild getGuild(UUID uuid) {
        return guildes.get(uuid);
    }

    //VOIDERS
    public void joinGuild(GPlayer gp, Guild guild) {
        Guild g = guildes.get(guild.getUUID());
        GPlayer gpl = gplayers.get(gp.getUUID());
        g.addMember(gpl);
        gpl.setGuild(guild.getUUID());
        gpl.setRank(Rank.MEMBER);
        updateGPlayer(gp);
    }

    public void leaveGuild(GPlayer gp) {
        Guild g = guildes.get(gp.getGuildUUID());
        GPlayer gpl = gplayers.get(gp.getUUID());
        g.removeMember(gpl);
        gpl.setGuild(null);
        gpl.setRank(Rank.NULL);
        updateGPlayer(gp);
    }

    public void createGuild(String name, GPlayer owner) {
        Guild g = new Guild(name, owner);
        owner.setGuild(g.getUUID());
        owner.setRank(Rank.OWNER);
        guildes.put(g.getUUID(), g);
        sql.addGuildAsync(g);
        updateGPlayer(owner);
        syncGuild();
        syncGuildPlayer();
    }

    public void disbandGuild(UUID uuid) {
        Guild g = guildes.get(uuid);
        for(GPlayer gpg : g.getMembers()) {
            gpg.setGuild(null);
            gpg.setRank(Rank.NULL);
            sql.updateGPlayerAsync(gpg);
        }
        plugin.getLogger().info(""+guildes.containsKey(g.getUUID()));
        guildes.remove(g.getUUID());
        g.disband();
        sql.removeGuildAsync(g);
        syncGuild();
        syncGuildPlayer();
    }

    public GPlayer getGPlayer(UUID uuid) {
        return gplayers.get(uuid);
    }

    //CHAT
    public void guildChat(Guild guild, GPlayer sender, String msg) {
        for(GPlayer gp : guild.getMembers()) {
            Player p = Bukkit.getPlayer(gp.getUUID());
            p.sendMessage(plugin.strConfig("chat.prefix")
                    .replace("%guild%", guild.getName())
                    .replace("%rank%", sender.getRank().getName())
                    .replace("%player%", Bukkit.getPlayer(sender.getUUID()).getName())
                    .replace("%message%", msg));
        }
        for(Player p : spy) {
            p.sendMessage(plugin.strConfig("chat.spyprefix")
                    .replace("%guild%", guild.getName())
                    .replace("%rank%", sender.getRank().getName())
                    .replace("%player%", Bukkit.getPlayer(sender.getUUID()).getName())
                    .replace("%message%", msg));
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

    //UPDATES
    public void updateGuild(Guild g) {
        guildes.replace(g.getUUID(), g);
        sql.updateGuildAsync(g);
        syncGuild();
    }

    public void updateGPlayer(GPlayer gp) {
        gplayers.replace(gp.getUUID(), gp);
        sql.updateGPlayerAsync(gp);
        syncGuildPlayer();
    }

    //SYNC
    public void syncGuild() {
        CoreBukkitPlugin core = CoreBukkitPlugin.getInstance();
        core.getMessagingManager().sendMessage("UPDATE_GUILD_CHANNEL", "");
    }

    public void syncGuildPlayer() {
        CoreBukkitPlugin core = CoreBukkitPlugin.getInstance();
        core.getMessagingManager().sendMessage("UPDATE_PLAYER_CHANNEL", "");
    }

    public void inviteMSG(UUID player, UUID guildUUID) {
        CoreBukkitPlugin core = CoreBukkitPlugin.getInstance();
        Gson gson = new Gson();
        core.getMessagingManager().sendMessage("INVITE_PLAYER_CHANNEL", gson.toJson(new InviteMessage(player, guildUUID)));
    }

    //RELOADERS
    public void reloadGuildes() {
        guildes.clear();
        sql.getAllGuildAsync().thenAccept(glist -> {
           for(Guild g : glist) {
               guildes.put(g.getUUID(), g);
           }
        });
    }

    public void reloadGPlayers() {
        gplayers.clear();
        sql.getAllGPlayersAsync().thenAccept(list -> {
            for(GPlayer gp : list) {
                gplayers.put(gp.getUUID(), gp);
            }
        });
    }


}
