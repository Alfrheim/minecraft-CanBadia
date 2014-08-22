package org.canbadia.generate;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.logging.Logger;

/**
 * Author: Marc Badia Cendros (randolph)
 * Date: 07/02/12
 * Time: 16:34
 * Mail: marc.badiac@gmail.com
 */
public class WallGenerator {

    static final Logger log = Logger.getLogger("WallGenerator");


    public void generateCubes(Location point, int x_wallLarge, int y, int z_wallLong) {
        World world = point.getWorld();

        Location location = point.add(-(x_wallLarge) / 2d, 0, -(z_wallLong / 2d));

        int x0, z0, y0;
        x0 = location.getBlockX();
        z0 = location.getBlockZ();

        Block blockToChange = null;

        this.generateLine(location, x_wallLarge, Material.SMOOTH_BRICK);

//        while(x0 <= (location.getBlockX()+x_wallLarge)) {
//            blockToChange = world.getBlockAt(x0, location.getBlockY(), z0);
//            blockToChange.setTypeId(Material.SMOOTH_BRICK.getId());
//            if(x0 == (location.getBlockX()+x_wallLarge)) {
//                while(z0 < (location.getBlockZ() + z_wallLong)) {
//                    blockToChange = world.getBlockAt(x0, location.getBlockY(), z0);
//                    blockToChange.setTypeId(Material.SMOOTH_BRICK.getId());
//                }
//            }
//            x0++;
//        }

//        for(int x = 0; x < x_wallLarge; x ++) {
//            for(int z = 0; z < z_wallLong; z++) {
//                Block blockToChange = world.getBlockAt(x0+x, location.getBlockY(), z0+z);
//                blockToChange.setTypeId(Material.SMOOTH_BRICK.getId());
//            }
//        }
    }

    private void generateLine(Location point, int length, Material material) {
        Block blockToChange = null;
        World world = point.getWorld();

        for (int x = point.getBlockX(); x < point.getBlockX() + length; x++) {
            blockToChange = world.getBlockAt(x, point.getBlockY(), point.getBlockY());
            blockToChange.setTypeId(material.getId());
        }
    }

