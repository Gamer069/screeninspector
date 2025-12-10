package me.illia.screeninspector.client;

import imgui.ImGui;
import me.illia.screeninspector.ScreenInspector;
import me.illia.screeninspector.imgui.ImRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class ScreenInspectorClient implements ClientModInitializer {
	public static ImRenderer RENDERER;

	@Override
	public void onInitializeClient() {
		MinecraftClient client = MinecraftClient.getInstance();

		RENDERER = ImRenderer.fromClient(client);
		RENDERER.update();

		ScreenEvents.BEFORE_INIT.register(((_client, screen, _scaledWidth, _scaledHeight) -> {
			ScreenEvents.remove(screen).register((screen1) -> {
				RENDERER.screen = null;
				RENDERER.update();
			});
		}));

		ScreenEvents.AFTER_INIT.register((_client, screen, _scaledWidth, _scaledHeight) -> {
			RENDERER.screen = screen;
			RENDERER.update();
		});
	}

	public static void render() {
		if (ScreenInspector.DEVTOOLS_ENABLED) {
			RENDERER.render();
		}
	}
}
