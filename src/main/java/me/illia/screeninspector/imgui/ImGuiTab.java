package me.illia.screeninspector.imgui;

import net.minecraft.client.Keyboard;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;

public abstract class ImGuiTab {
	public Window window;
	public Screen screen;
	public Keyboard keyboard;

	public ImGuiTab(Window window, Screen screen, Keyboard keyboard) {
		this.window = window;
		this.screen = screen;
		this.keyboard = keyboard;
	}

	public void update() {
		for (ImGuiTab child : children()) {
			child.window = window;
			child.keyboard = keyboard;
			child.screen = screen;
			child.update();
		}
	}

	public ImGuiTab[] children() {
		return new ImGuiTab[0];
	}

	public abstract void render();
}
