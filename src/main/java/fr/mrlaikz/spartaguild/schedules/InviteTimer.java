package fr.mrlaikz.spartaguild.schedules;

import fr.mrlaikz.spartaguild.SpartaGuild;
import fr.mrlaikz.spartaguild.objects.GPlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class InviteTimer extends BukkitRunnable {

    private GPlayer pl;
    private UUID guild;
    private int timer = 120;

    public InviteTimer(GPlayer pl, UUID guild) {
        this.pl = pl;
        this.guild = guild;
    }

    @Override
    public void run() {
        if(timer == 0) {
            pl.removeInvite(guild);
            cancel();
        }
        timer--;
    }
}
