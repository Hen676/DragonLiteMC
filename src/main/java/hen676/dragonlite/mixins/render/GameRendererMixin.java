package hen676.dragonlite.mixins.render;

import hen676.dragonlite.config.Config;
import hen676.dragonlite.keybinds.FreecamKeybinding;
import hen676.dragonlite.keybinds.ZoomKeybinding;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    // Change fov to zoom in
    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    public void getZoomLevel(CallbackInfoReturnable<Double> callbackInfo) {
        if (ZoomKeybinding.isZooming())
            callbackInfo.setReturnValue(callbackInfo.getReturnValue() * Config.ZOOM_AMOUNT);
        ZoomKeybinding.manageSmoothCamera();
    }

    // Disable block outline in freecam
    @Inject(method = "shouldRenderBlockOutline", at = @At("HEAD"), cancellable = true)
    private void removeRenderBlockOutlineOnFreecam(CallbackInfoReturnable<Boolean> cir) {
        if (FreecamKeybinding.isFreecam())
            cir.setReturnValue(false);
    }
}
