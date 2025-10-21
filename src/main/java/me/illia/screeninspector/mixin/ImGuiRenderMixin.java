package me.illia.screeninspector.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.illia.screeninspector.imgui.ImUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderSystem.class)
public class ImGuiRenderMixin {
	@Inject(at = @At("HEAD"), method = "flipFrame")
	private static void render(CallbackInfo ci) {
		ImUtil.render();
	}
}
