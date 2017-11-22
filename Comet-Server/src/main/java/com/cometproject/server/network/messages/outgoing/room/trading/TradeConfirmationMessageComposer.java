package com.cometproject.server.network.messages.outgoing.room.trading;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.messages.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class TradeConfirmationMessageComposer extends MessageComposer {

    @Override
    public short getId() {
        return Composers.TradingCompleteMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {

    }
}
