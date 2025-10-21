package me.illia.screeninspector.mixin;

import imgui.ImGui;
import me.illia.screeninspector.ScreenInspector;
import me.illia.screeninspector.Util;
import net.minecraft.client.Mouse;
import net.minecraft.client.input.MouseInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
	@Inject(method = "onMouseButton", at = @At("HEAD"), cancellable = true)
	private void onMouseButton(long window, MouseInput input, int action, CallbackInfo ci) {
		if (Util.mouseAndDevtools()) {
			ci.cancel();
		}
	}

	@Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
	private void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
		if (Util.mouseAndDevtools()) {
			ci.cancel();
		}
	}

	@Inject(method = "onCursorPos", at = @At("HEAD"), cancellable = true)
	private void onCursorPos(long window, double x, double y, CallbackInfo ci) {
		if (ScreenInspector.DEVTOOLS_ENABLED) {
			ci.cancel();
		}
	}

	@Inject(method = "unlockCursor", at = @At("HEAD"))
	private void unlockCursor(CallbackInfo ci) {
		ScreenInspector.MINECRAFT_WANT_UNLOCK = true;
	}

	@Inject(method = "lockCursor", at = @At("HEAD"))
	private void lockCursor(CallbackInfo ci) {
		ScreenInspector.MINECRAFT_WANT_UNLOCK = false;
	}
}
