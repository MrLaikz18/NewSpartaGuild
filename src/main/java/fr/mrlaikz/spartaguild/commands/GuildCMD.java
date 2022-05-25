package fr.mrlaikz.spartaguild.commands;

import fr.mrlaikz.spartaguild.SpartaGuild;
import fr.mrlaikz.spartaguild.database.SQLGetter;
import fr.mrlaikz.spartaguild.enums.Rank;
import fr.mrlaikz.spartaguild.manager.GuildManager;
import fr.mrlaikz.spartaguild.objects.GPlayer;
import fr.mrlaikz.spartaguild.objects.Guild;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
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

    public GuildCMD(SpartaGuild plugin) {
        this.plugin = plugin;
        this.sql = new SQLGetter(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(command.getName().equalsIgnoreCase("guild")) {

            if(sender instanceof Player) {
                Player p = (Player) sender;
                GPlayer gp = guildManager.getGPlayer(p.getUniqueId());

                if(args.length == 0) {
                    p.sendMessage("§b========== §6§lSparta'§c§lGuild §b==========");
                    p.sendMessage("");
                    p.sendMessage("§6/guild: §7Affichage de l'aide générale"); //FAIT
                    p.sendMessage("§6/guild eco: §7Affichage de l'aide économique"); //FAIT
                    p.sendMessage("§6/guild chat: §7Basculer en chat normal / guild");
                    p.sendMessage("§6/guild join <nom>: §7Rejoindre une guilde"); //FAIT
                    p.sendMessage("§6/guild create <nom>: §7Créer une guilde"); //FAIT
                    p.sendMessage("§6/guild leave: §7Quitter une guilde"); //FAIT
                    p.sendMessage("§6/guild disband: §7Dissoudre une guilde"); //FAIT
                    p.sendMessage("§6/guild invite <pseudo>: §7Inviter un joueur dans sa guilde"); //FAIT
                    p.sendMessage("§6/guild promote <pseudo>: §7Augmenter le rank d'un joueur"); //FAIT
                    p.sendMessage("§6/guild demote <pseudo>: §7Baisser le rank d'un joueur"); //FAIT
                    p.sendMessage("§6/guild rank <pseudo>: §7Obtenir le rank d'un joueur");  //FAIT
                }

                if(args.length == 1) {

                    //GUILD SPY
                    if(args[0].equalsIgnoreCase("spy") && p.hasPermission("spartaguild.spy")) {
                        if(guildManager.isSpy(p)) {
                            guildManager.removeSpy(p);
                            p.sendMessage("§cSpy Guild désactivé");
                        } else {
                            guildManager.addSpy(p);
                            p.sendMessage("§aSpy Guild activé");
                        }
                    }

                    //GUILD CHAT
                    if(args[0].equalsIgnoreCase("chat")) {
                        if (gp == null || gp.getGuildUUID() == null) {
                            p.sendMessage("§cVous n'avez pas de guildes !");
                            return false;
                        }

                        gp.setChat(!gp.getChat());
                        if(gp.getChat()) {
                            p.sendMessage("§aChat de Guild activé");
                        } else {
                            p.sendMessage("§aChat de Guild désactivé");
                        }

                    }

                    //GUILD LEAVE
                    if(args[0].equalsIgnoreCase("leave")) {
                        if(gp == null || gp.getGuildUUID() == null) {
                            p.sendMessage("§cVous n'avez pas de guildes !");
                            return false;
                        }
                        guildManager.leaveGuild(gp);
                        p.sendMessage("§aVous avez bien quitté votre guilde");
                    }

                    //GUILD DISBAND
                    if(args[0].equalsIgnoreCase("disband")) {
                        if(gp == null || gp.getGuildUUID() == null) {
                            p.sendMessage("§cVous n'avez pas de guildes !");
                            return false;
                        }

                        if(!gp.getRank().equals(Rank.OWNER)) {
                            p.sendMessage("§cVous n'avez pas la permission de faire cela !");
                            return false;
                        }
                        guildManager.disbandGuild(gp.getGuildUUID());
                        p.sendMessage("§aVotre guilde a bien été dissoute");

                    }

                    //GUILD ECO
                    if(args[0].equalsIgnoreCase("eco")) {
                        p.sendMessage("§b========== §6§lSparta'§c§lGuild §b==========");
                        p.sendMessage("");
                        p.sendMessage("§6/guild eco: §7Affichage de l'aide économique");
                        p.sendMessage("§6/guild eco bal: §7Affichage de la balance");
                        p.sendMessage("§6/guild eco deposit <montant>: §7Déposer de l'argent sur la banque de guilde");
                        p.sendMessage("§6/guild eco withdraw <montant>: §7Retirer de l'argent sur la banque de guilde");
                    }

                }

                if(args.length == 2) {

                    //GUILD JOIN <GUILD>
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

                    //GUILD CREATE <GUILD>
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

                    //GUILD INVITE <JOUEUR>
                    if(args[0].equalsIgnoreCase("invite")) {
                        if(gp == null || gp.getGuildUUID() == null) {
                            p.sendMessage("§cVous n'avez pas de guildes !");
                            return false;
                        }

                        Player c = Bukkit.getPlayer(args[1]);
                        if(c == null) {
                            p.sendMessage("§cCe joueur n'existe pas !");
                            return false;
                        }

                        if(gp.getRank().equals(Rank.MEMBER)) {
                            p.sendMessage("§cVous n'avez pas la permission de faire cela !");
                            return false;
                        }

                        GPlayer gpc = guildManager.getGPlayer(c.getUniqueId());
                        if(gpc == null) {
                            gpc = new GPlayer(c.getUniqueId());
                            guildManager.addGPlayer(gpc);
                        }

                        if(gpc.getGuildUUID() != null) {
                            p.sendMessage("§cCe joueur est déjà dans une guilde !");
                            return false;
                        }

                        Guild g = guildManager.getGuild(gp.getGuildUUID());
                        gpc.invite(g.getUUID());
                        p.sendMessage("§aVous avez bien invité " + c.getName() + " dans votre guilde");
                        c.sendMessage("§aVous avez reçu une invtation pour rejoindre la guilde " + g.getName());

                    }

                    //GUILD PROMOTE <JOUEUR>
                    if(args[0].equalsIgnoreCase("promote")) {
                        if (gp == null || gp.getGuildUUID() == null) {
                            p.sendMessage("§cVous n'avez pas de guildes !");
                            return false;
                        }

                        if(gp.getRank().equals(Rank.MEMBER) || gp.getRank().equals(Rank.MODERATOR)) {
                            p.sendMessage("§cVous n'avez pas la permission de faire cela !");
                            return false;
                        }

                        Player c = Bukkit.getPlayer(args[1]);
                        if(c == null) {
                            p.sendMessage("§cCe joueur n'existe pas !");
                            return false;
                        }

                        GPlayer gpc = guildManager.getGPlayer(c.getUniqueId());
                        if(gpc == null || gpc.getGuildUUID() == null) {
                            p.sendMessage("§cCe joueur n'est pas dans votre guilde !");
                            return false;
                        }

                        Guild g = guildManager.getGuild(gp.getGuildUUID());
                        if(!gpc.getGuildUUID().equals(g.getUUID())) {
                            p.sendMessage("§cCe joueur n'est pas dans votre guilde !");
                            return false;
                        }

                        gpc.promote();
                        p.sendMessage("§aVous avez promu " + c.getName() + " au rang de " + gpc.getRank().getName());
                        c.sendMessage("§aVous avez été promu au rang de " + gpc.getRank().getName());
                        //UPDATE PLAYER C

                    }

                    //GUILD DEMOTE <JOUEUR>
                    if(args[0].equalsIgnoreCase("demote")) {
                        if (gp == null || gp.getGuildUUID() == null) {
                            p.sendMessage("§cVous n'avez pas de guildes !");
                            return false;
                        }

                        if(gp.getRank().equals(Rank.MEMBER) || gp.getRank().equals(Rank.MODERATOR)) {
                            p.sendMessage("§cVous n'avez pas la permission de faire cela !");
                            return false;
                        }

                        Player c = Bukkit.getPlayer(args[1]);
                        if(c == null) {
                            p.sendMessage("§cCe joueur n'existe pas !");
                            return false;
                        }

                        GPlayer gpc = guildManager.getGPlayer(c.getUniqueId());
                        if(gpc == null || gpc.getGuildUUID() == null) {
                            p.sendMessage("§cCe joueur n'est pas dans votre guilde !");
                            return false;
                        }

                        Guild g = guildManager.getGuild(gp.getGuildUUID());
                        if(!gpc.getGuildUUID().equals(g.getUUID())) {
                            p.sendMessage("§cCe joueur n'est pas dans votre guilde !");
                            return false;
                        }

                        if(gpc.getRank().equals(Rank.MEMBER)) {
                            p.sendMessage("§cCe joueur a déjà le grade le plus bas !");
                            return false;
                        }

                        if(gpc.getRank().equals(Rank.ADMIN) || gp.getRank().equals(Rank.ADMIN)) {
                            p.sendMessage("§cVous ne pouvez pas rétrograder ce joueur !");
                            return false;
                        }

                        gpc.demote();
                        p.sendMessage("§aVous avez rétrogradé " + c.getName() + " au rang de " + gpc.getRank().getName());
                        c.sendMessage("§aVous avez été rétrogradé au rang de " + gpc.getRank().getName());
                        //UPDATE PLAYER C

                    }

                    //GUILD RANK <JOUEUR>
                    if(args[0].equalsIgnoreCase("rank")) {
                        if (gp == null || gp.getGuildUUID() == null) {
                            p.sendMessage("§cVous n'avez pas de guildes !");
                            return false;
                        }

                        Player c = Bukkit.getPlayer(args[1]);
                        if(c == null) {
                            p.sendMessage("§cCe joueur n'existe pas !");
                            return false;
                        }

                        GPlayer gpc = guildManager.getGPlayer(c.getUniqueId());
                        if(gpc == null || gpc.getGuildUUID() == null) {
                            p.sendMessage("§cCe joueur n'est pas dans votre guilde !");
                            return false;
                        }

                        Guild g = guildManager.getGuild(gp.getGuildUUID());
                        if(!gpc.getGuildUUID().equals(g.getUUID())) {
                            p.sendMessage("§cCe joueur n'est pas dans votre guilde !");
                            return false;
                        }

                        p.sendMessage(c.getName() + " §aà le role " + gpc.getRank().getName());

                    }

                    //GUILD ECO BAL
                    if(args[0].equalsIgnoreCase("eco") && args[1].equalsIgnoreCase("bal")) {
                        if (gp == null || gp.getGuildUUID() == null) {
                            p.sendMessage("§cVous n'avez pas de guildes !");
                            return false;
                        }

                        Guild g = guildManager.getGuild(gp.getGuildUUID());
                        p.sendMessage("§aBalance: " + g.getBalance());

                    }

                }

                if(args.length == 3) {

                    //GUILD ECO
                    if(args[0].equalsIgnoreCase("eco")) {

                        if (gp == null || gp.getGuildUUID() == null) {
                            p.sendMessage("§cVous n'avez pas de guildes !");
                            return false;
                        }

                        //GUILD ECO DEPOSIT <MONTANT>
                        if(args[1].equalsIgnoreCase("deposit")) {

                            double amount = Double.parseDouble(args[2]);
                            Economy eco = SpartaGuild.getEconomy();
                            if(eco.getBalance(p) < amount) {
                                p.sendMessage("§cVous n'avez pas assez d'argent !");
                                return false;
                            }

                            Guild g = guildManager.getGuild(gp.getGuildUUID());
                            g.deposit(amount, p);
                            p.sendMessage("§aVous avez bien déposé " + String.valueOf(amount) + " sur la banque de guilde");
                            p.sendMessage("§aNouveau solde: " + g.getBalance());
                            //UPDATE GUILD G

                        }

                        //GUILD ECO WITHDRAW <MONTANT>
                        if(args[1].equalsIgnoreCase("deposit")) {

                            Guild g = guildManager.getGuild(gp.getGuildUUID());
                            double amount = Double.parseDouble(args[2]);
                            if(g.getBalance() < amount) {
                                p.sendMessage("§cVous n'avez pas assez d'argent !");
                                return false;
                            }

                            g.withdraw(amount, p);
                            p.sendMessage("§aVous avez bien pris " + String.valueOf(amount) + " sur la banque de guilde");
                            p.sendMessage("§aNouveau solde: " + g.getBalance());
                            //UPDATE GUILD G

                        }

                    }

                }

            }

        }

        return false;
    }
}
