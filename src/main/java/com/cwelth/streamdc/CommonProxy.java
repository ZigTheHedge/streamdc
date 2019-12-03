package com.cwelth.streamdc;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

public abstract class CommonProxy {
    public abstract String getPath();
    public void serverStarting(final FMLServerStartingEvent event) {
    }

    public void saveDeath(PlayerEntity entity)
    {
        int pIndex = -1;
        for (int i = 0; i < ModMain.playerDeathCounters.size(); i++) {
            if(ModMain.playerDeathCounters.get(i).getUUID().equals(entity.getUniqueID().toString()))
            {
                pIndex = i;
                break;
            }
        }
        if(pIndex == -1)
            ModMain.playerDeathCounters.add(new PlayerDeathCounter(entity.getDisplayName().getString(), entity.getUniqueID().toString(), 1));
        else
            ModMain.playerDeathCounters.get(pIndex).addDeath();
        ModMain.playerDeathCounters.sort((o1, o2) -> o2.getDeathCount() - o1.getDeathCount());
    }
}
