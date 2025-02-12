package hen676.dragonlite.mixins.gui.hud;

import hen676.dragonlite.DragonLite;
import hen676.dragonlite.config.Config;
import hen676.dragonlite.gui.screen.option.CompassPlacement;
import hen676.dragonlite.keybinds.FreecamKeybinding;
import hen676.dragonlite.render.CompassRenderer;
import hen676.dragonlite.util.CallbackUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    // Freecam
    @Inject(method = "renderStatusBars", at = @At("HEAD"), cancellable = true)
    private void preventStatusBarRenderOnFreecam(DrawContext context, CallbackInfo ci) {
        CallbackUtil.CancelIfFreecamOn(ci);
    }

    @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
    private void preventHotbarRenderOnFreecam(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        CallbackUtil.CancelIfFreecamOn(ci);
    }

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void preventCrosshairRenderOnFreecam(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        CallbackUtil.CancelIfFreecamOn(ci);
    }

    @Inject(method = "renderExperienceBar", at =@At("HEAD"), cancellable = true)
    private void preventExperienceBarRenderOnFreecam(DrawContext context, int x, CallbackInfo ci) {
        CallbackUtil.CancelIfFreecamOn(ci);
    }

    @Inject(method = "renderExperienceLevel", at =@At("HEAD"), cancellable = true)
    private void preventExperienceLevelRenderOnFreecam(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        CallbackUtil.CancelIfFreecamOn(ci);
    }

    // Compass
    @Inject(method = "render", at = @At("RETURN"))
    private void renderCompassHud(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (!Config.ENABLE_COMPASS || FreecamKeybinding.isFreecam()) return;

        if (DragonLite.MC.cameraEntity == null || DragonLite.MC.options.hudHidden) return;
        Entity camera = DragonLite.MC.cameraEntity;
        CompassRenderer.draw(DragonLite.MC, camera.getHorizontalFacing(), camera.getBlockPos(), context, CompassPlacement.byId(Config.COMPASS_PLACEMENT));
    }
}
