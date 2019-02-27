package com.cwelth.streamdc;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DeathPacket implements IMessage {
    public int deathCount;

    public DeathPacket(){}

    public DeathPacket(int deathCount)
    {
        this.deathCount = deathCount;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        deathCount = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(deathCount);
    }
    public static class Handler implements IMessageHandler<DeathPacket, IMessage> {
        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(final DeathPacket message, final MessageContext ctx) {
            ModMain.logger.info("Message Catched!");
            try {
                FileOutputStream output = new FileOutputStream(ModMain.proxy.getPath() + "/deathcounter.txt", false);
                output.write(Integer.toString(message.deathCount).getBytes());
                output.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

}
