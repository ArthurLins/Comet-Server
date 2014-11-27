package com.cometproject.server.game.rooms.objects.items.types.floor.banzai;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.football.FootballScoreFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.GameType;

public class BanzaiTimerFloorItem extends RoomItemFloor {
    public BanzaiTimerFloorItem(int id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public void onInteract(GenericEntity entity, int requestData, boolean isWiredTriggered) {
        if (requestData == 2) {
            int time = Integer.parseInt(this.getExtraData());

            if (time == 0 || time == 30 || time == 60 || time == 120 || time == 180 || time == 300 || time == 600) {
                switch (time) {
                    default:
                        time = 0;
                        break;
                    case 0:
                        time = 30;
                        break;
                    case 30:
                        time = 60;
                        break;
                    case 60:
                        time = 120;
                        break;
                    case 120:
                        time = 180;
                        break;
                    case 180:
                        time = 300;
                        break;
                    case 300:
                        time = 600;
                        break;
                }
            } else {
                time = 0;
            }

            this.setExtraData(time + "");
            this.sendUpdate();
        } else {
            int gameLength = Integer.parseInt(this.getExtraData());

            if (this.getRoom().getGame().getInstance() == null) {
                this.getRoom().getGame().createNew(GameType.BANZAI);
                this.getRoom().getGame().getInstance().startTimer(gameLength);
            }
        }
    }
}