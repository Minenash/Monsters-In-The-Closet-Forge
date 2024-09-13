package minenash.monsters_in_the_closet.mixin;

import minenash.monsters_in_the_closet.MonstersInTheCloset;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.chat.ChatListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ChatListener.class)
public class ChatListenerMixin {

    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "handleSystemMessage", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/client/gui/Gui;setOverlayMessage(Lnet/minecraft/network/chat/Component;Z)V"))
    private void interceptDangerousSleepMessage(Component message, boolean isOverlay, CallbackInfo ci) {
        if (minecraft.player == null || minecraft.level == null ||
                !(message.getContents() instanceof TranslatableContents && ((TranslatableContents)message.getContents()).getKey().equals("block.minecraft.bed.not_safe")))
            return;

        Vec3 vec3d = Vec3.atBottomCenterOf(minecraft.player.blockPosition());
        List<Monster> list = minecraft.level.getEntitiesOfClass(
                Monster.class,
                new AABB(vec3d.x - 8.0D, vec3d.y - 5.0D, vec3d.z - 8.0D, vec3d.x + 8.0D, vec3d.y + 5.0D,
                        vec3d.z + 8.0D),
                hostileEntity -> hostileEntity.isPreventingPlayerRest(minecraft.player));

        if (!list.isEmpty()) {
            MonstersInTheCloset.duration = 60;
            MonstersInTheCloset.list = list;
        }
    }

}
