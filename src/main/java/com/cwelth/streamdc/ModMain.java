package com.cwelth.streamdc;

import com.google.gson.Gson;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by zth on 24/02/19.
 */


@Mod(ModMain.MODID)
public class ModMain {

    public static final String NAME = "Stream Death Counter";
    public static final String MODID = "streamdc";
    public static List<PlayerDeathCounter> playerDeathCounters = new ArrayList<>();

    public static final Logger logger = Logger.getLogger(NAME);
    public static ModMain instance;

    public ModMain()
    {
        instance = this;
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        loadConfig();
        MinecraftForge.EVENT_BUS.register(new DeathHandler());
        MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());
        Networking.registerMessages();
    }

    public void saveConfig()
    {
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
    }

    public void loadConfig()
    {
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
    }

    public static CommonProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

    public class ForgeEventHandlers {

        @SubscribeEvent
        public void registerCommands(RegisterCommandsEvent event) {
            CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
            LiteralCommandNode<CommandSourceStack> cmdsSDC = dispatcher.register(
                    Commands.literal("dc")
                            .requires( cs -> cs.getEntity() instanceof ServerPlayer )
                            .executes( cs -> {
                                ServerPlayer sender = cs.getSource().getPlayerOrException();

                                int rank = PlayerDeathCounter.getRank(sender.getUUID().toString(), ModMain.playerDeathCounters);

                                if(rank == -1)
                                    sender.sendMessage(new TextComponent(ChatFormatting.WHITE + "Your DeathCounter rank is: Out of Ranks (no single death registered!)"), Util.NIL_UUID);
                                else {
                                    sender.sendMessage(new TextComponent(ChatFormatting.WHITE + "Your DeathCounter rank is: " + rank + "(" + ModMain.playerDeathCounters.get(rank-1).getDeathCount() + " death(s))"), Util.NIL_UUID);
                                }
                                PlayerDeathCounter.sendRankTable(sender, ModMain.playerDeathCounters);
                                return 0;
                            })
            );
        }

        @SubscribeEvent
        public void serverLoad(ServerStartingEvent event) {
            proxy.serverStarting(event);
        }

        @SubscribeEvent
        public void serverStop(ServerStoppingEvent event) {
            saveConfig();
        }

    }

}
