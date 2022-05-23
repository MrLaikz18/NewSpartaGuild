package fr.mrlaikz.spartaguild.commands;

import fr.mrlaikz.spartaguild.SpartaGuild;
import fr.mrlaikz.spartaguild.manager.GuildManager;
import fr.mrlaikz.spartaguild.objects.GPlayer;
import fr.mrlaikz.spartaguild.objects.Guild;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class GuildCMD implements CommandExecutor {

    private SpartaGuild plugin;
    private GuildManager guildManager;

    public GuildCMD(SpartaGuild plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(command.getName().equalsIgnoreCase("guild")) {

            if(sender instanceof Player) {
                Player p = (Player) sender;
                GPlayer gp = guildManager.getGPlayer(p.getUniqueId());

                if(args.length == 0) {
                    if(gp == null || gp.getGuildUUID() == null) {
                        p.sendMessage("§c§lVous n'avez pas encore de Guilde !");
                        return false;
                    }
                    //MENU GUILDE
                }

                if(args.length == 1) {
                    if(args[0].equalsIgnoreCase("spy") && p.hasPermission("spartaguild.spy")) {
                        if(guildManager.isSpy(p)) {
                            guildManager.removeSpy(p);
                            p.sendMessage("§cSpy Guild désactivé");
                        } else {
                            guildManager.addSpy(p);
                            p.sendMessage("§aSpy Guild activé");
                        }
                    }
                }

                if(args.length == 2) {

                    if(args[0].equalsIgnoreCase("join")) {

                        String gName = args[1];

                        if(gp == null) {
                            gp = new GPlayer(p.getUniqueId());
                            guildManager.addGPlayer(gp);
                        }

                        if(gp.getGuildUUID() != null) {
                            p.sendMessage("§cVous êtes déjà dans une guilde !");
                            return false;
                        }

                        for(UUID u : gp.getInvites()) {
                            if(guildManager.getGuild(u).getName().equals(gName)) {
                                Guild g = guildManager.getGuild(u);
                                guildManager.joinGuild(gp, g);
                                p.sendMessage("§aVous avez bien rejoint la guilde " + g.getName());
                            }
                        }

                    }

                    if(args[0].equalsIgnoreCase("create")) {

                        String gName = args[1];

                        if(gp == null) {
                            gp = new GPlayer(p.getUniqueId());
                            guildManager.addGPlayer(gp);
                        }

                        if(gp.getGuildUUID() != null) {
                            p.sendMessage("§cVous avez déjà une guilde !");
                            return false;
                        }

                        guildManager.createGuild(gName, gp);
                        p.sendMessage("§aVous avez bien créer la guilde " + gName);

                    }
                }

            }

        }

        return false;
    }
}
