package moe.hertz.chainlead;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.LeadItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Box;

public class ChainLead implements ModInitializer {
    @Override
    public void onInitialize() {
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (player.isSpectator() || !player.isSneaking() || world.isClient)
                return ActionResult.PASS;
            var stack = player.getStackInHand(hand);
            if (stack.getItem() instanceof LeadItem && entity instanceof MobEntity mob) {
                var pos = mob.getPos();
                var min = pos.add(-7, -7, -7);
                var max = pos.add(7, 7, 7);
                var entities = world.getNonSpectatingEntities(MobEntity.class, new Box(min, max));
                var handled = false;
                for (var selected : entities) {
                    if (selected.getHoldingEntity() != player)
                        continue;
                    selected.attachLeash(mob, true);
                    handled = true;
                }
                if (handled)
                    return ActionResult.SUCCESS;
            }
            return ActionResult.PASS;
        });
    }
}
