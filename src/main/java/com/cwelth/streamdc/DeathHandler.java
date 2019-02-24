package com.cwelth.streamdc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.io.File;

public class DeathHandler {

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent e)
    {
        if(e.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer ep = (EntityPlayer)e.getEntity();
            ModMain.proxy.saveDeath(ep);

            int pIndex = -1;
            for (int i = 0; i < ModMain.playerDeathCounters.size(); i++) {
                if(ModMain.playerDeathCounters.get(i).getUUID().equals(ep.getUniqueID().toString()))
                {
                    pIndex = i;
                    break;
                }
            }
            if(pIndex != -1) {
                ep.sendMessage(new TextComponentString("You are dead now. Death count is: " + ModMain.playerDeathCounters.get(pIndex).getDeathCount()));
                ModMain.network.sendTo(new DeathPacket(ep.getUniqueID().toString(), ModMain.playerDeathCounters.get(pIndex).getDeathCount()), (EntityPlayerMP) ep);
            }
            else
                ModMain.logger.warning("No match!");
        }
    }
}
