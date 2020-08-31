package com.github.hansi132.discordfab.discordbot.util;

import com.github.hansi132.discordfab.DiscordFab;
import com.github.hansi132.discordfab.discordbot.integration.UserSynchronizer;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.invite.GenericGuildInviteEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

public class InviteTracker {
    HashMap<Invite, Integer> inviteCache = new HashMap<>();

    public void cacheInvites(Guild guild) {
        inviteCache.clear();
        guild.retrieveInvites().queue(invites -> {
            for (Invite invite : invites) {
                inviteCache.put(invite, invite.getUses());
            }
        });
    }

    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {
        event.getGuild().retrieveInvites().queue(invites -> {
            for (Invite invite : invites) {
                int i = inviteCache.get(invite);
                if (invite.getUses() != i) {
                    User inviter = invite.getInviter();
                    if (inviter == null) return;
                    long inviterID = inviter.getIdLong();
                    long invitedID = event.getUser().getIdLong();
                    DiscordFab.LOGGER.info(inviter.getName() + " invited " + event.getUser().getName() + " to " + event.getGuild().getName());
                    this.addEntry(inviterID, invitedID);
                    this.cacheInvites(event.getGuild());
                    return;
                } else {
                    DiscordFab.LOGGER.info(invite.getUses() + "!=" + i);
                }
            }
            DiscordFab.LOGGER.error("This shouldn't have happened! A new member joined, but we couldn't detect who invited them!");
            this.cacheInvites(event.getGuild());
        });
    }

    public void onGuildInviteChange(@Nonnull GenericGuildInviteEvent event) {
        this.cacheInvites(event.getGuild());
    }

    public int getTotalInvites(long inviter) {
        return getInvitedIDs(inviter).size();
    }

    public int getValidInvites(Guild guild, long inviter) {
        int size = 0;
        for (long invited : getInvitedIDs(inviter)) {
            if (guild.getMemberById(invited) != null) size++;
        }
        return size;
    }

    public int getLinkedInvites(Guild guild, long inviter) {
        int size = 0;
        for (long invited : getInvitedIDs(inviter)) {
            if (UserSynchronizer.isLinked(invited) && guild.getMemberById(invited) != null) size++;
        }
        return size;
    }

    private List<Long> getInvitedIDs(long inviter) {
        List<Long> list = new ArrayList<>();
        LinkedHashSet<Long> hashSet = new LinkedHashSet<>();
        try {
            Connection connection = DatabaseConnection.connect();
            String selectSql = "SELECT * FROM trackedinvites WHERE InviterDiscordId = ?;";
            PreparedStatement insertStatement = connection.prepareStatement(selectSql);
            insertStatement.setLong(1, inviter);
            ResultSet rs = insertStatement.executeQuery();
            while (rs.next()) {
                long invitedID = rs.getLong("InvitedDiscordId");
                //Prevent duplicates
                if (hashSet.add(invitedID)) list.add(invitedID);
            }
        } catch (SQLException | ClassNotFoundException e) {
            DiscordFab.LOGGER.error("Could not query the database!", e);
        }
        return list;
    }

    private void addEntry(long inviter, long invited) {
        try {
            Connection connection = DatabaseConnection.connect();
            String insertSql = "INSERT INTO trackedinvites (InviterDiscordId, InvitedDiscordId) VALUES (?, ?);";
            PreparedStatement insertStatement = connection.prepareStatement(insertSql);
            insertStatement.setLong(1, inviter);
            insertStatement.setLong(2, invited);
            insertStatement.execute();
        } catch (ClassNotFoundException | SQLException e) {
            DiscordFab.LOGGER.error("Could not query the database!", e);
        }
    }
}
