package com.cwelth.streamdc;

import com.google.gson.Gson;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by zth on 24/02/19.
 */


@Mod(modid = ModMain.MODID, name = ModMain.NAME, version = ModMain.VERSION, acceptableRemoteVersions = "*")
public class ModMain {

    public static final String NAME = "Stream Death Counter";
    public static final String MODID = "streamdc";
    public static final String VERSION = "1.1";
    public static List<PlayerDeathCounter> playerDeathCounters = new ArrayList<>();

    public Configuration config;

    public static final Logger logger = Logger.getLogger(NAME);

    public static SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

    public void saveConfig()
    {
        config.load();

        try {
            FileWriter deaths = new FileWriter(this.proxy.getPath()+"/deaths.json");
            for (PlayerDeathCounter dtItem: playerDeathCounters
                 ) {
                Gson dtJson = new Gson();
                deaths.write(String.format("%s%n", dtJson.toJson(dtItem)));
            }
            deaths.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        config.save();
    }

    public void loadConfig()
    {
        config.load();
        playerDeathCounters.clear();
        try {
            BufferedReader deaths = new BufferedReader(new FileReader(this.proxy.getPath()+"/deaths.json"));
            String jsonItem = deaths.readLine();
            while(jsonItem != null)
            {
                Gson dtJson = new Gson();
                playerDeathCounters.add(dtJson.fromJson(jsonItem, PlayerDeathCounter.class));
                jsonItem = deaths.readLine();
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }

        config.save();
    }

    @Mod.Instance(ModMain.MODID)
    public static ModMain instance;

    @SidedProxy(serverSide = "com.cwelth.streamdc.ServerProxy", clientSide = "com.cwelth.streamdc.ClientProxy")
    public static CommonProxy proxy;


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e)
    {
        config = new Configuration(e.getSuggestedConfigurationFile());

        loadConfig();

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e)
    {
        MinecraftForge.EVENT_BUS.register(new DeathHandler());
        network.registerMessage(DeathPacket.Handler.class, DeathPacket.class, 0, Side.CLIENT);

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e)
    {
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new DeathCommandHandler());
        proxy.serverStarting(event);
    }

    @Mod.EventHandler
    public void serverStop(FMLServerStoppingEvent event)
    {
        saveConfig();
    }

}
