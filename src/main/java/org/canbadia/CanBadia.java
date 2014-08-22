package org.canbadia;

/*
  This file is part of CanBadia

  Foobar is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  Foobar is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
*/

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.canbadia.command.*;
import org.canbadia.utils.BaseIOUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class CanBadia extends JavaPlugin implements BaseIOUtils<Map<String, PlayerConfig>> {

    private static final String PLAYERS_CONF_PATH = "playersConfigs.bin";


    //ClassListeners
    private final CanBadiaCommandExecutor commandExecutor =
            new CanBadiaCommandExecutor(this);
    private final MarketCommandExecutor marketCommandExecutor =
            new MarketCommandExecutor(this);
    private final GodCommandExecutor godCommandExecutor =
            new GodCommandExecutor(this);
    private final TradeCommandExecutor tradeCommandExecutor =
            new TradeCommandExecutor(this);
    private final WallCommandExecutor wallCommandExecutor =
            new WallCommandExecutor(this);
    //ClassListeners


    // List of players with their configs

    private static Map<String, PlayerConfig> players;

    public Location playerLoc;

    static final Logger log = Logger.getLogger("Minecraft");//Define your logger


    @Override
    public void onDisable() {
        log.info("Disabled message here, shown in console on startup");
        try {
            log.info("Saving playersConfig");
            // we try to save the players configuration
            this.save(players, PLAYERS_CONF_PATH);
            log.info("saved players");
        } catch (Exception e) {
            log.warning("Failed to save " + PLAYERS_CONF_PATH + "!!! " + e.getMessage());
        }
//        try {
//            log.info("Saving market");
//            // we try to save the market
//            marketCommandExecutor.save(MarketCommandExecutor.getMarketProducts(), MARKET_PRODUCT_PATH);
//            log.info("saved market");
//        } catch (Exception e) {
//            log.warning("Failed to save " + MarketCommandExecutor.getPathMarketProduct() + "!!! " + e.getMessage());
//        }
    }

    @Override
    public void onEnable() {
        // We try to get the players configuration from last time
        log.info("Loading players file");
        try {
            players = this.load(PLAYERS_CONF_PATH);
        } catch (Exception e) {
        }
        if (players == null) {
            log.info("players == null, creating Map");
            players = new HashMap<String, PlayerConfig>();
        }
        log.info("Enabling CanBadia Plugin");

        PluginManager pm = this.getServer().getPluginManager();


        getCommand("trade").setExecutor(tradeCommandExecutor);
        getCommand("god").setExecutor(godCommandExecutor);
        getCommand("godsfullpower").setExecutor(godCommandExecutor);
        getCommand("market").setExecutor(marketCommandExecutor);
        getCommand("class").setExecutor(godCommandExecutor);
        getCommand("wall").setExecutor(wallCommandExecutor);


        /*Some other example listeners
            pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.BLOCK_PLACE, blockListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.ENTITY_DAMAGE, entityListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.ENTITY_EXPLODE, entityListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.CREATURE_SPAWN, spawnListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.ENTITY_EXPLODE, entityListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.BLOCK_BURN, blockListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.BLOCK_IGNITE, blockListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Event.Priority.Normal, this);
      */

    }

    /**
     * Get the Map(UserName, Map(configName, object))
     *
     * @return
     */
    public static Map<String, PlayerConfig> getPlayers() {
        return players;
    }


    @Override
    public Map<String, PlayerConfig> load(String path) {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(path));
            players = (Map<String, PlayerConfig>) objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return players;
    }

    @Override
    public void save(Map<String, PlayerConfig> object, String path) {
        try {
            ObjectOutputStream objectInputStream = new ObjectOutputStream(new FileOutputStream(path));
            objectInputStream.writeObject(object);
            objectInputStream.flush();
            objectInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
