package fr.mrlaikz.spartaguild.objects;

import fr.mrlaikz.spartaguild.SpartaGuild;
import fr.mrlaikz.spartaguild.enums.Rank;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Guild {

    private String name;
    private UUID uuid;
    private List<GPlayer> members = new ArrayList<GPlayer>();
    private Economy eco;

    public Guild(String name, UUID uuid, List<GPlayer> members, double bal) {
        this.name = name;
        this.uuid = uuid;
        this.members = members;
        this.eco = new Economy(uuid, bal);
    }

    public Guild(String name, UUID uuid, double bal) {
        this.name = name;
        this.uuid = uuid;
        this.eco = new Economy(uuid, bal);
    }

    public Guild(String name, GPlayer owner) {
        this.name = name;
        this.uuid = UUID.randomUUID();
        this.members = new ArrayList<GPlayer>();
        members.add(owner);
        this.eco = new Economy(this.uuid, 0);
    }

    //GETTERS
    public String getName() {
        return name;
    }

    public UUID getUUID() {
        return uuid;
    }

    public List<GPlayer> getMembers() {
        return members;
    }

    public double getBalance() {
        return eco.getBalance();
    }

    //SETTERS
    public void addMember(GPlayer pl) {
        members.add(pl);
    }

    public void removeMember(GPlayer pl) {
        members.remove(pl);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void disband() {
        members.clear();
    }

    public void deposit(double amount, Player p) {
        this.eco.deposit(amount);
        SpartaGuild.getEconomy().withdrawPlayer(p, amount);
    }

    public void withdraw(double amount, Player p) {
        this.eco.withdraw(amount);
        SpartaGuild.getEconomy().depositPlayer(p, amount);
    }

}
