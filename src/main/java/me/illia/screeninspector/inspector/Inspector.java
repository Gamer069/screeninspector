package me.illia.screeninspector.inspector;

import imgui.ImGui;
import me.illia.screeninspector.imgui.ImGuiTab;
import me.illia.screeninspector.inspector.info.DrawablesInfo;
import me.illia.screeninspector.inspector.info.HandledScreenInfo;
import me.illia.screeninspector.inspector.info.MouseInfo;
import me.illia.screeninspector.inspector.info.ScreenInfo;
import net.minecraft.client.Keyboard;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;

public class Inspector extends ImGuiTab {
	public MouseInfo mouseInfo;
	public ScreenInfo screenInfo;
	public HandledScreenInfo handledScreenInfo;
	public DrawablesInfo drawablesInfo;

	public Inspector(Window window, Screen screen, Keyboard keyboard) {
		super(window, screen, keyboard);

		this.mouseInfo = new MouseInfo(window, screen, keyboard);
		this.screenInfo = new ScreenInfo(window, screen, keyboard);
		this.handledScreenInfo = new HandledScreenInfo(window, screen, keyboard);
		this.drawablesInfo = new DrawablesInfo(window, screen, keyboard);
	}

	@Override
	public ImGuiTab[] children() {
		return new ImGuiTab[]{ mouseInfo, screenInfo, handledScreenInfo, drawablesInfo };
	}

	@Override
	public void render() {
		mouseInfo.render();

		ImGui.separator();

		screenInfo.render();

		ImGui.separator();

		handledScreenInfo.render();

		ImGui.separator();

		drawablesInfo.render();
	}
}
