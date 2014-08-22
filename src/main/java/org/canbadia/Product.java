/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.canbadia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.canbadia.exceptions.NotEnoughQuantityException;


/**
 * @author joba
 */
public class Product implements Serializable {

    Logger log = Logger.getLogger("Product");

    private final int minPrice;
    private final int maxPrice;
    private final int minQuantity;
    private final int maxQuantity;
    private int quantity;
    private Material material;


    public Product(Product prod) {
        this(prod.getMaterial(), prod.getMinPrice(), prod.getMaxPrice(),
                prod.getMinQuantity(), prod.getMaxQuantity(), prod.getQuantity());
    }

    public Product(Material material, int minPrice, int maxPrice, int minQuantity, int maxQuantity, int quantity) {
        this.material = material;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minQuantity = minQuantity;
        this.maxQuantity = maxQuantity;
        this.quantity = quantity;
    }


    public Material getMaterial() {
        return material;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Give the current price of the material.
     *
     * @return the current price of the material
     */
    public int getPrice() {
        if (quantity > 0) {

            BigDecimal materialPrice = new BigDecimal(maxPrice - minPrice);
            BigDecimal materialQuantity = new BigDecimal(maxQuantity - minQuantity);

            if (quantity > maxQuantity) {
                return minPrice;

            } else if (quantity < minQuantity) {
                return maxPrice;

            } else {
                /*
        log.info("materialPrice: " + materialPrice + " / materialQuantity: " + materialQuantity + " = " +
                materialPrice.divide(materialQuantity, RoundingMode.HALF_UP) + " x quantity:" +this.quantity +" minus minQuantity " + (this.quantity - this.minQuantity));
                */
                /*
                 We need to calculate the price:
                 First we calculate how much cost each unity of material we have.
                 Then we multiply the cost of each unity for all the material we have less minQuantity (since it have his own price = maxPrice)
                 Rest us to subtract from maxPrice
                */
                return (new BigDecimal(maxPrice).subtract(materialPrice.divide(materialQuantity, RoundingMode.HALF_UP).multiply(new BigDecimal(this.quantity - this.minQuantity))).intValue());
            }
        } else {
            return 0;
        }

    }

    /**
     * Sustract the given quantity.
     *
     * @param value
     * @return true if have the enough quantity.
     */
    public boolean subst(int value) {
        int newQuantity = quantity - value;
        if (newQuantity < 0) {
            return false;
        } else {
            quantity = newQuantity;
            return true;
        }
    }

    public boolean add(int value) {
        int newQuantity = quantity + value;
        if (newQuantity > maxQuantity * 1.2) {
            return false;
        } else {
            quantity = newQuantity;
            return true;
        }
    }

    public int getPriceQuantity(int quantity) {
        Product product = new Product(this);
        int result = 0;
        while (quantity > 0) {

            result += product.getPrice();

            product.subst(1);
            quantity--;
        }
        return result;
    }


    /*
    public static Map<Material,Product> getProducts() {
        
        return 
    }*/
    private int price(int quant, Product aux2) {
        if (aux2.subst(1) & quant > 0) {
            int result = 0;
            result = aux2.getPrice();

            return result + price(quant - 1, aux2);
        }
        return 0;
    }

    public int getPriceForQuantity(int quantity) {
        // TODO implement how much will cost quantity of this material
        Product aux = new Product(this);

        if (aux.subst(quantity)) {
            aux.add(quantity);
            return price(quantity, aux);
        }

        return -1;
    }
}
