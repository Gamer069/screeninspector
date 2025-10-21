package me.illia.screeninspector.mixin;

import me.illia.screeninspector.imgui.ImUtil;
import net.minecraft.client.gl.GlBackend;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiFunction;

@Mixin(GlBackend.class)
public class ImGuiInitMixin {
	@Inject(method = "<init>", at = @At("TAIL"), remap = false)
	private void initImGui(long contextId, int debugVerbosity, boolean sync, BiFunction shaderSourceGetter, boolean renderDebugLabels, CallbackInfo ci) {
		ImUtil.init(contextId);
	}
}
