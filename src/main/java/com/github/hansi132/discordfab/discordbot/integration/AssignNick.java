package com.github.hansi132.discordfab.discordbot.integration;

import com.github.hansi132.discordfab.DatabaseConnection;
import com.github.hansi132.discordfab.DiscordFab;
import com.github.hansi132.discordfab.discordbot.config.DataConfig;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

import java.sql.*;
import java.util.List;

public class AssignNick {
    public AssignNick(int linkKey) throws SQLException, ClassNotFoundException {
        Connection connection = new DatabaseConnection().connect();

        String selectSql = "SELECT DiscordId, McUsername FROM linkedaccounts WHERE LinkKey = ?;";
        PreparedStatement selectStmt = connection.prepareStatement(selectSql);
        selectStmt.setInt(1, linkKey);
        ResultSet resultSet = selectStmt.executeQuery();
        resultSet.next();
        String discordId = resultSet.getString("DiscordId");
        String mcUsername = resultSet.getString("McUsername");

        Guild guild = DiscordFab.getBot().getGuildById(DiscordFab.getInstance().getDataConfig().getProperty("guild"));
        User user = DiscordFab.getBot().getUserById(discordId);
        List<Role> role = guild.getRolesByName(DiscordFab.getInstance().getDataConfig().getProperty("role"), true);
        guild.getMember(user).modifyNickname(mcUsername).complete();
        guild.addRoleToMember(guild.getMember(user), role.get(0)).complete();
    }
}
