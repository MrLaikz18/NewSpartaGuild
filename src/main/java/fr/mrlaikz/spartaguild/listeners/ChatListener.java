package fr.mrlaikz.spartaguild.listeners;

import fr.mrlaikz.spartaguild.SpartaGuild;
import fr.mrlaikz.spartaguild.manager.GuildManager;
import fr.mrlaikz.spartaguild.objects.GPlayer;
import fr.mrlaikz.spartaguild.objects.Guild;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private SpartaGuild plugin;
    private GuildManager guildManager;

    public ChatListener(SpartaGuild plugin) {
        this.plugin = plugin;
        this.guildManager = plugin.getGuildManager();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        GPlayer gp = guildManager.getGPlayer(p.getUniqueId());
        if(gp != null && gp.getGuildUUID() != null) {
            if(gp.getChat()) {
                e.setCancelled(true);
                Guild g = guildManager.getGuild(gp.getGuildUUID());
                guildManager.guildChat(g, gp, e.getMessage());
            }
        }
    }

}
