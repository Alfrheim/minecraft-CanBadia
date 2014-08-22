package org.canbadia.command;


import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.canbadia.CanBadia;
import org.canbadia.PlayerConfig;
import org.canbadia.Product;
import org.canbadia.commerce.MarketMaterials;
import org.canbadia.exceptions.NotEnoughCoinsException;
import org.canbadia.utils.AbstractBaseUtils;


import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: randolph
 * Date: 04/02/12
 * Time: 20:00
 */
public class MarketCommandExecutor extends AbstractBaseUtils<Map> implements CommandExecutor {
    private CanBadia plugin;

    public static String MARKET_PRODUCT = "marketProduct.bin";


    Logger log = Logger.getLogger("MarketExecutor");

    public static String getPathMarketProduct() {
        return MARKET_PRODUCT;
    }

    public static Map<Material, Product> getMarketProducts() {
        return marketProducts;
    }

    private static Map<Material, Product> marketProducts;


    public MarketCommandExecutor(CanBadia plugin) {
        this.plugin = plugin;
        try {
            marketProducts = this.load(MARKET_PRODUCT);
        } catch (Exception e) {
        }
        if (marketProducts == null) {
            //we generate the products at its default
            marketProducts = MarketMaterials.generateProducts();
        }
    }

    @Override
    protected Class<Map> getObject() {
        return Map.class;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        PlayerConfig configPlayer = null;
        if (!CanBadia.getPlayers().containsKey(sender.getName())) {
            CanBadia.getPlayers().put(sender.getName(), new PlayerConfig(sender.getName()));
        }
        Player player = (Player) sender;
        configPlayer = CanBadia.getPlayers().get(sender.getName());


        if (args[0].equalsIgnoreCase("show")) {
            if (args[1].equalsIgnoreCase("price")) {
                // ex: /market show price wood
                Product product = marketProducts.get(Material.matchMaterial(args[2]));
                StringBuilder message = new StringBuilder();
                message.append("We have ");
                message.append(product.getQuantity());
                message.append(" of ");
                message.append(" with a start price of ");
                message.append(product.getPrice());
                player.sendMessage(message.toString());

            } else {
                // Actually show all market, need to implement more things.
                MarketCommandExecutor.showMarket();
                return true;
            }
            // ex: /market wallet

        } else if (args[0].equalsIgnoreCase("wallet")) {
            boolean result = this.getWallet(player, configPlayer);
            return result;
        } else {
            // ex: /market buy 2 wood
            if (args[0].equalsIgnoreCase("buy")) {
                Product product = marketProducts.get(Material.matchMaterial(args[2]));
                int quantity = 0;
                try {
                    quantity = Integer.parseInt(args[1]);
                } catch (Exception e) {
                    return false;
                }
                log.info("quantity asked for: " + quantity);
                if (quantity < product.getQuantity()) {
                    log.info("Total price: " + product.getPriceQuantity(quantity));
                }
                int totalCost = 0;
                // we start tu buy
                while (product.getQuantity() > 0 && product.getPrice() < configPlayer.getCoins() && quantity > 0) {
                    try {
                        configPlayer.substractCoins(product.getPrice());
                    } catch (NotEnoughCoinsException e) {
                        break;
                    }
                    totalCost += product.getPrice();
                    log.info("add to the inventory, actual amount for mat " + product.getPrice() + " of " + product.getQuantity());
                    player.getInventory().addItem(new ItemStack(product.getMaterial().getId(), 1));
                    product.subst(1);
                    quantity--;
                }
                if (quantity < Integer.parseInt(args[1])) {
                    player.sendMessage(ChatColor.BLUE + "You bought " + quantity + " of " + args[2] + " for " + totalCost);
                } else {
                    player.sendMessage(ChatColor.BLUE + "You bought " + args[1] + " of " + args[2] + " for " + totalCost);
                }
                player.sendMessage(ChatColor.BLUE + product.getMaterial().name() + " left " + product.getQuantity());

                return true;

            } else if (args[0].equalsIgnoreCase("sell")) {
                log.info("Trying to sell something....");
                Material materialToSell = Material.matchMaterial(args[2]);
                int quantity = Integer.parseInt(args[1]);
                Product product = marketProducts.get(materialToSell);
                int money = 0;
                log.info("You want to sell: " + quantity + " x " + product.getMaterial().name());
                if (player.getInventory().contains(materialToSell, quantity)) {
                    for (ItemStack itemStack : player.getInventory().getContents()) {
                        if (quantity > 0 && product.getMaxQuantity() * 1.2 > product.getQuantity()) {
                            if (itemStack.getTypeId() == materialToSell.getId()) {
                                if (quantity > 0) {
                                    while (itemStack.getAmount() > 0) {
                                        money += product.getPrice();
                                        if (product.add(1)) {
                                            quantity--;
                                            itemStack.setAmount(itemStack.getAmount() - 1);
                                        } else {
                                            quantity--;
                                            itemStack.setAmount(itemStack.getAmount() - 1);
                                            break;
                                        }
                                    }
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                    configPlayer.addCoins(money);
                    player.sendMessage(ChatColor.DARK_RED + "You earned: " + money + " coins.");
                    return true;
                } else {
                    player.sendMessage(ChatColor.DARK_RED + "You don't have the quantity you said!");
                    return false;
                }

            }
        }

        return false;
    }

    /**
     * Show the to the player the actual prices of products.
     */
    private static void showMarket() {
        // TODO show the player the actual prices.

    }

    /**
     * Send a message to a player showing his wallet.
     *
     * @param player       who want to know his wallet
     * @param playerConfig config of the player
     * @return true if we send the message
     */
    private boolean getWallet(Player player, PlayerConfig playerConfig) {
        try {
            player.sendMessage(ChatColor.AQUA + "coins in your wallet: " + playerConfig.getCoins());
            return true;
        } catch (Exception e) {
            log.info("Error when trying to show the wallet of the player " + player.getName());
            return false;
        }
    }

    /*
    We will mark all products with a maxProduct,minProduct, maxPrize, minPrize.
    If the product reaches maxProduct, its prize gonna low to minPrize in the other hand
    if the product goes to minProduct or 0, its prize gonna be maxPrize (we need to decide
    if we can buy under 0)
     */


}
