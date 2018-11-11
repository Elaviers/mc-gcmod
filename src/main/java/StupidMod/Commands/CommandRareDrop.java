package StupidMod.Commands;


import net.minecraft.command.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CommandRareDrop extends CommandBase
{
    List alias;
    String[] MobNames = {"blaze","creeper","enderman","ghast","zombie pigman","skeleton","slime","spider","witch","zombie","bat","chicken","cow","horse","ocelot","pig","rabbit","sheep","squid","villager","wolf", "rogue fortnite player"};
    
    public CommandRareDrop() {
        alias = new ArrayList();
        alias.add("rd");
    }
    
    @Override
    public List<String> getAliases() {
        return this.alias;
    }
    
    @Override
    public String getName()
    {
        return "raredrop";
    }
    
    @Override
    public int getRequiredPermissionLevel() { return 2; }
    
    @Override
    public String getUsage(ICommandSender sender)
    {
        return "commands.rare_drop.usage";
    }
    
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 1)
            throw new WrongUsageException("command.raredrop.usage");
        else {
            EntityPlayerMP mpplayer = CommandBase.getCommandSenderAsPlayer(sender);
            Item item = CommandBase.getItemByText(sender, args[0]);
            int amount = 1,dmg = 0;
        
            if (args.length >= 2)
                mpplayer = CommandBase.getPlayer(server,sender,args[1]);
            if(args.length >= 3)
                amount = CommandBase.parseInt(args[2]);
            if(args.length >= 4)
                dmg = CommandBase.parseInt(args[3]);
        
            ItemStack stack = new ItemStack(item,amount,dmg);
            EntityItem itementity = mpplayer.dropItem(stack, false);
            itementity.setNoPickupDelay();
            int ind = (int) Math.round(Math.random() * (MobNames.length - 1));
        
            Iterator poo = server.getPlayerList().getPlayers().iterator();
        
            while (poo.hasNext()) {
                EntityPlayer moron = (EntityPlayer) poo.next();
                moron.sendMessage(new TextComponentTranslation("command.raredrop.success", new Object[] {MobNames[ind]}));
                moron.sendMessage(new TextComponentTranslation("command.raredrop.success2", new Object[] {sender.getDisplayName(),amount,stack.getDisplayName()}));
            }
        }
    }
    
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, Item.REGISTRY.getKeys());
        }
        else if (args.length == 2)
        {
            return  getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        
        return Collections.emptyList();
    }
    
    @Override
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 1;
    }
}