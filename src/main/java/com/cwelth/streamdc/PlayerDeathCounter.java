package com.cwelth.streamdc;

public class PlayerDeathCounter {
    private String UUID;
    private int deathCount;


    public PlayerDeathCounter(String UUID, int deathCount)
    {
        this.UUID = UUID;
        this.deathCount = deathCount;
    }

    public int getDeathCount() {
        return deathCount;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public int addDeath()
    {
        this.deathCount++;
        return this.deathCount;
    }
}
