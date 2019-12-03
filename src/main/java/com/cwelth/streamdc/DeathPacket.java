package com.cwelth.streamdc;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.Supplier;

public class DeathPacket {
    public int deathCount;
    public String prefix;

    public DeathPacket(){}

    public DeathPacket(int deathCount, String prefix)
    {
        this.deathCount = deathCount;
        this.prefix = prefix;
    }

    public DeathPacket(PacketBuffer buf) {
        deathCount = buf.readInt();
        prefix = buf.readString();
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeInt(deathCount);
        buf.writeString(prefix);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            try {
                Minecraft mc = Minecraft.getInstance();
                if(!mc.isIntegratedServerRunning())
                    prefix = mc.getCurrentServerData().serverIP;

                FileOutputStream output = new FileOutputStream(ModMain.proxy.getPath() + "/"+prefix+"_deathcounter.txt", false);
                output.write(Integer.toString(deathCount).getBytes());
                output.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
