package com.cwelth.streamdc;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;

import java.io.File;
import java.io.IOException;

public class ClientProxy extends CommonProxy {
    @Override
    public String getPath() {
        final File file = Minecraft.getInstance().gameDirectory;
        try {
            return file.getCanonicalFile().getPath();
        } catch (final IOException e) {
            ModMain.logger.warning("Could not canonize path!");
        }
        return file.getPath();
    }

    @Override
    public void saveDeath(ServerPlayer entity) {
        super.saveDeath(entity);
    }

    @Override
    public void setDeathCount(ServerPlayer entity, int amount) {
        super.setDeathCount(entity, amount);
    }
}
