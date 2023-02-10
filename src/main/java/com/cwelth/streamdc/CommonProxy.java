package com.cwelth.streamdc;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.server.ServerStartingEvent;

public abstract class CommonProxy {
    public abstract String getPath();
    public void serverStarting(final ServerStartingEvent event) {
    }

    public void saveDeath(ServerPlayer entity)
    {
        int pIndex = -1;
        for (int i = 0; i < ModMain.playerDeathCounters.size(); i++) {
            if(ModMain.playerDeathCounters.get(i).getUUID().equals(entity.getUUID().toString()))
            {
                pIndex = i;
                break;
            }
        }
        if(pIndex == -1)
            ModMain.playerDeathCounters.add(new PlayerDeathCounter(entity.getDisplayName().getString(), entity.getUUID().toString(), 1));
        else
            ModMain.playerDeathCounters.get(pIndex).addDeath();
        ModMain.playerDeathCounters.sort((o1, o2) -> o2.getDeathCount() - o1.getDeathCount());
    }

    public void setDeathCount(ServerPlayer entity, int amount)
    {
        int pIndex = -1;
        for (int i = 0; i < ModMain.playerDeathCounters.size(); i++) {
            if(ModMain.playerDeathCounters.get(i).getUUID().equals(entity.getUUID().toString()))
            {
                pIndex = i;
                break;
            }
        }
        if(pIndex == -1)
            ModMain.playerDeathCounters.add(new PlayerDeathCounter(entity.getDisplayName().getString(), entity.getUUID().toString(), amount));
        else
            ModMain.playerDeathCounters.get(pIndex).setDeathCount(amount);
        ModMain.playerDeathCounters.sort((o1, o2) -> o2.getDeathCount() - o1.getDeathCount());
    }
}
