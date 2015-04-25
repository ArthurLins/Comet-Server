package com.cometproject.server.network.messages.incoming.user.youtube;

import com.cometproject.server.game.players.components.types.settings.PlaylistItem;
import com.cometproject.server.game.players.types.PlayerSettings;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateFloorItemMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.youtube.PlayVideoMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.youtube.PlaylistMessageComposer;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;

import java.util.List;


public class LoadPlaylistMessageEvent implements Event {

    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int itemId = msg.readInt();

        RoomItemFloor item = client.getPlayer().getEntity().getRoom().getItems().getFloorItem(itemId);

        if (item == null)
            return;

        PlayerSettings playerSettings;

        //if(client.getPlayer().getId() != item.getOwner()) {
        //    return;
        //}

        playerSettings = PlayerDao.getSettingsById(item.getOwner());

        if (playerSettings == null) {
            playerSettings = client.getPlayer().getSettings();
        }

        int playingId = 0;

        if (item.hasAttribute("video")) {
            for (int i = 0; i < playerSettings.getPlaylist().size(); i++) {
                if (playerSettings.getPlaylist().get(i).getVideoId().equals(item.getAttribute("video"))) {
                    playingId = i;
                }
            }
        }

        List<PlaylistItem> playlist = playerSettings.getPlaylist();
        if(playlist != null) {
            client.send(new PlaylistMessageComposer(itemId, playlist, playingId));

            if (playlist.size() > 0) {
                PlaylistItem video = playlist.get(playingId);
                client.send(new PlayVideoMessageComposer(itemId, video.getVideoId(), video.getDuration()));

                item.setAttribute("video", video.getVideoId());

                client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new UpdateFloorItemMessageComposer(item));
            }
        }
    }
}