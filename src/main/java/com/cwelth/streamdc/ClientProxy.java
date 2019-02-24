package com.cwelth.streamdc;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ClientProxy extends CommonProxy {
    @Override
    public String getPath() {
        final File file = Minecraft.getMinecraft().mcDataDir;
        try {
            return file.getCanonicalFile().getPath();
        } catch (final IOException e) {
            ModMain.logger.warning("Could not canonize path!");
        }
        return file.getPath();
    }

    @Override
    public void saveDeath(EntityPlayer entity) {
        super.saveDeath(entity);
    }
}
