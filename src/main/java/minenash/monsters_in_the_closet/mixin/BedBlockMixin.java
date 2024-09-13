package minenash.monsters_in_the_closet.mixin;

import minenash.monsters_in_the_closet.MonstersInTheCloset;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BedBlock.class)
public class BedBlockMixin {

    @Unique
    private static BlockPos blockPos = null;

    @Inject(method = "lambda$useWithoutItem$1",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;displayClientMessage(Lnet/minecraft/network/chat/Component;Z)V"))
    private static void thingy(Player player, Player.BedSleepingProblem reason, CallbackInfo ci) {
        if (reason != Player.BedSleepingProblem.NOT_SAFE)
            return;


        if (blockPos != null) {

            Vec3 vec3d = Vec3.atBottomCenterOf(blockPos);
            List<Monster> list = player.level().getEntitiesOfClass(Monster.class,
                new AABB(vec3d.x() - 8.0D, vec3d.y() - 5.0D, vec3d.z() - 8.0D, vec3d.x() + 8.0D,
                        vec3d.y() + 5.0D, vec3d.z() + 8.0D),
                (hostileEntity) -> hostileEntity.isPreventingPlayerRest(player)
            );

            if (!list.isEmpty()) {
                MonstersInTheCloset.duration = 60;
                MonstersInTheCloset.list = list;
            }
        }


    }

    @Inject(method = "useWithoutItem", at = @At("HEAD"))
    public void onUse(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        blockPos = pos;
    }
}
