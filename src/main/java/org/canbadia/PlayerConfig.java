package org.canbadia;

import org.canbadia.exceptions.NotEnoughCoinsException;

import java.io.Serializable;

/**
 * User: randolph
 * Date: 04/02/12
 * Time: 22:39
 */
public class PlayerConfig implements Serializable {

    private PlayerClassType classType;
    private final String name;
    private int coins;

    public PlayerConfig(String name) {
        this.name = name;
    }

    public PlayerClassType getClassType() {
        return classType;
    }

    public void setClassType(PlayerClassType classType) {
        this.classType = classType;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    /**
     * Add the given coins to the player.
     *
     * @param coins
     */
    public void addCoins(int coins) {
        this.coins += coins;
    }

    /**
     * Try to substract coins of the player. If param is bigger than the coins player have, it will throw an exception.
     *
     * @param coins
     * @throws NotEnoughCoinsException
     */
    public void substractCoins(int coins) throws NotEnoughCoinsException {
        if (coins > this.coins) {
            throw new NotEnoughCoinsException();
        } else {
            this.coins -= coins;
        }
    }

    public String getName() {
        return name;
    }
}
