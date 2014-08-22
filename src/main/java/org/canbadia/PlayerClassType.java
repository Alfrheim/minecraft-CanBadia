package org.canbadia;

import org.bukkit.inventory.ItemStack;
import org.canbadia.enums.ClassType;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: randolph
 * Date: 04/02/12
 * Time: 21:57
 */
public class PlayerClassType implements Serializable {

    private final ClassType classType;
    private Calendar firstTime;
    private Calendar lastTimePower;
    private List<ItemStack> classSet;

    public PlayerClassType(String classType) {
        this.firstTime = Calendar.getInstance();
        this.classType = ClassType.matchClassType(classType);
    }

    public ClassType getClassType() {
        return classType;
    }

    public Calendar getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(Calendar firstTime) {
        this.firstTime = firstTime;
    }

    public Calendar getLastTimePower() {
        return lastTimePower;
    }

    public void setLastTimePower(Calendar lastTimePower) {
        this.lastTimePower = lastTimePower;
    }

    public List<ItemStack> getClassSet() {
        // TODO we will upgrade here and set the new set if needed
        return classSet;
    }
}