    public void generateCube(Location point, int length) {  // public visible method generateCube() with 2 parameters point and location
        log.info("we are in the generation");
        World world = point.getWorld();
        log.info("x: " + point.getBlockX() + " y: " + point.getBlockY() + " z: " + point.getBlockZ());
        point.add(1, 0, 0);
        int x_start = point.getBlockX();     // Set the startpoints to the coordinates of the given location
        int y_start = point.getBlockY();     // I use getBlockX() instead of getX() because it gives you a int value and so you dont have to cast it with (int)point.getX()
        int z_start = point.getBlockZ();
        log.info("x: " + x_start + " y: " + y_start + " z: " + z_start);
        Vector vector = point.getDirection();
        int x_lenght = x_start + 3;    // now i set the lenghts for each dimension... should be clear.
        int y_lenght = y_start + 6;
        int z_lenght = z_start + length;
        int xWall = 0;
        int yWall = 0;
        int idMaterial;
        for (int x_operate = x_start; x_operate <= x_lenght; x_operate++) {
            log.info("xWall: " + xWall + " yWall: " + yWall);

            if ((xWall == 1 || xWall == 2)) {
                yWall = 0;
                log.info("changed to wood");
                idMaterial = Material.WOOD.getId();
            } else {
                yWall = 0;
                log.info("changed to stone");
                idMaterial = Material.SMOOTH_BRICK.getId();
            }

            //log.info("x: " + x_operate);
            for (int y_operate = y_start; y_operate <= y_start + 6; y_operate++) {// Loop 2 for the Y-Dimension
                //log.info("We are in y: xWall: " + xWall + " yWall: " + yWall);
                //log.info("x: " + x_operate + " y: " + y_operate);
                //if (xWall == 1 || xWall == 2 && yWall == 6) {
                //    yWall++;
                //    continue;
                //}
                for (int z_operate = z_start; z_operate <= z_lenght; z_operate++) {// Loop 3 for the Z-Dimension
                    //log.info("Current block: " + world.getBlockAt(x_operate,y_operate,z_operate));
                    //log.info("x: " + x_operate + " y: " + y_operate);
                    if (yWall == 6) {
                        if (xWall == 1 || xWall == 2) {
                            log.info("xWall: " + xWall + " yWall: " + yWall);
                            log.info("break");
                            break;
                        } else { // xWall == 0 || xWall == 3)
                            if (z_operate % 2 == 0) {
                                Block blockToChange = world.getBlockAt(x_operate, y_operate, z_operate); // get the block with the current coordinates
                                blockToChange.setTypeId(Material.FENCE.getId());
                                continue;
                            }
                        }
                    }
                    Block blockToChange = world.getBlockAt(x_operate, y_operate, z_operate); // get the block with the current coordinates
                    Block underBlock = world.getBlockAt(x_operate, y_operate - 1, z_operate);
                    Block upperBlock = world.getBlockAt(x_operate, y_operate + 1, z_operate);

                    if (blockToChange.getType().getId() == Material.AIR.getId()) {
                        if (underBlock.getType().getId() != Material.AIR.getId()) {
                            log.info("construccio per base.");
                            blockToChange.setTypeId(idMaterial);
                            y_start = underBlock.getY();
                            continue;
                        } else {
                            for (int rate = 0; rate > -10; rate--) {
                                if (underBlock.getType().getId() == Material.AIR.getId()) {
                                    underBlock = world.getBlockAt(x_operate, y_operate - rate, z_operate);
                                } else {
                                    underBlock.setTypeId(idMaterial);
                                    y_start = underBlock.getY();
                                    continue;
                                }
                            }
                        }

                    } else {
                        if (upperBlock.getType().getId() == Material.AIR.getId()) {
                            upperBlock.setTypeId(idMaterial);
                            y_start = upperBlock.getY();
                            continue;
                        } else {
                            for (int rate = 0; rate < 10; rate--) {
                                if (upperBlock.getType().getId() == Material.AIR.getId()) {
                                    upperBlock = world.getBlockAt(x_operate, y_operate + rate, z_operate);
                                } else {
                                    upperBlock.setTypeId(idMaterial);
                                    y_start = upperBlock.getY();
                                    continue;
                                }
                            }
                        }
                    }
//                        for (int a = y_operate; a > y_operate - 10; a--) {
//                            log.info("Hi ha un esglao");
//                            underBlock = world.getBlockAt(x_operate, y_operate - 1 - a, z_operate);
//                            if (!(underBlock.getType().getId() == Material.AIR.getId())) {
//                                blockToChange = world.getBlockAt(x_operate, y_operate - a, z_operate); // construeixo a sobre del terra
//                                blockToChange.setTypeId(idMaterial);
//                                continue;
//                            }
//                        }
//                    }
//                    log.info("Don't have the case, we change it!!!");
//                    blockToChange.setTypeId(idMaterial);

//                    if ((underBlock.getType().getId() != Material.AIR.getId()) && (upperBlock.getType().getId() == Material.AIR.getId())) {
//                        blockToChange.setTypeId(idMaterial);
//                    } else if ((underBlock.getType().getId() == Material.AIR.getId()) && (upperBlock.getType().getId() == Material.AIR.getId())) { // hi ha un esglao
//                        for (int a = y_operate; a > y_operate - 10; a--) {
//                            underBlock = world.getBlockAt(x_operate, y_operate - 1 - a, z_operate);
//                            if (!(underBlock.getType().getId() == Material.AIR.getId())) {
//                                blockToChange = world.getBlockAt(x_operate, y_operate - a, z_operate); // construeixo a sobre del terra
//                                blockToChange.setTypeId(idMaterial);
//                                continue;
//                            }
//                        }
//                    } else if (!(underBlock.getType().getId() == Material.AIR.getId()) && (upperBlock.getType().getId() == Material.AIR.getId())) {
//                        blockToChange.setTypeId(idMaterial);
//                        break;
//                    } else if (!(upperBlock.getType().getId() == Material.AIR.getId()) && !(underBlock.getType().getId() == Material.AIR.getId())) { // hi ha un esglao
//                        for (int a = y_operate; a > y_operate + 10; a++) {
//                            underBlock = world.getBlockAt(x_operate, y_operate - 1 - a, z_operate);
//                            if (!(upperBlock.getType().getId() == Material.AIR.getId())) {
//                                blockToChange = world.getBlockAt(x_operate, y_operate + a, z_operate); // construeixo a sobre del terra
//                                blockToChange.setTypeId(idMaterial);
//                            }
//                        }
//                    } else {
//                        //log.info("Don't have the case, we change it!!!");
//                        blockToChange.setTypeId(idMaterial);
//                    }

                }
                yWall++;
            }
            xWall++;
        }
    }


}
