package com.cometproject.server.game.players.components;

import com.cometproject.api.game.players.data.components.PlayerPermissions;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.permissions.PermissionsManager;
import com.cometproject.server.game.permissions.types.CommandPermission;
import com.cometproject.server.game.permissions.types.OverrideCommandPermission;
import com.cometproject.server.game.permissions.types.Rank;
import com.cometproject.server.game.players.types.Player;


public class PermissionComponent implements PlayerPermissions {
    private Player player;

    public PermissionComponent(Player player) {
        this.player = player;
    }

    @Override
    public Rank getRank() {
        return PermissionsManager.getInstance().getRank(this.player.getData().getRank());
    }

    @Override
    public boolean hasCommand(String key) {
        if (this.player.getData().getRank() == 255) {
            return true;
        }

        if (PermissionsManager.getInstance().getOverrideCommands().containsKey(key)) {
            OverrideCommandPermission permission = PermissionsManager.getInstance().getOverrideCommands().get(key);

            if (permission.getPlayerId() == this.getPlayer().getData().getId() && permission.isEnabled()) {
                return true;
            }
        }

        if (PermissionsManager.getInstance().getCommands().containsKey(key)) {
            CommandPermission permission = PermissionsManager.getInstance().getCommands().get(key);

            if (permission.getMinimumRank() <= this.getPlayer().getData().getRank()) {
                boolean hasCommand = !permission.vipOnly() || player.getData().isVip();

                if (permission.getRightsOverride() == CommandPermission.RoomRights.NONE) {
                    return hasCommand;
                }

                if (hasCommand) {
                    if (this.getPlayer().getPermissions().getRank().roomFullControl() ||
                            this.getPlayer().getId() == this.getPlayer().getEntity().getRoom().getId()) {
                        return true;
                    } else if (permission.getRightsOverride() == CommandPermission.RoomRights.RIGHTS &&
                            player.getEntity().getRoom().getRights().hasRights(this.getPlayer().getId())) {
                        return true;
                    }
                }

                return false;
            }

            return (key.equals("debug") && Comet.isDebugging) || key.equals("dev");
        }

        return false;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public void dispose() {

    }
}