package com.cometproject.game.items.wired.conditions.positive;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.game.items.wired.base.WiredConditionItem;
import com.cometproject.server.game.rooms.types.Room;

public class WiredConditionHasHandItem extends WiredConditionItem {
    private final static int PARAM_HANDITEM = 0;

    public WiredConditionHasHandItem(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public int getInterface() {
        return 25;
    }

    @Override
    public boolean evaluate(RoomEntity entity, Object data) {
        if (entity == null || !(entity instanceof PlayerEntity)) return false;

        if (this.getWiredData().getParams().size() != 1) {
            return false;
        }

        int handItem = this.getWiredData().getParams().get(PARAM_HANDITEM);

        if (entity.getHandItem() == handItem) {
            return true;
        }

        return false;
    }
}
