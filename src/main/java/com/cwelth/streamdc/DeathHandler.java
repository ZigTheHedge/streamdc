package com.cwelth.streamdc;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;

public class DeathHandler {

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent e)
    {
        if(e.getEntity() instanceof ServerPlayer)
        {
            ServerPlayer ep = (ServerPlayer)e.getEntity();
            ModMain.proxy.saveDeath(ep);

            int pIndex = -1;
            for (int i = 0; i < ModMain.playerDeathCounters.size(); i++) {
                if(ModMain.playerDeathCounters.get(i).getUUID().equals(ep.getUUID().toString()))
                {
                    pIndex = i;
                    break;
                }
            }
            if(pIndex != -1) {
                ep.sendMessage(new TextComponent(ChatFormatting.RED + "You are dead now. Death count is: " + ModMain.playerDeathCounters.get(pIndex).getDeathCount() + ". Your rank is: " + PlayerDeathCounter.getRank(ep.getUUID().toString(), ModMain.playerDeathCounters)), Util.NIL_UUID);
                PlayerDeathCounter.sendRankTable(ep, ModMain.playerDeathCounters);
                Networking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> ep), new DeathPacket(ModMain.playerDeathCounters.get(pIndex).getDeathCount(), ep.getServer().getWorldData().getLevelName()));
                ModMain.instance.saveConfig();
            }
            else
                ModMain.logger.warning("No match!");
        }
    }
}
