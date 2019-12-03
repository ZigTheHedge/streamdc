package com.cwelth.streamdc;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class Networking {
    public static SimpleChannel INSTANCE;
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void registerMessages() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(ModMain.MODID, "networking"), () -> "1.0", s -> true, s -> true);

        INSTANCE.registerMessage(nextID(),
                DeathPacket.class,
                DeathPacket::toBytes,
                DeathPacket::new,
                DeathPacket::handle);
    }
}
