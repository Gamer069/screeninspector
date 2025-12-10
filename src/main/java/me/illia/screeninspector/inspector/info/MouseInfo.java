package me.illia.screeninspector.inspector.info;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiKey;
import me.illia.screeninspector.Util;
import me.illia.screeninspector.imgui.ImGuiTab;
import net.minecraft.client.Keyboard;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;

public class MouseInfo extends ImGuiTab {
	public MouseInfo(Window window, Screen screen, Keyboard keyboard) {
		super(window, screen, keyboard);
	}

	@Override
	public void render() {
		if (window == null) return;

		ImGui.text("Mouse position:");

		ImVec2 localPos = ImGui.getIO().getMousePos();
		ImVec2 pos = Util.windowToGui(window, localPos);

		ImGui.text("X: " + pos.x + ", Y:" + pos.y);

		if (ImGui.button("Copy (Ctrl + C)") || (ImGui.getIO().getKeyCtrl() && !ImGui.getIO().getKeyShift() && ImGui.isKeyPressed(ImGuiKey.C))) {
			keyboard.setClipboard(pos.x + "," + pos.y);
		}

		ImGui.separator();

		if (ImGui.button("Copy Color (Ctrl + Shift + C)") || (ImGui.getIO().getKeyCtrl() && ImGui.getIO().getKeyShift() && ImGui.isKeyPressed(ImGuiKey.C))) {
			ByteBuffer buffer = BufferUtils.createByteBuffer(4);
			GL11.glReadPixels((int)localPos.x, window.getWidth() - 1 - (int)localPos.y, 1, 1, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

			int r = buffer.get(0) & 0xFF;
			int g = buffer.get(1) & 0xFF;
			int b = buffer.get(2) & 0xFF;
			int a = buffer.get(3) & 0xFF;

			String hex = String.format("#%02X%02X%02X%02X", a, r, g, b);

			keyboard.setClipboard(hex);
		}
	}
}
