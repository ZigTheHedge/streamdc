package com.cwelth.streamdc;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;

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

    public static void sendRankTable(ServerPlayer ep, List<PlayerDeathCounter> playerDeathCounters)
    {
        ep.sendMessage(new TextComponent(ChatFormatting.WHITE + "Top 5 deaths on server:"), Util.NIL_UUID);
        int maxIteration = (playerDeathCounters.size() > 5)? 5: playerDeathCounters.size();
        for (int i = 0; i < maxIteration; i++) {
            if(playerDeathCounters.get(i).getUUID().equals(ep.getUUID().toString()))
                ep.sendMessage(new TextComponent(ChatFormatting.getById(i+1) + "[->] "+playerDeathCounters.get(i).getNickname()+" - " + ChatFormatting.BOLD + playerDeathCounters.get(i).getDeathCount()), Util.NIL_UUID);
            else
                ep.sendMessage(new TextComponent(ChatFormatting.getById(i+1) + "[  ] " +playerDeathCounters.get(i).getNickname()+" - " + ChatFormatting.BOLD + playerDeathCounters.get(i).getDeathCount()), Util.NIL_UUID);
        }
    }
}
