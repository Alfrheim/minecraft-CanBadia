package org.canbadia.enums;

import com.google.common.collect.Maps;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Java15Compat;

import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: randolph
 * Date: 04/02/12
 * Time: 22:02
 */
public enum ClassType {
    PALADIN(1),
    FARMER(2),
    MINER(3),
    ENGINEER(4),
    ARCHER(5),
    GRANGER(6);

    int type;

    private final static Map<String, ClassType> BY_NAME = Maps.newHashMap();
    private static ClassType[] byId = new ClassType[6];
    private static Logger log = Logger.getLogger("ClassType");

    private ClassType(int type) {
        this.type = type;
    }

    public static ClassType matchClassType(final String name) {
        Validate.notNull(name, "Name cannot be null");

        ClassType result = null;

        if (result == null) {
            String filtered = name.toUpperCase();
            result = BY_NAME.get(filtered);
        }

        return result;
    }

    public static ClassType getClassType(final String name) {
        return BY_NAME.get(name);
    }

    public static Map<String, ClassType> getBY_NAME() {
        return BY_NAME;
    }

    // Carreguem BY_NAME al arrancar
    static {
        for (ClassType classType : values()) {
            if (byId.length > classType.type) {
                byId[classType.type] = classType;
            } else {
                byId = Java15Compat.Arrays_copyOfRange(byId, 0, classType.type + 2);
                byId[classType.type] = classType;
            }
            BY_NAME.put(classType.name(), classType);
        }
    }
}
