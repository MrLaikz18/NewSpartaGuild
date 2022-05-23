package fr.mrlaikz.spartaguild.objects;

import fr.mrlaikz.spartaguild.enums.Rank;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Guild {

    private String name;
    private UUID uuid;
    private List<GPlayer> members;
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
        owner.setRank(Rank.OWNER);
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

    //SETTERS
    public void addMember(GPlayer pl) {
        members.add(pl);
    }

    public void disband() {
        for(GPlayer p : members) {
            p.setGuild(null);
            p.setRank(null);
        }
        members.clear();
    }

}
