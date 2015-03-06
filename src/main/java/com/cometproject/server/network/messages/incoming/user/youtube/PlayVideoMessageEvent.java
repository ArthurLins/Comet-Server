package com.cometproject.server.network.messages.incoming.user.youtube;

import com.cometproject.server.game.players.components.types.settings.PlaylistItem;
import com.cometproject.server.game.players.types.PlayerSettings;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateFloorItemMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.youtube.PlayVideoMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.youtube.PlaylistMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;
import org.apache.commons.lang.StringUtils;


public class PlayVideoMessageEvent implements IEvent {

    @Override
    public void handle(Session client, Event msg) throws Exception {
        int itemId = msg.readInt();

        String videoIdStr = msg.readString();
        int videoId = videoIdStr.isEmpty() ? 0 : StringUtils.isNumeric(videoIdStr) ? Integer.parseInt(videoIdStr) : 0;

        RoomItemFloor item = client.getPlayer().getEntity().getRoom().getItems().getFloorItem(itemId);

        PlayerSettings playerSettings;

        playerSettings = PlayerDao.getSettingsById(item.getOwner());

        if (playerSettings == null) {
            playerSettings = client.getPlayer().getSettings();
        }

        if (client.getPlayer().getId() != item.getOwner()) {
            if (item.hasAttribute("video")) {
                for (int i = 0; i < playerSettings.getPlaylist().size(); i++) {
                    if (playerSettings.getPlaylist().get(i).getVideoId().equals(item.getAttribute("video"))) {
                        PlaylistItem playlistItem = playerSettings.getPlaylist().get(i);

                        //client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new PlayVideoMessageComposer(itemId, playlistItem.getVideoId(), playlistItem.getDuration()));
                        client.send(new PlayVideoMessageComposer(itemId, playlistItem.getVideoId(), playlistItem.getDuration()));
                    }
                }
            }

            return;
        }

        PlaylistItem playlistItem = playerSettings.getPlaylist().get(videoId);
        client.send(new PlaylistMessageComposer(itemId, playerSettings.getPlaylist(), videoId));


        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new PlayVideoMessageComposer(itemId, playlistItem.getVideoId(), playlistItem.getDuration()));

        item.setAttribute("video", playlistItem.getVideoId());

        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new UpdateFloorItemMessageComposer(item, client.getPlayer().getEntity().getRoom().getData().getOwnerId()));
    }
}
