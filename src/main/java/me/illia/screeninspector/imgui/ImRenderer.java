package me.illia.screeninspector.imgui;

import imgui.ImGui;
import me.illia.screeninspector.console.Console;
import me.illia.screeninspector.inspector.Inspector;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;

public class ImRenderer extends ImGuiTab {
	public Console console;
	public Inspector inspector;
	public ImRenderer(Screen screen, Window window, Keyboard keyboard) {
		super(window, screen, keyboard);

		this.console = new Console(window, screen, keyboard);
		this.inspector = new Inspector(window, screen, keyboard);
	}

	public static ImRenderer fromClient(MinecraftClient client) {
		return new ImRenderer(client.currentScreen, client.getWindow(), client.keyboard);
	}

	@Override
	public ImGuiTab[] children() {
		return new ImGuiTab[]{ console, inspector };
	}

	public void render() {
		ImGui.begin("Inspector");

		if (screen == null) {
			ImGui.textColored(200, 10, 27, 255, "No screen is currently open!");
			ImGui.end();
			return;
		}

		if (ImGui.beginTabBar("inspector")) {
			if (ImGui.beginTabItem("Inspector")) {
				inspector.render();

				ImGui.endTabItem();
			}

			if (ImGui.beginTabItem("Console")) {
				console.render();

				ImGui.endTabItem();
			}

			ImGui.endTabBar();
		}

		ImGui.end();
	}
}
