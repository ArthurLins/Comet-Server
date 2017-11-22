package com.cometproject.server.game.groups.types.components.forum.threads;

import com.cometproject.api.game.groups.types.components.forum.IForumThread;
import com.cometproject.api.game.groups.types.components.forum.IForumThreadReply;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.api.game.players.data.PlayerAvatar;
import com.cometproject.server.storage.queries.groups.GroupForumThreadDao;

public class ForumThreadReply implements IForumThreadReply {
    private int id;
    private int index;

    private String message;
    private int threadId;
    private int authorId;
    private int authorTimestamp;

    private int state;

    private int adminId;
    private String adminUsername;

    public ForumThreadReply(int id, int index, String message, int threadId, int authorId, int authorTimestamp, int state, int adminId, String adminUsername) {
        this.id = id;
        this.index = index;
        this.message = message;
        this.threadId = threadId;
        this.authorId = authorId;
        this.authorTimestamp = authorTimestamp;
        this.state = state;
        this.adminId = adminId;
        this.adminUsername = adminUsername;
    }
    
    @Override
    public void compose(IComposer msg) {
        final PlayerAvatar playerAvatar = PlayerManager.getInstance().getAvatarByPlayerId(this.getAuthorId(),
                PlayerAvatar.USERNAME_FIGURE);

        msg.writeInt(this.getId());
        msg.writeInt(this.index);

        msg.writeInt(this.getAuthorId());
        msg.writeString(playerAvatar.getUsername());
        msg.writeString(playerAvatar.getFigure());

        msg.writeInt((int) Comet.getTime() - this.getAuthorTimestamp());
        msg.writeString(this.getMessage());
        msg.writeByte(this.getState()); // state

        msg.writeInt(this.adminId); // _adminId
        msg.writeString(this.adminUsername); // _adminName
        msg.writeInt(0); // _adminOperationTimeAsSeccondsAgo
        msg.writeInt(GroupForumThreadDao.getPlayerMessageCount(playerAvatar.getId())); // messages by author todo: optimise if needed
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int getAuthorId() {
        return authorId;
    }

    @Override
    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    @Override
    public int getAuthorTimestamp() {
        return authorTimestamp;
    }

    @Override
    public void setAuthorTimestamp(int authorTimestamp) {
        this.authorTimestamp = authorTimestamp;
    }

    @Override
    public int getThreadId() {
        return threadId;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int getState() {
        return state;
    }

    @Override
    public void setState(int state) {
        this.state = state;
    }

    @Override
    public int getAdminId() {
        return adminId;
    }

    @Override
    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    @Override
    public String getAdminUsername() {
        return adminUsername;
    }

    @Override
    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }
}
