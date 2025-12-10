package me.illia.screeninspector.mixin;

import me.illia.screeninspector.client.ScreenInspectorClient;
import net.minecraft.client.util.Window;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Window.class)
public abstract class WindowMixin {
	@Shadow
	protected abstract void onWindowSizeChanged(long window, int width, int height);

	@Inject(method = "<init>", at = @At("TAIL"))
	private void initWin(CallbackInfo ci) {
		long handle = ((Window)(Object)this).getHandle();

		GLFW.glfwSetWindowSizeCallback(handle, (win,w,h) ->
		{
			// update window
			ScreenInspectorClient.RENDERER.window = (Window)(Object)this;
			ScreenInspectorClient.RENDERER.update();

			onWindowSizeChanged(win, w, h);
		});
	}
}
