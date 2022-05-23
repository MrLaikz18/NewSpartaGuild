package fr.mrlaikz.spartaguild.objects;

import fr.mrlaikz.spartaguild.enums.Rank;
import fr.mrlaikz.spartaguild.SpartaGuild;
import fr.mrlaikz.spartaguild.schedules.InviteTimer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GPlayer {

    private UUID uuid;
    private UUID guild;
    private Rank rank;
    private boolean chat;
    private HashMap<UUID, InviteTimer> invites;

    public GPlayer(UUID uuid, UUID guild, Rank rank, boolean chat) {
        this.uuid = uuid;
        this.guild = guild;
        this.rank = rank;
        this.chat = chat;
        this.invites = new HashMap<>();
    }

    public GPlayer(UUID uuid) {
        this.uuid = uuid;
        this.guild = null;
        this.rank = null;
        this.chat = false;
        this.invites = new HashMap<>();
    }

    //GETTERS
    public UUID getUUID() {
        return uuid;
    }

    public UUID getGuildUUID() {
        return guild;
    }

    public Rank getRank() {
        return rank;
    }

    public boolean getChat() {
        return chat;
    }

    public List<UUID> getInvites() {
        List<UUID> ret = new ArrayList<UUID>();
        for(UUID u : invites.keySet()) {
            ret.add(u);
        }
        return ret;
    }

    //SETTERS
    public void setGuild(UUID guild) {
        this.guild = guild;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public void invite(UUID uuid) {
        InviteTimer timer = new InviteTimer(this, uuid);
        timer.runTaskTimerAsynchronously(SpartaGuild.getInstance(), 0, 20);
        invites.put(uuid, timer);
    }

    public void removeInvite(UUID uuid) {
        invites.remove(uuid);
    }

}
