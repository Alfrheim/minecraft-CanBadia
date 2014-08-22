package org.canbadia.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.canbadia.generate.WallGenerator;

import java.util.logging.Logger;

/**
 * Author: Marc Badia Cendros (randolph)
 * Date: 07/02/12
 * Time: 16:42
 * Mail: marc.badiac@gmail.com
 */
public class WallCommandExecutor implements CommandExecutor {

    private Plugin plugin;
    static final Logger log = Logger.getLogger("WallCommandExecutor");

    public WallCommandExecutor(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        log.info("We are in the logger");
        if (args.length == 0) {
            WallGenerator wallGenerator = new WallGenerator();
            Location location = ((Player) sender).getLocation();
            wallGenerator.generateCube(location.add(10, 0, -100), 50);
            location = ((Player) sender).getLocation();
            //wallGenerator.generateCube(location.add(-50, 0, -100), 200);
            return true;
        } else {
            WallGenerator wallGenerator = new WallGenerator();
            Location location = ((Player) sender).getLocation();
            wallGenerator.generateCubes(location, Integer.parseInt(args[0]), 0, Integer.parseInt(args[1]));
            return true;
        }
    }
}
