package fr.mrlaikz.spartaguild.enums;

import fr.mrlaikz.spartaguild.SpartaGuild;

public enum Rank {

    NULL("NULL"),
    MEMBER(SpartaGuild.getInstance().strConfig("chat.member")),
    MODERATOR(SpartaGuild.getInstance().strConfig("chat.moderator")),
    ADMIN(SpartaGuild.getInstance().strConfig("chat.admin")),
    OWNER(SpartaGuild.getInstance().strConfig("chat.owner"));

    private String name;

    Rank(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}
