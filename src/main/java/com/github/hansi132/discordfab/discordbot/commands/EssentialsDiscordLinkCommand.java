package com.github.hansi132.discordfab.discordbot.commands;

import com.github.hansi132.discordfab.discordbot.api.text.Messages;
import com.github.hansi132.discordfab.discordbot.util.DatabaseConnection;
import com.github.hansi132.discordfab.discordbot.util.LinkKeyCreator;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import org.apache.commons.collections4.MapUtils;
import org.kilocraft.essentials.api.command.EssentialCommand;
import org.kilocraft.essentials.api.user.OnlineUser;
import org.kilocraft.essentials.util.messages.nodes.ExceptionMessageNode;
import org.kilocraft.essentials.util.text.Texter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class EssentialsDiscordLinkCommand extends EssentialCommand {
    public EssentialsDiscordLinkCommand(String label, String[] alias) {
        super(label, alias);
    }

    @Override
    public void register(CommandDispatcher<ServerCommandSource> commandDispatcher) {
        this.argumentBuilder.executes(this::execute);
    }

    private int execute(final CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        OnlineUser user = this.getOnlineUser(ctx);
        Random random = new Random();
        int testKey = random.nextInt(10000);
        int linkKey;

        try {
            Connection connection = new DatabaseConnection().get();
            linkKey = new LinkKeyCreator().checkKey(testKey);

            String selectSql = "SELECT McUUID FROM linkedaccounts WHERE McUUID = ?;";
            PreparedStatement selectStatement = connection.prepareStatement(selectSql);
            selectStatement.setString(1, user.getUuid().toString());
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next() && resultSet.getLong("DiscordID") != 0L) {
                user.sendError("You're account is already linked!");
                return FAILED;
            }

            user.sendMessage(getText(linkKey));

            String insertSql = "INSERT INTO linkedaccounts (LinkKey, McUUID, McUsername) VALUES (?,?,?);";
            PreparedStatement insertStatement = connection.prepareStatement(insertSql);
            insertStatement.setInt(1, linkKey);
            insertStatement.setString(2, user.getUuid().toString());
            insertStatement.setString(3, user.getUsername());
            insertStatement.execute();

            connection.close();
        } catch (SQLException e) {
            user.sendError(
                    Texter.newText("Unexpected database error.").styled((style) ->
                            style.setHoverEvent(Texter.Events.onHover(Messages.getInnermostMessage(e))))
            );
        } catch (ClassNotFoundException e) {
            user.sendError(ExceptionMessageNode.UNKNOWN_COMMAND_EXCEPTION);
        }

        return SUCCESS;
    }

    private static MutableText getText(final int linkKey) {
        return Texter.newText().append(
                Texter.newText("Your link key is: ").append(
                        Texter.newText(String.valueOf(linkKey)).formatted(Formatting.AQUA).styled((style) ->
                                style.setHoverEvent(Texter.Events.onHover("Click to Copy"))
                                        .withClickEvent(
                                                new ClickEvent(
                                                        ClickEvent.Action.COPY_TO_CLIPBOARD,
                                                        String.valueOf(linkKey)
                                                )
                                        )
                        )
                )
        );
    }
}
