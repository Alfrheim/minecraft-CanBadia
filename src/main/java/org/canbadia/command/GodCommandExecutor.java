package org.canbadia.command;

import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.canbadia.CanBadia;
import org.canbadia.PlayerClassType;
import org.canbadia.PlayerConfig;
import org.canbadia.enums.ClassType;

import java.util.*;
import java.util.logging.Logger;

/**
 * User: randolph
 * Date: 04/02/12
 * Time: 20:00
 */
public class GodCommandExecutor implements CommandExecutor {
    private CanBadia plugin;
    Logger log = Logger.getLogger("GodsPower");
    private static final int prayingTime = 100000;

    public GodCommandExecutor(CanBadia plugin) {
        this.plugin = plugin;

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        log.info("onCommand Reached in godPower");


        PlayerConfig configPlayer = null;
        log.info("we have player in list? " + CanBadia.getPlayers().containsKey(sender.getName()));
        if (!CanBadia.getPlayers().containsKey(sender.getName())) {
            CanBadia.getPlayers().put(sender.getName(), new PlayerConfig(sender.getName()));
        }

        log.info("users in userMap:");
        for (String userMap : CanBadia.getPlayers().keySet()) {
            log.info(userMap);
        }
        configPlayer = CanBadia.getPlayers().get(sender.getName());

        if ((sender instanceof Player)) {
            log.info(command.getName());
            if (command.getName().equalsIgnoreCase("god")) {

                if (args.length >= 1) {
                    if (args[0].equalsIgnoreCase("class")) {

                        Player player = (Player) sender;
                        List<ItemStack> playerSet = new ArrayList<ItemStack>();
                        ItemStack weapon = null;
                        ItemStack helmet = null;
                        ItemStack chest = null;
                        ItemStack legs = null;
                        ItemStack boot = null;
                        ItemStack item1 = new ItemStack(Material.APPLE, 10); // as default, paladins love apples so i do.
                        ItemStack item2 = null;

                        if (!this.isPrayTimeEnough(configPlayer)) {
                            player.sendMessage(ChatColor.RED + "God is too busy yet, you will need to wait a little bit longer.");
                            return true;
                        }
                        // we set all the items depending of who is
                        if (ClassType.matchClassType(args[1]) == ClassType.PALADIN) {
                            log.info((sender).getName() + " is a paladin.");
                            weapon = new ItemStack(Material.DIAMOND_SWORD, 1);
                            helmet = new ItemStack(Material.DIAMOND_HELMET, 1);
                            chest = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
                            legs = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
                            boot = new ItemStack(Material.DIAMOND_BOOTS, 1);

                        } else if (ClassType.matchClassType(args[1]) == ClassType.FARMER) {
                            log.info((sender).getName() + " is a farmer.");
                            weapon = new ItemStack(Material.DIAMOND_HOE, 1);
                            helmet = new ItemStack(Material.LEATHER_HELMET, 1);
                            chest = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
                            legs = new ItemStack(Material.LEATHER_LEGGINGS, 1);
                            boot = new ItemStack(Material.LEATHER_BOOTS, 1);
                            item1 = new ItemStack(Material.IRON_AXE, 1);

                        } else if (ClassType.matchClassType(args[1]) == ClassType.MINER) {
                            log.info((sender).getName() + " is a Miner.");
                            weapon = new ItemStack(Material.IRON_PICKAXE, 1);
                            helmet = new ItemStack(Material.IRON_HELMET, 1);
                            chest = new ItemStack(Material.IRON_CHESTPLATE, 1);
                            legs = new ItemStack(Material.IRON_LEGGINGS, 1);
                            boot = new ItemStack(Material.IRON_BOOTS, 1);
                            item1 = new ItemStack(Material.IRON_SPADE, 1);

                        } else if (ClassType.matchClassType(args[1]) == ClassType.ENGINEER) {
                            log.info((sender).getName() + " is an Engineer.");
                            weapon = new ItemStack(Material.IRON_PICKAXE, 1);
                            helmet = new ItemStack(Material.IRON_HELMET, 1);
                            chest = new ItemStack(Material.IRON_CHESTPLATE, 1);
                            legs = new ItemStack(Material.IRON_LEGGINGS, 1);
                            boot = new ItemStack(Material.IRON_BOOTS, 1);
                            item1 = new ItemStack(Material.IRON_SPADE, 1);

                        } else if (ClassType.matchClassType(args[1]) == ClassType.ARCHER) {
                            log.info((sender).getName() + " is a archer.");
                            weapon = new ItemStack(Material.BOW, 1);
                            helmet = new ItemStack(Material.DIAMOND_HELMET, 1);
                            chest = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
                            legs = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
                            boot = new ItemStack(Material.DIAMOND_BOOTS, 1);
                            item1 = new ItemStack(Material.ARROW, 64);

                        } else {
                            log.info("Couldnt find class type: " + ClassType.matchClassType(args[1]) + " args[1]=" + args[1]);
                            log.info("BY_NAME is empty: " + ClassType.getBY_NAME().isEmpty());
                            for (String name : ClassType.getBY_NAME().keySet()) {
                                log.info(name + ClassType.getBY_NAME().get(name));
                            }
                            return false;
                        }
                        playerSet.add(weapon);
                        playerSet.add(helmet);
                        playerSet.add(chest);
                        playerSet.add(legs);
                        playerSet.add(boot);
                        playerSet.add(item1);
                        // we put all the items in the player's inventory
                        if (configPlayer.getClassType() == null) {
                            configPlayer.setClassType(new PlayerClassType(args[0]));
                        } else if (configPlayer.getClassType().getClassType().name().equalsIgnoreCase(args[0])) {
                            this.fillInventory(playerSet, player);
                        } else {
                            player.sendMessage(ChatColor.DARK_RED + "You are a " + configPlayer.getClassType().getClassType().name());
                            return true;
                        }
                        configPlayer.getClassType().setLastTimePower(Calendar.getInstance());
                        return true;
                    }


                } else {
                    float explosionPower = 10F;
                    Player target = (Player) sender;
                    target.getWorld().createExplosion(target.getLocation(), explosionPower);
                    target.setHealth(0);
                    return true;

                }
            } else if (command.getName().equalsIgnoreCase("class")) {
                Player player = (Player) sender;
                if (configPlayer.getClassType() != null) {
                    player.sendMessage(ChatColor.BLUE + "You are a: " + configPlayer.getClassType().getClassType().name());
                    return true;

                }
                player.sendMessage(ChatColor.BLUE + "You are nothing yet.");
                return true;

            } else if (command.getName().equalsIgnoreCase("godsfullpower") && args.length == 1) {
                Player player = (Player) sender;
                for (Player tar : player.getWorld().getPlayers()) {
                    if (tar.getName().equals(args[0])) {
                        tar.setFireTicks(10000);
                        log.info(tar.getDisplayName() + " is receiving god's love.");
                        return true;
                    }
                }
                log.info("God couldn't find this player");
                return false;
            }
        }
        log.info("GodPower failed!!!");
        log.info("sender instanceof Player: " + (sender instanceof Player));
        log.info("sender: " + sender.getClass().getName());
        log.info("args: " + args.length);
        return false;
    }

