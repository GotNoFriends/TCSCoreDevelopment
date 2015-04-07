package com.tylerhyper.utils.core.commands;

import com.tylerhyper.utils.core.EnforcerEvent;
import com.tylerhyper.utils.core.TatsuCraftCore;
import com.tylerhyper.utils.core.Type;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(source = SourceType.ANY, permission = "tatsucraftcore.ban")
@CommandParameters(description = "Bans a player.", usage = "/ban <player> <reason>")
public class Command_ban extends BukkitCommand<TatsuCraftCore>
{
    private TatsuCraftCore plugin;
    @Override
    public boolean run(CommandSender sender, Command command, String commandLabel, String[] args) 
    {
        if (args.length == 0)
        {
            return false;
        }

        final Player player = getPlayer(args[0]);

        if (player == null)
        {
            sender.sendMessage(ChatColor.RED + "Player not found!");
            return true;
        }

        String reason = null;
        if (args.length >= 2)
        {
            reason = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");
        }
        server.dispatchCommand(sender, "co rb p:" + player.getName() + " t:24h r:global #silent");
        player.getInventory().clear();
        Player ban = plugin.getServer().getPlayer(args[0]);
        plugin.bannedPlayers.add(args[0]);
        player.setBanned(true);
        Bukkit.getServer().getPluginManager().callEvent(new EnforcerEvent(player, Type.BAN));
        sender.sendMessage(ChatColor.RED + args[0] + " has been banned from this server for" + reason);
        ban.kickPlayer("You have been banned for" + reason);
        Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "Player " + player.getName() + " has been banned by " + sender.getName() + "!");
        return true;    
    }
}