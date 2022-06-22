package com.cwelth.streamdc;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class Networking {
    public static SimpleChannel INSTANCE;
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void registerMessages() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(ModMain.MODID, "networking"), () -> "1.0", s -> true, s -> true);

        INSTANCE.messageBuilder(DeathPacket.class, nextID(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(DeathPacket::new)
                .encoder(DeathPacket::toBytes)
                .consumer(DeathPacket::handle)
                .add();
    }
}
