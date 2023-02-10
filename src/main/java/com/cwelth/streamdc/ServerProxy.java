package com.cwelth.streamdc;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.server.ServerStartingEvent;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class ServerProxy extends CommonProxy {
    private WeakReference<MinecraftServer> serverWeakReference = null;

    @Override
    public void serverStarting(ServerStartingEvent event) {
        super.serverStarting(event);
        this.serverWeakReference = new WeakReference<MinecraftServer>(event.getServer());
    }

    @Override
    public void saveDeath(ServerPlayer entity) {
        super.saveDeath(entity);
    }

    @Override
    public void setDeathCount(ServerPlayer entity, int amount) {
        super.setDeathCount(entity, amount);
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
