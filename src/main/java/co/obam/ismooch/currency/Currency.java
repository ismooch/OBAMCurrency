package co.obam.ismooch.currency;

import co.obam.ismooch.obamapi.ObamAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class Currency extends JavaPlugin implements Listener {


    public void onEnable() {

        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new Allowance(), this);
        getServer().getPluginManager().registerEvents(new TAM(), this);
        TAM.startTam(this);

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
                                ChatColor.YELLOW + args[1] + ChatColor.RED + " is not a registered player!");
                        return true;

                    } else {

                        StringBuilder buffer = new StringBuilder();

                        for (int i = 3; i < args.length; i++) {
                            buffer.append(' ').append(args[i]);
                        }
                        UUID uuid = ObamAPI.getUUID(args[1]);
                        ObamAPI.addTickets(uuid, Double.parseDouble(args[2]), player.getName(), buffer.toString());
                        player.sendRawMessage(
                                ChatColor.GREEN + "You have added " + ChatColor.YELLOW + args[2] + "Tickets " +
                                        ChatColor.GREEN + "to the account of " + ChatColor.YELLOW + args[1] +
                                        ChatColor.GREEN + "!");
                        if (Bukkit.getPlayer(args[1]).isOnline()) {

                            Bukkit.getPlayer(args[1]).sendRawMessage(
                                    ChatColor.YELLOW + player.getName() + ChatColor.GREEN + " has added " +
                                            ChatColor.YELLOW + args[2] + " Tickets" + ChatColor.GREEN +
                                            " to your account!");

                        }
                        return true;

                    }


                } else if (args[0].equalsIgnoreCase("remove")) {

                    if (!player.hasPermission("obam.smod")) {

                        player.sendRawMessage(ChatColor.RED + "You do not have permission to do this!");
                        return true;

                    } else if (args.length < 2) {

                        player.sendRawMessage(ChatColor.RED + "You need to specify a player! " + ChatColor.YELLOW +
                                "tickets remove [player] [amount] [reason]");
                        return true;

                    } else if (args.length < 3) {

                        player.sendRawMessage(ChatColor.RED + "You need to specify an amount! " + ChatColor.YELLOW +
                                "tickets remove " + args[1] + " [amount] [reason]");
                        return true;

                    } else if (args.length < 4) {

                        player.sendRawMessage(ChatColor.RED + "You need to specify a reason! " + ChatColor.YELLOW +
                                "tickets remove " + args[1] + " " + args[2] + " [reason]");
                        return true;

                    } else if (!ObamAPI.isOBAMPlayer(args[1])) {

                        player.sendRawMessage(
                                ChatColor.YELLOW + args[1] + ChatColor.RED + " is not a registered player!");
                        return true;

                    } else {

                        StringBuilder buffer = new StringBuilder();
                        for (int i = 3; i < args.length; i++) {
                            buffer.append(' ').append(args[i]);
                        }
                        UUID uuid = ObamAPI.getUUID(args[1]);
                        if (Double.parseDouble(args[2]) > ObamAPI.getTickets(uuid)) {

                            player.sendRawMessage(
                                    ChatColor.YELLOW + args[1] + ChatColor.RED + " does not have that many Tickets!");
                            return true;

                        }
                        ObamAPI.openTConnection();
                        ObamAPI.removeTickets(uuid, Double.parseDouble(args[2]), player.getName(), buffer.toString());
                        player.sendRawMessage(
                                ChatColor.GREEN + "You have removed " + ChatColor.YELLOW + args[2] + " Tickets " +
                                        ChatColor.GREEN + "from the account of " + ChatColor.YELLOW + args[1] +
                                        ChatColor.GREEN + "!");
                        ObamAPI.closeTConnection();
                        if (Bukkit.getPlayer(args[1]).isOnline()) {

                            Bukkit.getPlayer(args[1]).sendRawMessage(
                                    ChatColor.YELLOW + player.getName() + ChatColor.GREEN + " has removed " +
                                            ChatColor.YELLOW + args[2] + " Tickets" + ChatColor.GREEN +
                                            " from your account!");

                        }
                        return true;

                    }

                } else if (args[0].equalsIgnoreCase("view")) {

                    if (!player.hasPermission("obam.mod")) {

                        player.sendRawMessage(ChatColor.RED + "You do not have permission to do this!");
                        return true;

                    } else if (args.length < 2) {

                        player.sendRawMessage(ChatColor.RED + "You need to specify a player! " + ChatColor.YELLOW +
                                "tickets view [player]");
                        return true;

                    } else if (!ObamAPI.isOBAMPlayer(args[1])) {

                        player.sendRawMessage(
                                ChatColor.YELLOW + args[1] + ChatColor.RED + " is not a registered player!");
                        return true;

                    } else {

                        UUID uuid = ObamAPI.getUUID(args[1]);
                        Double tickets = ObamAPI.getTickets(uuid);
                        player.sendRawMessage(ChatColor.YELLOW + args[1] + ChatColor.YELLOW + "'s " + ChatColor.GREEN +
                                "Ticket balance is " + ChatColor.YELLOW + tickets);
                        return true;

                    }

                }

            } else if (cmd.getName().equalsIgnoreCase("obucks")) {

                if (args.length < 1) {

                    Double obucks = ObamAPI.getObucks(player.getUniqueId());
                    player.sendRawMessage(
                            ChatColor.DARK_AQUA + "O" + ChatColor.YELLOW + "Bucks " + ChatColor.WHITE + "| " +
                                    ChatColor.GREEN + obucks + " " +
                                    ChatColor.WHITE + "..::.. More Info: obam.co/obucks");
                    return true;

                } else if (args[0].equalsIgnoreCase("give")) {

                    if (!player.hasPermission("obam.smod")) {

                        player.sendRawMessage(ChatColor.RED + "You do not have permission to do this!");
                        return true;

                    } else if (args.length < 2) {

                        player.sendRawMessage(ChatColor.RED + "You must specify a player! " + ChatColor.YELLOW +
                                "obucks give [player] [amount] [reason]");
                        return true;

                    } else if (args.length < 3) {

                        player.sendRawMessage(
                                ChatColor.RED + "You must specify an amount! " + ChatColor.YELLOW + "obucks give " +
                                        args[1] + " [amount] [reason]");
                        return true;
                    } else if (args.length < 4) {

                        player.sendRawMessage(
                                ChatColor.RED + "You must specify a reason!" + ChatColor.YELLOW + " obucks give " +
                                        args[1] + " " + args[2] + " [reason]");
                        return true;

                    } else if (!ObamAPI.isOBAMPlayer(args[1])) {

                        player.sendRawMessage(
                                ChatColor.YELLOW + args[1] + ChatColor.RED + " is not a registered player!");
                        return true;

                    } else {

                        StringBuilder buffer = new StringBuilder();

                        for (int i = 3; i < args.length; i++) {
                            buffer.append(' ').append(args[i]);
                        }
                        UUID uuid = ObamAPI.getUUID(args[1]);
                        ObamAPI.addObucks(uuid, Double.parseDouble(args[2]), player.getName(), buffer.toString());
                        player.sendRawMessage(
                                ChatColor.GREEN + "You have added " + ChatColor.YELLOW + args[2] + ChatColor.DARK_AQUA +
                                        "O" + ChatColor.YELLOW + "Bucks " +
                                        ChatColor.GREEN + "to the account of " + ChatColor.YELLOW + args[1] +
                                        ChatColor.GREEN + "!");

                        if (Bukkit.getPlayer(args[1]).isOnline()) {

                            Bukkit.getPlayer(args[1]).sendRawMessage(
                                    ChatColor.YELLOW + player.getName() + ChatColor.GREEN + " has added " +
                                            ChatColor.YELLOW + args[2] + ChatColor.DARK_AQUA + " O" + ChatColor.YELLOW +
                                            "Bucks " + ChatColor.GREEN + " to your account!");

                        }
                        return true;

                    }


                } else if (args[0].equalsIgnoreCase("remove")) {

                    if (!player.hasPermission("obam.smod")) {

                        player.sendRawMessage(ChatColor.RED + "You do not have permission to do this!");
                        return true;

                    } else if (args.length < 2) {

                        player.sendRawMessage(ChatColor.RED + "You need to specify a player! " + ChatColor.YELLOW +
                                "obucks remove [player] [amount] [reason]");
                        return true;

                    } else if (args.length < 3) {

                        player.sendRawMessage(ChatColor.RED + "You need to specify an amount! " + ChatColor.YELLOW +
                                "obucks remove " + args[1] + " [amount] [reason]");
                        return true;

                    } else if (args.length < 4) {

                        player.sendRawMessage(
                                ChatColor.RED + "You need to specify a reason! " + ChatColor.YELLOW + "obucks remove " +
                                        args[1] + " " + args[2] + " [reason]");
                        return true;

                    } else if (!ObamAPI.isOBAMPlayer(args[1])) {

                        player.sendRawMessage(
                                ChatColor.YELLOW + args[1] + ChatColor.RED + " is not a registered player!");
                        return true;

                    } else {

                        StringBuilder buffer = new StringBuilder();
                        for (int i = 3; i < args.length; i++) {
                            buffer.append(' ').append(args[i]);
                        }
                        UUID uuid = ObamAPI.getUUID(args[1]);
                        if (Double.parseDouble(args[2]) > ObamAPI.getObucks(uuid)) {

                            player.sendRawMessage(
                                    ChatColor.YELLOW + args[1] + ChatColor.RED + " does not have that many OBucks!");
                            return true;

                        }
                        ObamAPI.removeObucks(uuid, Double.parseDouble(args[2]), player.getName(), buffer.toString());
                        player.sendRawMessage(
                                ChatColor.GREEN + "You have removed " + ChatColor.YELLOW + args[2] +
                                        ChatColor.DARK_AQUA + "O" + ChatColor.YELLOW + "Bucks " +
                                        ChatColor.GREEN + "from the account of " + ChatColor.YELLOW + args[1] +
                                        ChatColor.GREEN + "!");

                        if (Bukkit.getPlayer(args[1]).isOnline()) {

                            Bukkit.getPlayer(args[1]).sendRawMessage(
                                    ChatColor.YELLOW + player.getName() + ChatColor.GREEN + " has removed " +
                                            ChatColor.YELLOW + args[2] + ChatColor.DARK_AQUA + " O" + ChatColor.YELLOW +
                                            "Bucks" + ChatColor.GREEN + " from your account!");

                        }
                        return true;

                    }

                } else if (args[0].equalsIgnoreCase("view")) {

                    if (!player.hasPermission("obam.mod")) {

                        player.sendRawMessage(ChatColor.RED + "You do not have permission to do this!");
                        return true;

                    } else if (args.length < 2) {

                        player.sendRawMessage(ChatColor.RED + "You need to specify a player! " + ChatColor.YELLOW +
                                "obucks view [player]");
                        return true;

                    } else if (!ObamAPI.isOBAMPlayer(args[1])) {

                        player.sendRawMessage(
                                ChatColor.YELLOW + args[1] + ChatColor.RED + " is not a registered player!");
                        return true;

                    } else {

                        UUID uuid = ObamAPI.getUUID(args[1]);
                        Double obucks = ObamAPI.getObucks(uuid);
                        player.sendRawMessage(ChatColor.YELLOW + args[1] + ChatColor.YELLOW + "'s " + ChatColor.GREEN +
                                "OBucks balance is " + ChatColor.YELLOW + obucks);
                        return true;

                    }

                }

            } else if (cmd.getName().equalsIgnoreCase("stubs")) {


                if (args.length < 1) {

                    Double stubs = ObamAPI.getStubs(player.getUniqueId());
                    player.sendRawMessage(
                            ChatColor.YELLOW + "Stubs " + ChatColor.WHITE + "| " + ChatColor.GREEN + stubs + " " +
                                    ChatColor.WHITE + "..::.. More Info: obam.co/stubs");
                    return true;

                } else if (args[0].equalsIgnoreCase("give")) {

                    if (!player.hasPermission("obam.smod")) {

                        player.sendRawMessage(ChatColor.RED + "You do not have permission to do this!");
                        return true;

                    } else if (args.length < 2) {

                        player.sendRawMessage(ChatColor.RED + "You must specify a player! " + ChatColor.YELLOW +
                                "stubs give [player] [amount] [reason]");
                        return true;

                    } else if (args.length < 3) {

                        player.sendRawMessage(
                                ChatColor.RED + "You must specify an amount! " + ChatColor.YELLOW + "stubs give " +
                                        args[1] + " [amount] [reason]");
                        return true;
                    } else if (args.length < 4) {

                        player.sendRawMessage(
                                ChatColor.RED + "You must specify a reason!" + ChatColor.YELLOW + " stubs give " +
                                        args[1] + " " + args[2] + " [reason]");
                        return true;

                    } else if (!ObamAPI.isOBAMPlayer(args[1])) {

                        player.sendRawMessage(
                                ChatColor.YELLOW + args[1] + ChatColor.RED + " is not a registered player!");
                        return true;

                    } else {

                        StringBuilder buffer = new StringBuilder();

                        for (int i = 3; i < args.length; i++) {
                            buffer.append(' ').append(args[i]);
                        }
                        UUID uuid = ObamAPI.getUUID(args[1]);
                        ObamAPI.addStubs(uuid, Double.parseDouble(args[2]), player.getName(), buffer.toString());
                        player.sendRawMessage(
                                ChatColor.GREEN + "You have added " + ChatColor.YELLOW + args[2] + ChatColor.YELLOW +
                                        "Stubs " +
                                        ChatColor.GREEN + "to the account of " + ChatColor.YELLOW + args[1] +
                                        ChatColor.GREEN + "!");

                        if (Bukkit.getPlayer(args[1]).isOnline()) {

                            Bukkit.getPlayer(args[1]).sendRawMessage(
                                    ChatColor.YELLOW + player.getName() + ChatColor.GREEN + " has added " +
                                            ChatColor.YELLOW + args[2] + " Stubs" + ChatColor.GREEN +
                                            " to your account!");

                        }
                        return true;

                    }


                } else if (args[0].equalsIgnoreCase("remove")) {

                    if (!player.hasPermission("obam.smod")) {

                        player.sendRawMessage(ChatColor.RED + "You do not have permission to do this!");
                        return true;

                    } else if (args.length < 2) {

                        player.sendRawMessage(ChatColor.RED + "You need to specify a player! " + ChatColor.YELLOW +
                                "stubs remove [player] [amount] [reason]");
                        return true;

                    } else if (args.length < 3) {

                        player.sendRawMessage(
                                ChatColor.RED + "You need to specify an amount! " + ChatColor.YELLOW + "stubs remove " +
                                        args[1] + " [amount] [reason]");
                        return true;

                    } else if (args.length < 4) {

                        player.sendRawMessage(
                                ChatColor.RED + "You need to specify a reason! " + ChatColor.YELLOW + "stubs remove " +
                                        args[1] + " " + args[2] + " [reason]");
                        return true;

                    } else if (!ObamAPI.isOBAMPlayer(args[1])) {

                        player.sendRawMessage(
                                ChatColor.YELLOW + args[1] + ChatColor.RED + " is not a registered player!");
                        return true;

                    } else {

                        StringBuilder buffer = new StringBuilder();
                        for (int i = 3; i < args.length; i++) {
                            buffer.append(' ').append(args[i]);
                        }
                        UUID uuid = ObamAPI.getUUID(args[1]);
                        if (Double.parseDouble(args[2]) > ObamAPI.getStubs(uuid)) {

                            player.sendRawMessage(
                                    ChatColor.YELLOW + args[1] + ChatColor.RED + " does not have that many Stubs!");
                            return true;

                        }
                        ObamAPI.removeStubs(uuid, Double.parseDouble(args[2]), player.getName(), buffer.toString());
                        player.sendRawMessage(
                                ChatColor.GREEN + "You have removed " + ChatColor.YELLOW + args[2] + ChatColor.YELLOW +
                                        "Stubs " +
                                        ChatColor.GREEN + "from the account of " + ChatColor.YELLOW + args[1] +
                                        ChatColor.GREEN + "!");
                        if (Bukkit.getPlayer(args[1]).isOnline()) {

                            Bukkit.getPlayer(args[1]).sendRawMessage(
                                    ChatColor.YELLOW + player.getName() + ChatColor.GREEN + " has removed " +
                                            ChatColor.YELLOW + args[2] + " Stubs" + ChatColor.GREEN +
                                            " from your account!");

                        }
                        return true;

                    }

                } else if (args[0].equalsIgnoreCase("view")) {

                    if (!player.hasPermission("obam.mod")) {

                        player.sendRawMessage(ChatColor.RED + "You do not have permission to do this!");
                        return true;

                    } else if (args.length < 2) {

                        player.sendRawMessage(ChatColor.RED + "You need to specify a player! " + ChatColor.YELLOW +
                                "stubs view [player]");
                        return true;

                    } else if (!ObamAPI.isOBAMPlayer(args[1])) {

                        player.sendRawMessage(
                                ChatColor.YELLOW + args[1] + ChatColor.RED + " is not a registered player!");
                        return true;

                    } else {

                        UUID uuid = ObamAPI.getUUID(args[1]);
                        Double stubs = ObamAPI.getStubs(uuid);
                        player.sendRawMessage(ChatColor.YELLOW + args[1] + ChatColor.YELLOW + "'s " + ChatColor.GREEN +
                                "Stubs balance is " + ChatColor.YELLOW + stubs);
                        return true;

                    }

                }

            } else if (cmd.getName().equalsIgnoreCase("pay")) {

                if (args.length < 1) {

                    player.sendRawMessage(ChatColor.RED + "You must specify someone to pay! " + ChatColor.YELLOW +
                            " /pay [player] [amount]");
                    return true;

                } else if (args.length < 2) {

                    player.sendRawMessage(
                            ChatColor.RED + "You must specify an amount! " + ChatColor.YELLOW + "/pay " + args[0] +
                                    " [amount]");
                    return true;


                } else if (Double.parseDouble(args[1]) == 0) {

                    player.sendRawMessage(ChatColor.RED + "You must enter a number!");
                    return true;

                } else if (Double.parseDouble(args[1]) > ObamAPI.getObucks(player.getUniqueId())) {

                    player.sendRawMessage(ChatColor.RED + "You do not have enough OBucks to do this!");
                    return true;

                } else if (!ObamAPI.isOBAMPlayer(args[0])) {

                    player.sendRawMessage(ChatColor.YELLOW + args[0] + ChatColor.RED + " is not a registered player!");
                    return true;


                } else {

                    UUID uuid = ObamAPI.getUUID(args[0]);
                    ObamAPI.removeObucks(player.getUniqueId(), Double.parseDouble(args[1]), player.getName(),
                            "Payment to " + args[0]);
                    ObamAPI.addObucks(uuid, Double.parseDouble(args[1]), player.getName(),
                            "Payment from " + player.getName());
                    /*
                     * TODO Alert cross server of any payments
                     */

                    player.sendRawMessage(
                            ChatColor.GREEN + "You have paid " + ChatColor.YELLOW + args[1] + ChatColor.DARK_AQUA +
                                    " O" + ChatColor.YELLOW + "Bucks " + ChatColor.GREEN + "to " + ChatColor.YELLOW +
                                    args[0]);
                    if (Bukkit.getPlayer(args[0]).isOnline()) {

                        Player receive = Bukkit.getPlayer(args[0]);
                        receive.sendRawMessage(ChatColor.GREEN + "You have received " + ChatColor.YELLOW + args[1] +
                                ChatColor.DARK_AQUA + " 0" + ChatColor.YELLOW + "Bucks " + ChatColor.GREEN + "from " +
                                ChatColor.YELLOW + player.getName());

                    }

                    return true;
                }

            } else if (cmd.getName().equalsIgnoreCase("balance")) {

                Double tickets = ObamAPI.getTickets(player.getUniqueId());
                Double obucks = ObamAPI.getObucks(player.getUniqueId());
                Double stubs = ObamAPI.getStubs(player.getUniqueId());

                player.sendRawMessage(
                        ChatColor.YELLOW + "Tickets " + ChatColor.WHITE + "| " + ChatColor.GREEN + tickets + " " +
                                ChatColor.WHITE + "..::.. More Info: obam.co/tickets");

                player.sendRawMessage(
                        ChatColor.DARK_AQUA + "O" + ChatColor.YELLOW + "Bucks " + ChatColor.WHITE + "| " +
                                ChatColor.GREEN + obucks + " " +
                                ChatColor.WHITE + "..::.. More Info: obam.co/obucks");

                player.sendRawMessage(
                        ChatColor.YELLOW + "Stubs " + ChatColor.WHITE + "| " + ChatColor.GREEN + stubs + " " +
                                ChatColor.WHITE + "..::.. More Info: obam.co/stubs");

                return true;

            } else if (cmd.getName().equalsIgnoreCase("tam")) {

                if (args.length < 1) {

                    double total = TAM.getTamTotal(player);
                    double boost = TAM.getBoost();
                    int cur = TAM.getTam();
                    double session = 0;
                    if (TAM.sessionTam.containsKey(player)) {
                        session = TAM.sessionTam.get(player);
                    }
                    player.sendRawMessage(ChatColor.GREEN + "Total Tickets Earned with -TAM-:");
                    player.sendRawMessage(ChatColor.YELLOW + String.valueOf(total));
                    player.sendRawMessage(ChatColor.GREEN + "Total this session:");
                    player.sendRawMessage(ChatColor.YELLOW + String.valueOf(session));
                    player.sendRawMessage(ChatColor.GREEN + "Total Tickets:");
                    player.sendRawMessage(ChatColor.YELLOW + String.valueOf(ObamAPI.getTickets(player.getUniqueId())));
                    player.sendRawMessage(ChatColor.GREEN + "Current TAM Modifier:");
                    player.sendRawMessage(ChatColor.YELLOW + String.valueOf(cur));
                    player.sendRawMessage(ChatColor.GREEN + "Current Supporter Boost");
                    player.sendRawMessage(ChatColor.YELLOW + String.valueOf(boost));
                    return true;

                } else if (args[0].equalsIgnoreCase("set")) {

                    if (!player.hasPermission("obam.smod")) {

                        player.sendRawMessage(ChatColor.RED + "You do not have permission to do this!");
                        return true;

                    } else if (args.length < 2) {

                        player.sendRawMessage(ChatColor.RED + "You must specify an amount!");
                        return true;

                    } else {
                        int up = 0;
                        try {
                            up = Integer.parseInt(args[1]);
                        } catch (NumberFormatException e) {

                            player.sendRawMessage(ChatColor.RED + "You must use a number!");
                            return true;

                        }

                        TAM.setTam(up);
                        player.sendRawMessage(
                                ChatColor.GREEN + "You have set the TAM Modifier to " + ChatColor.YELLOW + up);
                        return true;

                    }

                }

            }

        }

        return false;
    }


}
