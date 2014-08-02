package com.cometproject.server.network.messages.outgoing.room.engine;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class ForwardRoomMessageComposer {
    public static Composer compose(int roomId) {
        Composer msg = new Composer(Composers.ForwardRoomMessageComposer);

        msg.writeInt(roomId);

        return msg;
    }
}