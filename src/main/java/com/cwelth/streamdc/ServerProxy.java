package com.cwelth.streamdc;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class ServerProxy extends CommonProxy {
    private WeakReference<MinecraftServer> serverWeakReference = null;

    @Override
    public void serverStarting(FMLServerStartingEvent event) {
        super.serverStarting(event);
        this.serverWeakReference = new WeakReference<MinecraftServer>(event.getServer());
    }

    @Override
    public void saveDeath(PlayerEntity entity) {
        super.saveDeath(entity);
    }

    @Override
    public String getPath() {
        final MinecraftServer server = this.serverWeakReference != null ? this.serverWeakReference.get() : null;
        final File file = server != null ? server.getFile(".") : new File(".");
        try {
            return file.getCanonicalFile().getPath();
        } catch (final IOException e) {
            ModMain.logger.warning("Could not canonize path!");
        }
        return file.getPath();
    }
}
