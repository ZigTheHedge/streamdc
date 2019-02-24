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
    public String ep;

    public DeathPacket(){}

    public DeathPacket(String ep, int deathCount)
    {
        this.deathCount = deathCount;
        this.ep = ep;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        deathCount = buf.readInt();
        ep = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(deathCount);
        ByteBufUtils.writeUTF8String(buf, ep);
    }
    public static class Handler implements IMessageHandler<DeathPacket, IMessage> {
        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(final DeathPacket message, final MessageContext ctx) {
            int pIndex = -1;
            for (int i = 0; i < ModMain.playerDeathCounters.size(); i++) {
                if(ModMain.playerDeathCounters.get(i).getUUID().equals(message.ep))
                {
                    pIndex = i;
                    break;
                }
            }
            if(pIndex != -1) {
                try {
                    FileOutputStream output = new FileOutputStream(ModMain.proxy.getPath() + "/deathcounter.txt", false);
                    output.write(Integer.toString(ModMain.playerDeathCounters.get(pIndex).getDeathCount()).getBytes());
                    output.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else
                ModMain.logger.warning("No UUID found! UUID is: "+message.ep);

            return null;
        }

    }

}
