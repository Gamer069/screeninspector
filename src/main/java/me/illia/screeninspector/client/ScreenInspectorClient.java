package me.illia.screeninspector.client;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import me.illia.screeninspector.ScreenInspector;
import me.illia.screeninspector.Util;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;

public class ScreenInspectorClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
	}

	public static void render() {
		MinecraftClient client = MinecraftClient.getInstance();
		if (ScreenInspector.DEVTOOLS_ENABLED) {
			ImGui.begin("Current Screen Info", ImGuiWindowFlags.AlwaysAutoResize);

			Util.curScreen(client);

			ImGui.end();
		}
	}
}
