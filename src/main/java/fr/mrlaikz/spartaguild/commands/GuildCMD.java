package fr.mrlaikz.spartaguild.commands;

import fr.mrlaikz.spartaguild.SpartaGuild;
import fr.mrlaikz.spartaguild.database.SQLGetter;
import fr.mrlaikz.spartaguild.enums.Rank;
import fr.mrlaikz.spartaguild.manager.GuildManager;
import fr.mrlaikz.spartaguild.objects.GPlayer;
import fr.mrlaikz.spartaguild.objects.Guild;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class GuildCMD implements CommandExecutor {

    private SpartaGuild plugin;
    private GuildManager guildManager;
    private SQLGetter sql;
    public final String prefix = "§6§l[Sparta'§c§lGuild§6§l] §r";

    public GuildCMD(SpartaGuild plugin) {
        this.plugin = plugin;
        this.sql = new SQLGetter(plugin);
        this.guildManager = plugin.getGuildManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(command.getName().equalsIgnoreCase("guild")) {

            if(sender instanceof Player) {
                Player p = (Player) sender;
                GPlayer gp = guildManager.getGPlayer(p.getUniqueId());

                if(args.length == 0) {
                    p.sendMessage("");
                    p.sendMessage("§b========== §6§lSparta'§c§lGuild §b==========");
                    p.sendMessage("");
                    p.sendMessage("§6/guild: §7Affichage de l'aide générale"); //FAIT
                    p.sendMessage("§6/guild eco: §7Affichage de l'aide économique"); //FAIT
                    p.sendMessage("§6/guild chat: §7Basculer en chat normal / guild");
                    p.sendMessage("§6/guild join <nom>: §7Rejoindre une guilde"); //FAIT
                    p.sendMessage("§6/guild create <nom>: §7Créer une guilde"); //FAIT
                    p.sendMessage("§6/guild leave: §7Quitter une guilde"); //FAIT
                    p.sendMessage("§6/guild disband: §7Dissoudre une guilde"); //FAIT
                    p.sendMessage("§6/guild show: §7Informations de sa guilde"); //FAIT
                    p.sendMessage("§6/guild invite <pseudo>: §7Inviter un joueur dans sa guilde"); //FAIT
                    p.sendMessage("§6/guild promote <pseudo>: §7Augmenter le rank d'un joueur"); //FAIT
                    p.sendMessage("§6/guild demote <pseudo>: §7Baisser le rank d'un joueur"); //FAIT
                    p.sendMessage("§6/guild rank <pseudo>: §7Obtenir le rank d'un joueur");  //FAIT
                    p.sendMessage("§6/guild lead <pseudo>: §7Donner le commandement de la guilde"); //FAIT
                    p.sendMessage("§6/guild show <guilde>: §7Informations d'une guilde"); //FAIT
                    p.sendMessage("");
                }

                if(args.length == 1) {

                    if(args[0].equalsIgnoreCase("test")) {
                        guildManager.info(p);
                    }

                    //GUILD SPY
                    if(args[0].equalsIgnoreCase("spy") && p.hasPermission("spartaguild.spy")) {
                        if(guildManager.isSpy(p)) {
                            guildManager.removeSpy(p);
                            p.sendMessage(prefix + "§cSpy Guild désactivé");
                        } else {
                            guildManager.addSpy(p);
                            p.sendMessage(prefix + "§aSpy Guild activé");
                        }
                    }

                    //GUILD CHAT
                    if(args[0].equalsIgnoreCase("chat")) {
                        if (gp == null || gp.getGuildUUID() == null) {
                            p.sendMessage(prefix + "§cVous n'avez pas de guildes !");
                            return false;
                        }

                        gp.setChat(!gp.getChat());
                        if(gp.getChat()) {
                            p.sendMessage(prefix + "§aChat de Guild activé");
                        } else {
                            p.sendMessage(prefix + "§aChat de Guild désactivé");
                        }

                    }

                    //GUILD LEAVE
                    if(args[0].equalsIgnoreCase("leave")) {
                        if(gp == null || gp.getGuildUUID() == null) {
                            p.sendMessage(prefix + "§cVous n'avez pas de guildes !");
                            return false;
                        }
                        Guild g = guildManager.getGuild(gp.getGuildUUID());
                        if(gp.getRank().equals(Rank.OWNER)) {
                            for(GPlayer gpa : g.getMembers()) {
                                Bukkit.getPlayer(gpa.getUUID()).sendMessage(prefix + "§cVotre guilde a été dissoute !");
                            }
                            guildManager.disbandGuild(g.getUUID());
                            return false;
                        }
                        guildManager.leaveGuild(gp);
                        for(GPlayer gpa : g.getMembers()) {
                            Bukkit.getPlayer(gpa.getUUID()).sendMessage(prefix + "§c"+p.getName()+" a quitté la guilde");
                        }
                        gp.setChat(false);
                        p.sendMessage(prefix + "§aVous avez bien quitté votre guilde");
                    }

                    //GUILD DISBAND
                    if(args[0].equalsIgnoreCase("disband")) {
                        if(gp == null || gp.getGuildUUID() == null) {
                            p.sendMessage(prefix + "§cVous n'avez pas de guilde !");
                            return false;
                        }

                        if(!guildManager.isBypass(p)) {
                            if(!gp.getRank().equals(Rank.OWNER)) {
                                p.sendMessage(prefix + "§cVous n'avez pas la permission de faire cela !");
                                return false;
                            }
                        }

                        guildManager.disbandGuild(gp.getGuildUUID());
                        p.sendMessage(prefix + "§aVotre guilde a bien été dissoute");

                    }

                    //GUILD ECO
                    if(args[0].equalsIgnoreCase("eco")) {
                        p.sendMessage("");
                        p.sendMessage("§b========== §6§lSparta'§c§lGuild §b==========");
                        p.sendMessage("");
                        p.sendMessage("§6/guild eco: §7Affichage de l'aide économique");
                        p.sendMessage("§6/guild eco bal: §7Affichage de la balance");
                        p.sendMessage("§6/guild eco baltop: §7Affichage le classement économique");
                        p.sendMessage("§6/guild eco deposit <montant>: §7Déposer de l'argent sur la banque de guilde");
                        p.sendMessage("§6/guild eco withdraw <montant>: §7Retirer de l'argent sur la banque de guilde");
                        p.sendMessage("");
                    }

                    //GUILD SHOW
                    if(args[0].equalsIgnoreCase("show")) {

                        if(gp == null || gp.getGuildUUID() == null) {
                            p.sendMessage(prefix + "§cVous n'avez pas de guilde !");
                            return false;
                        }

                        Guild g = guildManager.getGuild(gp.getGuildUUID());

                        String owner = "";
                        String admins = "";
                        String mods = "";
                        String members = "";

                        for(GPlayer gpa : g.getMembers()) {
                            if(gpa.getRank().equals(Rank.MEMBER)) {
                                members = members + Bukkit.getOfflinePlayer(gpa.getUUID()).getName() + " ";
                            }

                            if(gpa.getRank().equals(Rank.MODERATOR)) {
                                mods = mods + Bukkit.getOfflinePlayer(gpa.getUUID()).getName() + " ";
                            }

                            if(gpa.getRank().equals(Rank.ADMIN)) {
                                admins = admins + Bukkit.getOfflinePlayer(gpa.getUUID()).getName() + " ";
                            }

                            if(gpa.getRank().equals(Rank.OWNER)) {
                                owner = Bukkit.getOfflinePlayer(gpa.getUUID()).getName();
                            }
                        }

                        p.sendMessage("");
                        p.sendMessage("§b========== §6§l" + g.getName() + " §b==========");
                        p.sendMessage("");
                        p.sendMessage("§4§lOwner: §c" + owner);
                        p.sendMessage("§c§lAdmins: §c" + admins);
                        p.sendMessage("§b§lModérateurs: §b" + mods);
                        p.sendMessage("§a§lMembres: " + members);

                    }

                    //GUILD BYPASS
                    if(args[0].equalsIgnoreCase("bypass")) {
                        if(p.hasPermission("spartaguild.bypass")) {

                            if(guildManager.isBypass(p)) {
                                guildManager.removeBypass(p);
                                p.sendMessage("§cBypass désactivé");
                            } else {
                                guildManager.setBypass(p);
                                p.sendMessage("§aBypass activé");
                            }

                        } else {
                            p.sendMessage("§cVous n'avez pas la permission de faire cela !");
                        }
                    }

                }

                if(args.length == 2) {

                    //GUILD INFO <JOUEUR>
                    if(args[0].equalsIgnoreCase("info")) {

                        OfflinePlayer c = Bukkit.getOfflinePlayerIfCached(args[1]);

                        if(c == null) {
                            p.sendMessage(prefix + "§cCe joueur n'a jamais joué");
                            return false;
                        }

                        GPlayer gpc = guildManager.getGPlayer(c.getUniqueId());
                        Guild g = guildManager.getGuild(gpc.getGuildUUID());
                        p.sendMessage(prefix + "§a" + c.getName() + " est dans la guilde §6" + g.getName());

                    }

                    //GUILD SHOW <GUILDE>
                    if(args[0].equalsIgnoreCase("show")) {

                        Guild g = guildManager.getGuildByName(args[1]);
                        if(g == null) {
                            p.sendMessage(prefix + "§cCette guilde n'existe pas !");
                            return false;
                        }

                        String owner = "";
                        String admins = "";
                        String mods = "";
                        String members = "";

                        for(GPlayer gpa : g.getMembers()) {
                            if(gpa.getRank().equals(Rank.MEMBER)) {
                                members = members + Bukkit.getOfflinePlayer(gpa.getUUID()).getName() + " ";
                            }

                            if(gpa.getRank().equals(Rank.MODERATOR)) {
                                mods = mods + Bukkit.getOfflinePlayer(gpa.getUUID()).getName() + " ";
                            }

                            if(gpa.getRank().equals(Rank.ADMIN)) {
                                admins = admins + Bukkit.getOfflinePlayer(gpa.getUUID()).getName() + " ";
                            }

                            if(gpa.getRank().equals(Rank.OWNER)) {
                                owner = Bukkit.getOfflinePlayer(gpa.getUUID()).getName();
                            }
                        }

                        p.sendMessage("");
                        p.sendMessage("§b========== §6§l" + g.getName() + " §b==========");
                        p.sendMessage("");
                        p.sendMessage("§4§lOwner: §c" + owner);
                        p.sendMessage("§c§lAdmins: §c" + admins);
                        p.sendMessage("§b§lModérateurs: §b" + mods);
                        p.sendMessage("§a§lMembres: " + members);

                    }

                    //GUILD JOIN <GUILD>
                    if(args[0].equalsIgnoreCase("join")) {

                        String gName = args[1];

                        if(gp == null) {
                            gp = new GPlayer(p.getUniqueId());
                            guildManager.addGPlayer(gp);
                        }

                        if(gp.getGuildUUID() != null) {
                            p.sendMessage(prefix + "§cVous êtes déjà dans une guilde !");
                            return false;
                        }

                        if(guildManager.isBypass(p)) {
                            Guild j = guildManager.getGuildByName(gName);
                            guildManager.joinGuild(gp, j);
                            p.sendMessage(prefix + "§aVous avez bien rejoint la guilde " + j.getName());
                            return false;
                        }

                        for(UUID u : gp.getInvites()) {
                            if(guildManager.getGuild(u).getName().equals(gName)) {
                                Guild g = guildManager.getGuild(u);
                                for(GPlayer gpa : g.getMembers()) {
                                    if(Bukkit.getOfflinePlayer(gpa.getUUID()).isOnline()) {
                                        Bukkit.getPlayer(gpa.getUUID()).sendMessage(prefix + "§a" + p.getName() + " a rejoint la guilde !");
                                    }
                                }
                                guildManager.joinGuild(gp, g);
                                p.sendMessage(prefix + "§aVous avez bien rejoint la guilde " + g.getName());
                                return false;
                            }
                        }
                        p.sendMessage(prefix + "§cVous n'avez pas d'invitations en cours !");

                    }

                    //GUILD CREATE <GUILD>
                    if(args[0].equalsIgnoreCase("create")) {
                        String gName = args[1];

                        if(gp == null) {
                            gp = new GPlayer(p.getUniqueId());
                            guildManager.createGuild(gName, gp);
                            p.sendMessage(prefix + "§aVous avez bien créer la guilde " + gName);
                            guildManager.addGPlayer(gp);
                            return false;
                        }

                        if(gp.getGuildUUID() != null) {
                            p.sendMessage(prefix + "§cVous avez déjà une guilde !");
                            return false;
                        }

                        if(!guildManager.isValidName(gName)) {
                            p.sendMessage(prefix + "§cIl existe déjà une guilde a ce nom !");
                            return false;
                        }

                        guildManager.createGuild(gName, gp);
                        p.sendMessage(prefix + "§aVous avez bien créer la guilde §6" + gName);

                    }

                    //GUILD INVITE <JOUEUR>
                    if(args[0].equalsIgnoreCase("invite")) {
                        if(gp == null || gp.getGuildUUID() == null) {
                            p.sendMessage(prefix + "§cVous n'avez pas de guildes !");
                            return false;
                        }

                        OfflinePlayer c = Bukkit.getOfflinePlayerIfCached(args[1]);
                        if(c == null) {
                            p.sendMessage(prefix + "§cCe joueur n'a jamais joué !");
                            return false;
                        }

                        if(!guildManager.isBypass(p)) {
                            if(gp.getRank().equals(Rank.MEMBER)) {
                                p.sendMessage(prefix + "§cVous n'avez pas la permission de faire cela !");
                                return false;
                            }
                        }


                        GPlayer gpc = guildManager.getGPlayer(c.getUniqueId());

                        if(gpc == null) {
                            gpc = new GPlayer(c.getUniqueId());
                            guildManager.addGPlayer(gpc);
                        }

                        if(gpc.getGuildUUID() != null) {
                            p.sendMessage(prefix + "§cCe joueur est déjà dans une guilde !");
                            return false;
                        }

                        gpc.invite(gp.getGuildUUID());
                        p.sendMessage(prefix + "§aVous avez bien invité " + c.getName() + " dans votre guilde");
                        if(c.isOnline()) {
                            c.getPlayer().sendMessage(prefix + "§aVous avez reçu une invitation pour rejoindre la guilde " + guildManager.getGuild(gp.getGuildUUID()).getName());
                            return false;
                        }
                        guildManager.inviteMSG(c.getUniqueId(), gp.getGuildUUID());

                    }

                    //GUILD PROMOTE <JOUEUR>
                    if(args[0].equalsIgnoreCase("promote")) {
                        if (gp == null || gp.getGuildUUID() == null) {
                            p.sendMessage(prefix + "§cVous n'avez pas de guildes !");
                            return false;
                        }

                        if(!guildManager.isBypass(p)) {
                            if(gp.getRank().equals(Rank.MEMBER) || gp.getRank().equals(Rank.MODERATOR)) {
                                p.sendMessage(prefix + "§cVous n'avez pas la permission de faire cela !");
                                return false;
                            }
                        }

                        OfflinePlayer c = Bukkit.getOfflinePlayerIfCached((args[1]));
                        if(c == null) {
                            p.sendMessage(prefix + "§cCe joueur n'existe pas !");
                            return false;
                        }

                        if(c.getPlayer().equals(p)) {
                            p.sendMessage(prefix + "§cVous n'avez pas la permission de faire cela !");
                            return false;
                        }

                        GPlayer gpc = guildManager.getGPlayer(c.getUniqueId());
                        if(gpc == null || gpc.getGuildUUID() == null) {
                            p.sendMessage(prefix + "§cCe joueur n'est pas dans votre guilde !");
                            return false;
                        }

                        Guild g = guildManager.getGuild(gp.getGuildUUID());
                        if(!gpc.getGuildUUID().equals(g.getUUID())) {
                            p.sendMessage(prefix + "§cCe joueur n'est pas dans votre guilde !");
                            return false;
                        }

                        if(gpc.getRank().equals(Rank.ADMIN)) {
                            p.sendMessage(prefix + "§c/guild lead <player> pour promouvoir d'avantage");
                            return false;
                        }

                        gpc.promote();
                        p.sendMessage(prefix + "§aVous avez promu " + c.getName() + " au rang de " + gpc.getRank().getName());
                        guildManager.updateGPlayer(gpc);
                        if(c.isOnline()) {
                            c.getPlayer().sendMessage(prefix + "§aVous avez été promu au rang de " + gpc.getRank().getName());
                        }

                    }

                    //GUILD DEMOTE <JOUEUR>
                    if(args[0].equalsIgnoreCase("demote")) {
                        if (gp == null || gp.getGuildUUID() == null) {
                            p.sendMessage(prefix + "§cVous n'avez pas de guildes !");
                            return false;
                        }

                        if(!guildManager.isBypass(p)) {
                            if(gp.getRank().equals(Rank.MEMBER) || gp.getRank().equals(Rank.MODERATOR)) {
                                p.sendMessage(prefix + "§cVous n'avez pas la permission de faire cela !");
                                return false;
                            }
                        }

                        OfflinePlayer c = Bukkit.getOfflinePlayerIfCached(args[1]);
                        if(c == null) {
                            p.sendMessage(prefix + "§cCe joueur n'existe pas !");
                            return false;
                        }

                        if(c.getPlayer().equals(p)) {
                            p.sendMessage(prefix + "§cVous n'avez pas la permission de faire cela !");
                            return false;
                        }

                        GPlayer gpc = guildManager.getGPlayer(c.getUniqueId());
                        if(gpc == null || gpc.getGuildUUID() == null) {
                            p.sendMessage(prefix + "§cCe joueur n'est pas dans votre guilde !");
                            return false;
                        }

                        Guild g = guildManager.getGuild(gp.getGuildUUID());
                        if(!gpc.getGuildUUID().equals(g.getUUID())) {
                            p.sendMessage(prefix + "§cCe joueur n'est pas dans votre guilde !");
                            return false;
                        }

                        if(gpc.getRank().equals(Rank.MEMBER)) {
                            p.sendMessage(prefix + "§cCe joueur a déjà le grade le plus bas !");
                            return false;
                        }

                        if(gpc.getRank().equals(Rank.ADMIN) && gp.getRank().equals(Rank.ADMIN)) {
                            p.sendMessage(prefix + "§cVous ne pouvez pas rétrograder ce joueur !");
                            return false;
                        }

                        gpc.demote();
                        guildManager.updateGPlayer(gpc);
                        if(c.isOnline()) {
                            c.getPlayer().sendMessage(prefix + "§aVous avez été rétrogradé au rang de " + gpc.getRank().getName());
                        }
                        p.sendMessage(prefix + "§aVous avez rétrogradé " + c.getName() + " au rang de " + gpc.getRank().getName());
                    }

                    //GUILD RANK <JOUEUR>
                    if(args[0].equalsIgnoreCase("rank")) {
                        if (gp == null || gp.getGuildUUID() == null) {
                            p.sendMessage(prefix + "§cVous n'avez pas de guildes !");
                            return false;
                        }

                        OfflinePlayer c = Bukkit.getOfflinePlayerIfCached(args[1]);
                        if(c == null) {
                            p.sendMessage(prefix + "§cCe joueur n'existe pas !");
                            return false;
                        }

                        GPlayer gpc = guildManager.getGPlayer(c.getUniqueId());
                        if(gpc == null || gpc.getGuildUUID() == null) {
                            p.sendMessage(prefix + "§cCe joueur n'est pas dans votre guilde !");
                            return false;
                        }

                        Guild g = guildManager.getGuild(gp.getGuildUUID());
                        if(!gpc.getGuildUUID().equals(g.getUUID())) {
                            p.sendMessage(prefix + "§cCe joueur n'est pas dans votre guilde !");
                            return false;
                        }

                        p.sendMessage(prefix + "§6" + c.getName() + " §aà le role " + gpc.getRank().getName());

                    }

                    //GUILD LEAD <pseudo>
                    if(args[0].equalsIgnoreCase("lead")) {
                        if (gp == null || gp.getGuildUUID() == null) {
                            p.sendMessage(prefix + "§cVous n'avez pas de guildes !");
                            return false;
                        }

                        OfflinePlayer c = Bukkit.getOfflinePlayerIfCached(args[1]);
                        if(c == null) {
                            p.sendMessage(prefix + "§cCe joueur n'existe pas !");
                            return false;
                        }

                        if(c.getPlayer().equals(p)) {
                            p.sendMessage(prefix + "§cVous n'avez pas la permission de faire cela !");
                            return false;
                        }

                        GPlayer gpc = guildManager.getGPlayer(c.getUniqueId());
                        if(gpc == null || gpc.getGuildUUID() == null) {
                            p.sendMessage(prefix + "§cCe joueur n'est pas dans votre guilde !");
                            return false;
                        }

                        Guild g = guildManager.getGuild(gp.getGuildUUID());
                        if(!gpc.getGuildUUID().equals(g.getUUID())) {
                            p.sendMessage(prefix + "§cCe joueur n'est pas dans votre guilde !");
                            return false;
                        }

                        if(gp.getRank().equals(Rank.OWNER) || guildManager.isBypass(p)) {
                            gp.setRank(Rank.ADMIN);
                            gpc.setRank(Rank.OWNER);
                            p.sendMessage(prefix + "§aVous avez promu " + c.getName() + " au rang d'§cOwner");
                            if(c.isOnline()) {
                                c.getPlayer().sendMessage(prefix + "§aVous avez été promu au rang d'§cOwner");
                            }
                            return false;
                        }

                        p.sendMessage(prefix + "§cVous n'avez pas la permission de faire cela !");

                    }

                    //GUILD KICK <PSEUDO>
                    if(args[0].equalsIgnoreCase("kick")) {
                        if (gp == null || gp.getGuildUUID() == null) {
                            p.sendMessage(prefix + "§cVous n'avez pas de guildes !");
                            return false;
                        }

                        OfflinePlayer c = Bukkit.getOfflinePlayerIfCached(args[1]);
                        if(c == null) {
                            p.sendMessage(prefix + "§cCe joueur n'existe pas !");
                            return false;
                        }

                        if(c.getPlayer().equals(p)) {
                            p.sendMessage(prefix + "§cVous n'avez pas la permission de faire cela !");
                            return false;
                        }

                        GPlayer gpc = guildManager.getGPlayer(c.getUniqueId());
                        if(gpc == null || gpc.getGuildUUID() == null) {
                            p.sendMessage(prefix + "§cCe joueur n'est pas dans votre guilde !");
                            return false;
                        }

                        Guild g = guildManager.getGuild(gp.getGuildUUID());
                        if(!gpc.getGuildUUID().equals(g.getUUID())) {
                            p.sendMessage(prefix + "§cCe joueur n'est pas dans votre guilde !");
                            return false;
                        }

                        if(guildManager.isBypass(p)) {
                            guildManager.leaveGuild(gpc);
                            if(c.isOnline()) {
                                c.getPlayer().sendMessage(prefix + "§aVous avez été kick de la guilde !");
                            }
                            p.sendMessage(prefix + "§aVous avez bien kick " + c.getName() + " !");
                            return false;
                        }

                        switch(gp.getRank()) {
                            case MEMBER:
                                p.sendMessage(prefix + "§cVous n'avez pas la permission de faire cela !");
                                return false;
                            case MODERATOR:
                                if(!gpc.getRank().equals(Rank.MEMBER)) {
                                    p.sendMessage(prefix + "§cVous n'avez pas la permission de faire cela !");
                                }
                                return false;
                            case ADMIN:
                                if(gpc.getRank().equals(Rank.ADMIN) || gpc.getRank().equals(Rank.OWNER)) {
                                    p.sendMessage(prefix + "§cVous n'avez pas la permission de faire cela !");
                                }
                                return false;
                        }

                        guildManager.leaveGuild(gpc);
                        if(c.isOnline()) {
                            c.getPlayer().sendMessage(prefix + "§aVous avez été kick de la guilde !");
                        }
                        p.sendMessage(prefix + "§aVous avez bien kick " + c.getName() + " !");

                    }

                    //GUILD SETNAME <STRING>
                    if(args[0].equalsIgnoreCase("setname")) {
                        if (gp == null || gp.getGuildUUID() == null) {
                            p.sendMessage(prefix + "§cVous n'avez pas de guildes !");
                            return false;
                        }

                        if(!guildManager.isBypass(p)) {
                            if (!gp.getRank().equals(Rank.OWNER)) {
                                p.sendMessage(prefix + "§cVous n'avez pas la permission de faire cela !");
                                return false;
                            }
                        }

                        Guild g = guildManager.getGuild(gp.getGuildUUID());
                        g.setName(args[1]);
                        p.sendMessage(prefix + "§aLe nom de guilde a bien été modifié");
                        guildManager.updateGuild(g);

                    }

                    //GUILD ECO BAL
                    if(args[0].equalsIgnoreCase("eco") && args[1].equalsIgnoreCase("bal")) {
                        if (gp == null || gp.getGuildUUID() == null) {
                            p.sendMessage(prefix + "§cVous n'avez pas de guildes !");
                            return false;
                        }

                        Guild g = guildManager.getGuild(gp.getGuildUUID());
                        p.sendMessage(prefix + "§aBalance: " + g.getBalance());

                    }

                    if(args[0].equalsIgnoreCase("eco") && args[1].equalsIgnoreCase("baltop")) {

                        guildManager.baltop(p);

                    }

                }

                if(args.length == 3) {

                    //GUILD ECO
                    if(args[0].equalsIgnoreCase("eco")) {

                        if (gp == null || gp.getGuildUUID() == null) {
                            p.sendMessage(prefix + "§cVous n'avez pas de guildes !");
                            return false;
                        }

                        if(gp.getRank().equals(Rank.ADMIN)) {

                            //GUILD ECO DEPOSIT <MONTANT>
                            if (args[1].equalsIgnoreCase("deposit")) {

                                double amount = Double.parseDouble(args[2]);
                                Economy eco = SpartaGuild.getEconomy();
                                if (eco.getBalance(p) < amount) {
                                    p.sendMessage(prefix + "§cVous n'avez pas assez d'argent !");
                                    return false;
                                }

                                Guild g = guildManager.getGuild(gp.getGuildUUID());
                                g.deposit(amount, p);
                                p.sendMessage(prefix + "§aVous avez bien déposé " + String.valueOf(amount) + " sur la banque de guilde");
                                p.sendMessage(prefix + "§aNouveau solde: " + g.getBalance());
                                guildManager.updateGuild(g);

                            }

                            //GUILD ECO WITHDRAW <MONTANT>
                            if (args[1].equalsIgnoreCase("withdraw")) {

                                if(!guildManager.isBypass(p)) {
                                    if (!gp.getRank().equals(Rank.ADMIN) || gp.getRank().equals(Rank.OWNER)) {
                                        p.sendMessage(prefix + "§cVous n'avez pas la permission de faire cela !");
                                        return false;
                                    }
                                }

                                Guild g = guildManager.getGuild(gp.getGuildUUID());
                                double amount = Double.parseDouble(args[2]);
                                if (g.getBalance() < amount) {
                                    p.sendMessage(prefix + "§cVous n'avez pas assez d'argent !");
                                    return false;
                                }

                                g.withdraw(amount, p);
                                p.sendMessage(prefix + "§aVous avez bien pris " + String.valueOf(amount) + " sur la banque de guilde");
                                p.sendMessage(prefix + "§aNouveau solde: " + g.getBalance());
                                guildManager.updateGuild(g);

                            }

                        } else {
                            p.sendMessage(prefix + "§cVous n'avez pas la permission de faire cela !");
                        }

                    }

                }

            }

        }

        return false;
    }
}