    /**
     * Fill the inventory with the objects of the given list if the selected item is not
     * in his inventory or is wearing the equipment.
     *
     * @param list   of Items you want to fill in the inventory
     * @param player who will receive those items
     */
    private void fillInventory(List<? extends ItemStack> list, Player player) {
        PlayerInventory inventory = player.getInventory();
        for (ItemStack item : list) {
            if (!inventory.contains(item)) {
                // TODO check it dont have items equiped inventory.getArmorContents()
                inventory.addItem(item);
                player.sendMessage(ChatColor.GOLD + "God give you a: " + item.getType().toString());
            }
        }
        player.getWorld().setThundering(true); // una tempesta
    }

    /**
     * Check if the player have enough faith in God. Usually a player need some minutes after pray.
     *
     * @param playerConfig
     * @return
     */
    private boolean isPrayTimeEnough(PlayerConfig playerConfig) {
        log.info("Can Use power?");
        if (playerConfig.getClassType() != null) {
            if (playerConfig.getClassType().getLastTimePower() != null) {
                Calendar ini = playerConfig.getClassType().getLastTimePower();
                Calendar now = Calendar.getInstance();
                return now.getTimeInMillis() - ini.getTimeInMillis() > prayingTime ? true : false;
            } else {
                return true; // he has never used the power
            }
        } else {
            return true; // he didnt even decided what he want to be!!!
        }
    }
}
