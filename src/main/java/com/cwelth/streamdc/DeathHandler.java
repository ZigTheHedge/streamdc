package com.cwelth.streamdc;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkDirection;

public class DeathHandler {

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent e)
    {
        if(e.getEntity() instanceof ServerPlayerEntity)
        {
            ServerPlayerEntity ep = (ServerPlayerEntity)e.getEntity();
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
                ep.sendMessage(new StringTextComponent(TextFormatting.RED + "You are dead now. Death count is: " + ModMain.playerDeathCounters.get(pIndex).getDeathCount() + ". Your rank is: " + PlayerDeathCounter.getRank(ep.getUniqueID().toString(), ModMain.playerDeathCounters)));
                PlayerDeathCounter.sendRankTable(ep, ModMain.playerDeathCounters);
                Networking.INSTANCE.sendTo(new DeathPacket(ModMain.playerDeathCounters.get(pIndex).getDeathCount(), ep.getServerWorld().getWorldInfo().getWorldName()), ep.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
                ModMain.instance.saveConfig();
            }
            else
                ModMain.logger.warning("No match!");
        }
    }
}
