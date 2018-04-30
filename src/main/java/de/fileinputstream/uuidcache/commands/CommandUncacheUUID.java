package de.fileinputstream.uuidcache.commands;

import de.fileinputstream.uuidcache.UUIDCacheBootstrap;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * This class has been generated by Alexander on 30.04.18 18:06
 * You are not allowed to edit this resource or other components of it
 * © 2018 Alexander Fiedler
 */
public class CommandUncacheUUID implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender.isOp()) {
            if(strings.length == 1) {
                String name = strings[0];
                if((UUIDCacheBootstrap.getInstance().getRedisManager().getJedis().exists("uuidcache:" + name.toLowerCase() ))) {
                    UUIDCacheBootstrap.getInstance().getUuidCache().getUUID(name, s1 -> {
                        UUIDCacheBootstrap.getInstance().getUuidCache().uncacheUUID(name, s1);
                        commandSender.sendMessage(ChatColor.GREEN + "This uuid has been uncached now!");

                    });

                } else {
                    commandSender.sendMessage(ChatColor.DARK_RED + "This uuid hasn't been cached yet.");
                    return true;
                }
            } else {
                commandSender.sendMessage(ChatColor.DARK_RED + "§bPlease use /uncacheuuid <Name>");
                return true;
            }

        }
        return false;
    }
}
