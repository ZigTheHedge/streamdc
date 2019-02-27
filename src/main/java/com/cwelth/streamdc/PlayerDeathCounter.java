package com.cwelth.streamdc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.util.List;

public class PlayerDeathCounter {
    private String UUID;
    private String nickname;
    private int deathCount;


    public PlayerDeathCounter(String nickname, String UUID, int deathCount) {
        this.UUID = UUID;
        this.deathCount = deathCount;
        this.nickname = nickname;
    }

    public int getDeathCount() {
        return deathCount;
    }

    public String getUUID() {
        return UUID;
    }

    public String getNickname() { return nickname; }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public int addDeath() {
        this.deathCount++;
        return this.deathCount;
    }

    public static int getRank(String UUID, List<PlayerDeathCounter> playerDeathCounters)
    {
        for (int i = 0; i < playerDeathCounters.size(); i++) {
            if(playerDeathCounters.get(i).getUUID().equals(UUID))return i+1;
        }
        return -1;
    }

    public static void sendRankTable(EntityPlayer ep, List<PlayerDeathCounter> playerDeathCounters)
    {
        ep.sendMessage(new TextComponentString(TextFormatting.WHITE + "Top 5 deaths on server:"));
        int maxIteration = (playerDeathCounters.size() > 5)? 5: playerDeathCounters.size();
        for (int i = 0; i < maxIteration; i++) {
            if(playerDeathCounters.get(i).getUUID().equals(ep.getUniqueID().toString()))
                ep.sendMessage(new TextComponentString(TextFormatting.fromColorIndex(i+1) + "[->] "+playerDeathCounters.get(i).getNickname()+" - " + TextFormatting.BOLD + playerDeathCounters.get(i).getDeathCount()));
            else
                ep.sendMessage(new TextComponentString(TextFormatting.fromColorIndex(i+1) + "[  ] " +playerDeathCounters.get(i).getNickname()+" - " + TextFormatting.BOLD + playerDeathCounters.get(i).getDeathCount()));
        }
    }
}
