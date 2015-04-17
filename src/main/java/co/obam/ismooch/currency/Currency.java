package co.obam.ismooch.currency;

import co.obam.ismooch.obamapi.ObamAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

/**
 * Created by troyj_000 on 4/17/2015.
 */
public class Currency extends JavaPlugin implements Listener {


    public void onEnable() {

        getServer().getPluginManager().registerEvents(this, this);

    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {

            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("tickets")) {

                if (args.length < 1) {

                    Double tickets = ObamAPI.getTickets(player.getUniqueId());
                    player.sendRawMessage(
                            ChatColor.YELLOW + "Tickets " + ChatColor.WHITE + "| " + ChatColor.GREEN + tickets + " " +
                                    ChatColor.WHITE + "..::.. More Info: obam.co/tickets");
                    return true;

                } else if (args[0].equalsIgnoreCase("give")) {

                    if (!player.hasPermission("obam.smod")) {

                        player.sendRawMessage(ChatColor.RED + "You do not have permission to do this!");
                        return true;

                    } else if (args.length < 2) {

                        player.sendRawMessage(ChatColor.RED + "You must specify a player! " + ChatColor.YELLOW +
                                "tickets give [player] [amount] [reason]");
                        return true;

                    } else if (args.length < 3) {

                        player.sendRawMessage(
                                ChatColor.RED + "You must specify an amount! " + ChatColor.YELLOW + "tickets give " +
                                        args[1] + " [amount] [reason]");
                        return true;
                    } else if (args.length < 4) {

                        player.sendRawMessage(
                                ChatColor.RED + "You must specify a reason!" + ChatColor.YELLOW + " tickets give " +
                                        args[1] + " " + args[2] + " [reason]");
                        return true;

                    } else if (!ObamAPI.isOBAMPlayer(args[1])) {

                        player.sendRawMessage(
                                ChatColor.YELLOW + args[1] + ChatColor.RED + " is not registered player!");
                        return true;

                    } else {

                        StringBuilder buffer = new StringBuilder();

                        for (int i = 3; i < args.length; i++) {
                            buffer.append(' ').append(args[i]);
                        }
                        UUID uuid = ObamAPI.getUUID(args[1]);
                        ObamAPI.addTickets(uuid, Double.parseDouble(args[2]), player.getName(), buffer.toString());
                        player.sendRawMessage(
                                ChatColor.GREEN + "You have added " + ChatColor.YELLOW + args[2] + " Tickets " +
                                        ChatColor.GREEN + " to the account of " + ChatColor.YELLOW + args[1] +
                                        ChatColor.GREEN + "!");
                        return true;

                    }


                }

            }

        }

        return false;
    }


}
