package com.cometproject.server.game.commands.staff.banning;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.moderation.BanManager;
import com.cometproject.server.game.moderation.types.BanType;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.sessions.Session;
import org.apache.commons.lang3.StringUtils;

import java.util.List;


public class IpBanCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length < 2) {
            sendNotif(Locale.getOrDefault("command.params.length", "Oops! You did something wrong!"), client);
            return;
        }

        String username = params[0];
        int length = StringUtils.isNumeric(params[1]) ? Integer.parseInt(params[1]) : 0;

        Session user = NetworkManager.getInstance().getSessions().getByPlayerUsername(username);

        if (user == null) {
            sendNotif(Locale.getOrDefault("command.user.offline", "This user is offline!"), client);
            return;
        }

        if (user == client || !user.getPlayer().getPermissions().getRank().bannable()) {
            sendNotif(Locale.getOrDefault("command.user.notbannable", "You're not able to ban this user!"), client);
            return;
        }

        long expire = Comet.getTime() + (length * 3600);

        String ipAddress = user.getIpAddress();

        if (BanManager.getInstance().hasBan(ipAddress, BanType.IP)) {
            sendNotif("IP: " + ipAddress + " is already banned.", client);
            return;
        }

        BanManager.getInstance().banPlayer(BanType.IP, user.getIpAddress(), length, expire, params.length > 2 ? this.merge(params, 2) : "", client.getPlayer().getId());

        sendNotif("User has been IP banned (IP: " + ipAddress + ")", client);

        List<Integer> playerIds = PlayerManager.getInstance().getPlayerIdsByIpAddress(ipAddress);

        for (int playerId : playerIds) {
            Session player = NetworkManager.getInstance().getSessions().getByPlayerId(playerId);

            if (player != null) {
                player.disconnect("banned");
            }
        }

        playerIds.clear();
    }

    @Override
    public String getPermission() {
        return "ipban_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.ban", "%username% %time% %reason%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.ipban.description");
    }

    @Override
    public boolean bypassFilter() {
        return true;
    }

}
