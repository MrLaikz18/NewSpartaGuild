package fr.mrlaikz.spartaguild.listeners;

import com.google.gson.Gson;
import fr.iban.bukkitcore.event.CoreMessageEvent;
import fr.iban.common.messaging.Message;
import fr.mrlaikz.spartaguild.SpartaGuild;
import fr.mrlaikz.spartaguild.manager.GuildManager;
import fr.mrlaikz.spartaguild.objects.GPlayer;
import fr.mrlaikz.spartaguild.objects.Guild;
import fr.mrlaikz.spartaguild.objects.InviteMessage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class MessageListener implements Listener {

    private SpartaGuild plugin;
    private GuildManager guildManager;

    public MessageListener(SpartaGuild plugin) {
        this.plugin = plugin;
        this.guildManager = plugin.getGuildManager();
    }

    @EventHandler
    public void onMessageChannel(CoreMessageEvent e) {
        Message m = e.getMessage();
        String channel = m.getChannel();
        switch(channel) {
            case "UPDATE_GUILD_CHANNEL":
                guildManager.reloadGuildes();
                break;
            case "UPDATE_PLAYER_CHANNEL":
                guildManager.reloadGPlayers();
                break;
            case "INVITE_PLAYER_CHANNEL":
                Gson gson = new Gson();
                InviteMessage inviteMSG = gson.fromJson(m.getMessage(), InviteMessage.class);
                OfflinePlayer op = Bukkit.getOfflinePlayer(inviteMSG.getPlayer());
                Guild g = guildManager.getGuild(inviteMSG.getGuild());
                GPlayer gp = guildManager.getGPlayer(op.getUniqueId());
                gp.invite(g.getUUID());
                if(op.isOnline()) {
                    op.getPlayer().sendMessage("§aTu as reçu une invitation a rejoindre " + g.getName());
                }
        }
    }

}
