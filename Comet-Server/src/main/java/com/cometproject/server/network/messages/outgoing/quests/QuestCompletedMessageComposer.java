package com.cometproject.server.network.messages.outgoing.quests;

import com.cometproject.api.game.players.IPlayer;
import com.cometproject.api.game.quests.IQuest;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.quests.types.Quest;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class QuestCompletedMessageComposer extends MessageComposer {
    private IQuest quest;
    private IPlayer player;

    public QuestCompletedMessageComposer(IQuest quest, IPlayer player) {
        this.quest = quest;
        this.player = player;
    }

    @Override
    public short getId() {
        return Composers.QuestCompletedMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        quest.compose(this.player, msg);

        msg.writeBoolean(this.player.getQuests().hasCompletedQuest(this.quest.getId()));
    }
}
