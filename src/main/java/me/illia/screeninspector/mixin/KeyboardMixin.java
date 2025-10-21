package me.illia.screeninspector.mixin;

import imgui.ImGui;
import me.illia.screeninspector.ScreenInspector;
import me.illia.screeninspector.Util;
import net.minecraft.client.Keyboard;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {
	@Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
	private void onKey(long window, int action, KeyInput input, CallbackInfo ci) {
		if (input.key() == GLFW.GLFW_KEY_BACKSLASH && action == GLFW.GLFW_RELEASE) {
			ScreenInspector.DEVTOOLS_ENABLED = !ScreenInspector.DEVTOOLS_ENABLED;
			ci.cancel();
		}

		if (ImGui.getIO().getWantCaptureKeyboard()) {
			ci.cancel();
		}
	}

	@Inject(method = "onChar", at = @At("HEAD"), cancellable = true)
	private void onChar(long window, CharInput input, CallbackInfo ci) {
		if (Util.keyboardAndDevtools()) {
			ci.cancel();
		}
	}
}
