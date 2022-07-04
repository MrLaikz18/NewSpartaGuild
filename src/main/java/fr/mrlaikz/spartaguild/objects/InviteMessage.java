package fr.mrlaikz.spartaguild.objects;

import java.util.UUID;

public class InviteMessage {

    private UUID player;
    private UUID guild;

    public InviteMessage(UUID player, UUID guild) {
        this.player = player;
        this.guild = guild;
    }

    public UUID getPlayer() {
        return player;
    }

    public UUID getGuild() {
        return guild;
    }

}
