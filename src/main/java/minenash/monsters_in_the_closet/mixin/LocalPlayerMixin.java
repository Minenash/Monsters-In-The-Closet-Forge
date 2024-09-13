package minenash.monsters_in_the_closet.mixin;

import minenash.monsters_in_the_closet.MonstersInTheCloset;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo info) {

        if (MonstersInTheCloset.duration == 0)
            MonstersInTheCloset.list = new ArrayList<>();

        if (MonstersInTheCloset.duration >= 0)
            MonstersInTheCloset.duration--;

    }

}
