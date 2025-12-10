package me.illia.screeninspector.inspector.info;

import imgui.ImGui;
import me.illia.screeninspector.MappingsUtil;
import me.illia.screeninspector.Util;
import me.illia.screeninspector.imgui.ImGuiTab;
import net.minecraft.client.Keyboard;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.Window;

public class ScreenInfo extends ImGuiTab {
	public ScreenInfo(Window window, Screen screen, Keyboard keyboard) {
		super(window, screen, keyboard);
	}

	@Override
	public void render() {
		ImGui.text("Title:");
		ImGui.text(screen.getTitle().getString().isEmpty() ? "<empty string>" : screen.getTitle().getString());

		ImGui.separator();

		ImGui.text("Class:");
		ImGui.text(MappingsUtil.intermediaryToYarn(screen.getClass()));

		ImGui.text("Is handled?:");
		ImGui.text(Util.bool(screen, HandledScreen.class));
	}
}
