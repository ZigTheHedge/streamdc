package com.cwelth.streamdc;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class DeathCommandHandler extends CommandBase {
    @Override
    public String getName() {
        return "dc";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/dc";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(sender instanceof EntityPlayer) {
            int rank = PlayerDeathCounter.getRank(((EntityPlayer)sender).getUniqueID().toString(), ModMain.playerDeathCounters);
            if(rank == -1)
                sender.sendMessage(new TextComponentString(TextFormatting.WHITE + "Your DeathCounter rank is: Out of Ranks (no single death registered!)"));
            else
                sender.sendMessage(new TextComponentString(TextFormatting.WHITE + "Your DeathCounter rank is: " + rank));
            PlayerDeathCounter.sendRankTable((EntityPlayer) sender, ModMain.playerDeathCounters);
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }
}
