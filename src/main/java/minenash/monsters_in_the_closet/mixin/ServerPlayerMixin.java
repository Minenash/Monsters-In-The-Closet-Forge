package minenash.monsters_in_the_closet.mixin;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Entity {
    public ServerPlayerMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Inject(method = "startSleepInBed", at = @At("RETURN"))
    public void highlightMobs(BlockPos pos, CallbackInfoReturnable<Either<Player.BedSleepingProblem, Unit>> info) {
        Optional<Player.BedSleepingProblem> reason = info.getReturnValue().left();
        if (level() != null && reason.isPresent() && reason.get() == Player.BedSleepingProblem.NOT_SAFE) {

            Vec3 vec3d = Vec3.atBottomCenterOf(pos);
            List<Monster> list = level().getEntitiesOfClass(
                    Monster.class,
                    new AABB(vec3d.x - 8.0D, vec3d.y - 5.0D, vec3d.z - 8.0D, vec3d.x + 8.0D, vec3d.y + 5.0D,
                            vec3d.z + 8.0D),
                    (hostileEntity) -> hostileEntity.isPreventingPlayerRest((Player) (Object) this)
            );

            for (Monster entity : list)
                entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 60, 0, true, false));

        }
    }
}
