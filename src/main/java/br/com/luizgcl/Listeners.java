package br.com.luizgcl;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class Listeners implements Listener {

    @EventHandler
    void event(EntityExplodeEvent event) {
        event.blockList().clear();
        event.setCancelled(true);

        if (event.getEntityType().equals(EntityType.PRIMED_TNT) || event.getEntityType().equals(EntityType.FIREBALL)) {
            Entity explodeEntity = event.getEntity();

            explodeEntity.getNearbyEntities(5, 5, 5).forEach(entity -> {
                if (entity instanceof Player) {
                    Player player = (Player) entity;
                    Location location = player.getLocation();
                    Vector vector = entity.getLocation().getDirection();

                    double rotX = location.getYaw();

                    double xMultiplier = 2.75;
                    double zMultiplier = 2.75;

                    vector.setX((-1 * Math.sin(Math.toRadians(rotX)))*xMultiplier);
                    vector.setY(1.3);
                    vector.setZ(Math.cos(Math.toRadians(rotX))*zMultiplier);

                    entity.setVelocity(vector);
                }
            });
        }
    }

    @EventHandler
    void event(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (block.getType().equals(Material.TNT)) {
            event.setCancelled(true);

            TNTPrimed tnt = Bukkit.getWorld(player.getWorld().getName()).spawn(block.getLocation(), TNTPrimed.class);
            tnt.setFuseTicks(20);
        }
    }


    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        if (player.getItemInHand().getType().equals(Material.FIREBALL)) {
            event.setCancelled(true);
            if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                Fireball fireball = player.launchProjectile(Fireball.class);
                fireball.setIsIncendiary(false);
                fireball.setFireTicks(0);
            }
        }
    }

}
