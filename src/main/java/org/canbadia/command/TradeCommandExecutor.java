package org.canbadia.command;

import com.sun.corba.se.impl.orbutil.LogKeywords;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.canbadia.CanBadia;

import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: randolph
 * Date: 04/02/12
 * Time: 20:24
 */
public class TradeCommandExecutor implements CommandExecutor {
    private CanBadia plugin;
    Logger log = Logger.getLogger("MarketExecutor");

    public TradeCommandExecutor(CanBadia plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            StringBuilder help = new StringBuilder();
            help.append(ChatColor.DARK_GRAY);
            help.append("1.- Put the stack of the desired material to offer\\n");
            help.append("2.- Use the comand: \\n");
            help.append(ChatColor.BLACK);
            help.append("/trade <material to offer> <material you want> <quantity>");
            sender.sendMessage(help.toString());
            return true;

        } else if (args.length == 1) { // market wood <== ha seleccionat el material


        } else if (args.length > 1) {
            // TODO add other features with more args.length > 1

            if (args.length == 3) { // market wood diamond 10
                /* TODO
                 - get amount traded from the market prize
                 - get amount want to trade from all inventory instead of one stack
                  */
                // Player want to buy
                String offer = args[0];
                String demand = args[1];
                Integer quantity = new Integer(args[2]);

                Player player = (Player) sender;

                ItemStack itemToTrade = new ItemStack(Material.matchMaterial(offer), quantity);
                PlayerInventory inventory = player.getInventory();

                if (inventory.contains(itemToTrade)) {

                    for (ItemStack item : inventory.getContents()) {

                        if (item != null) {
                            if (item.getType() != null && item.getType().name() != null) {

                                if (item.getType().equals(itemToTrade.getType()) && item.getAmount() == itemToTrade.getAmount()) {

                                    inventory.removeItem(item);
                                    inventory.addItem(new ItemStack(Material.matchMaterial(demand), quantity));
                                    player.sendMessage(ChatColor.BLUE + "Trade accepted!");
                                    return true;
                                }
                            } else {

                            }
                        }
                    }
                    player.sendMessage(ChatColor.DARK_RED + "Couldnt find " + itemToTrade.getAmount() + " x " + itemToTrade.getType().name());
                    return true;
                }
            }
        }
        return false;
    }
}
