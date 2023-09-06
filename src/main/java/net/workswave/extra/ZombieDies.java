
package net.workswave.extra;


import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.workswave.item.ModItems;


@Mod.EventBusSubscriber
public class ZombieDies {
    public static Zombie zombie;
    final ItemStack itemStack = new ItemStack(ModItems.ROTTEN.get());
    double x = zombie.getX();
    double y = zombie.getY();
    double z = zombie.getZ();
    public ZombieDies(Zombie zombie){
        this.zombie = zombie;
    }

    public static void OnEntityDeath(LivingDeathEvent event) {
        if (event != null && event.getEntity() == zombie && event.getEntity() != null && zombie.getRandom().nextInt(400) == 0){
        }
    }
    @Override
    protected void dropEquipment() {
        if (this.level() instanceof ServerLevel server) {
            if (Math.random() <= 0.2F) {
                final ItemEntity item = new ItemEntity(EntityType.ITEM, server);
                item.setItem(this.getMainWeapon().getDefaultInstance());
                item.moveTo(this.position());
                server.addFreshEntity(item);
            }
            final ItemStack map = Aquamirae.getStructureMap(Aquamirae.SHELTER, server, this);
            if (!map.isEmpty()) {
                final ItemEntity item = new ItemEntity(EntityType.ITEM, server);
                item.setItem(map);
                item.moveTo(this.position());
                server.addFreshEntity(item);
            }
        }
    }




}
