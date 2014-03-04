package com.cometproject.server.network.clients;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.network.NetworkEngine;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.log4j.Logger;

public class ClientHandler extends SimpleChannelInboundHandler<Event> {
    private static Logger log = Logger.getLogger(ClientHandler.class.getName());

    private final boolean CLOSE_ON_ERROR = false;

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        //log.debug("Channel [" + ctx.channel().attr(NetworkEngine.UNIQUE_ID_KEY).get().toString() + "] connected");

        if(!Comet.getServer().getNetwork().getSessions().add(ctx.channel())) {
            ctx.channel().disconnect();
        }

        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        try {
            Session client = ctx.channel().attr(NetworkEngine.SESSION_ATTRIBUTE_KEY).get();
            client.onDisconnect();
        } catch(Exception e) { }

        Comet.getServer().getNetwork().getSessions().remove(ctx.channel());
        log.debug("Channel [" + ctx.channel().attr(NetworkEngine.UNIQUE_ID_KEY).get().toString() + "] disconnected");

        ctx.fireChannelInactive();
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, Event msg) throws Exception {
        try {
            Session client = ctx.channel().attr(NetworkEngine.SESSION_ATTRIBUTE_KEY).get();

            if(client != null) {
                Comet.getServer().getNetwork().getMessages().handle(msg, client);
            }
        } catch(Exception e) {
            log.error("Error while receiving message", e);
        }
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (ctx.channel().isActive()) {
            log.error("Exception in ClientHandler : " + cause.getMessage());

            cause.printStackTrace();

            if (CLOSE_ON_ERROR) { ctx.close(); }
        }
    }
}