package com.cwelth.streamdc;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;

import java.io.File;
import java.io.IOException;

public class ClientProxy extends CommonProxy {
    @Override
    public String getPath() {
        final File file = Minecraft.getInstance().gameDir;
        try {
            return file.getCanonicalFile().getPath();
        } catch (final IOException e) {
            ModMain.logger.warning("Could not canonize path!");
        }
        return file.getPath();
    }

    @Override
    public void saveDeath(PlayerEntity entity) {
        super.saveDeath(entity);
    }
}
