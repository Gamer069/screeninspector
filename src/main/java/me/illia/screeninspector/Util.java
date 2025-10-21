package me.illia.screeninspector;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiKey;
import me.illia.screeninspector.mixin.HandledScreenAccessor;
import me.illia.screeninspector.mixin.ScreenAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.LayoutWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.Identifier;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;
import java.util.List;

public class Util {
	public static Identifier id(String id) {
		return Identifier.of(ScreenInspector.MODID, id);
	}

	public static int not(int i) {
		return i == 0 ? 1 : 0;
	}

	public static void mouseInfo(MinecraftClient client) {
		int guiWidth = client.getWindow().getScaledWidth();
		ImGui.text("Mouse position:");

		int guiHeight = client.getWindow().getScaledHeight();
		int windowWidth = client.getWindow().getWidth();
		int windowHeight = client.getWindow().getHeight();
		ImVec2 localPos = ImGui.getIO().getMousePos();
		ImVec2 pos = new ImVec2(localPos.x * ((float) guiWidth / windowWidth), localPos.y * ((float) guiHeight / windowHeight));

		ImGui.text("X: " + pos.x + ", Y:" + pos.y);

		if (ImGui.button("Copy (Ctrl + C)") || (ImGui.getIO().getKeyCtrl() && !ImGui.getIO().getKeyShift() && ImGui.isKeyPressed(ImGuiKey.C))) {
			client.keyboard.setClipboard(pos.x + "," + pos.y);
		}

		ImGui.separator();

		if (ImGui.button("Copy Color (Ctrl + Shift + C)") || (ImGui.getIO().getKeyCtrl() && ImGui.getIO().getKeyShift() && ImGui.isKeyPressed(ImGuiKey.C))) {
			ByteBuffer buffer = BufferUtils.createByteBuffer(4);
			GL11.glReadPixels((int)localPos.x, windowHeight - 1 - (int)localPos.y, 1, 1, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

			int r = buffer.get(0) & 0xFF;
			int g = buffer.get(1) & 0xFF;
			int b = buffer.get(2) & 0xFF;
			int a = buffer.get(3) & 0xFF;

			String hex = String.format("#%02X%02X%02X%02X", a, r, g, b);

			client.keyboard.setClipboard(hex);
		}
	}

	public static void curScreen(MinecraftClient client) {
		Screen screen = client.currentScreen;

		if (screen == null) {
			ImGui.textColored(200, 10, 27, 255, "No screen is currently open!");
			return;
		}

		mouseInfo(client);

		ImGui.separator();

		ImGui.text("Title:");
		ImGui.text(screen.getTitle().getString().isEmpty() ? "<empty string>" : screen.getTitle().getString());

		ImGui.separator();

		ImGui.text("Class:");
		ImGui.text(MappingsUtil.intermediaryToYarn(screen.getClass()));

		ImGui.separator();

		if (screen instanceof HandledScreen<?> handledScreen) {
			ImGui.text("Title:");
			int titleX = ((HandledScreenAccessor)handledScreen).robotmod$getTitleX();
			int titleY = ((HandledScreenAccessor)handledScreen).robotmod$getTitleY();
			int[] tempTitleX = { titleX };
			int[] tempTitleY = { titleY };

			if (ImGui.dragInt("##titleX", tempTitleX)) {
				((HandledScreenAccessor)handledScreen).robotmod$setTitleX(tempTitleX[0]);
			}

			if (ImGui.dragInt("##titleY", tempTitleY)) {
				((HandledScreenAccessor)handledScreen).robotmod$setTitleY(tempTitleY[0]);
			}
		}

		ImGui.text("Drawables:");
		List<Drawable> drawables = ((ScreenAccessor)screen).robotmod$getDrawables();

		int drawableI = 0;
		for (Drawable drawable : drawables) {
			if (ImGui.treeNode(MappingsUtil.intermediaryToYarn(drawable.getClass()) + "##" + drawableI)) {
				ImGui.text("Widget? " + (drawable instanceof Widget ? "yes" : "no"));
				ImGui.text("Layout widget? " + (drawable instanceof LayoutWidget ? "yes" : "no"));

				if (drawable instanceof Widget widget) {
					int[] tempX = { widget.getX() };
					int[] tempY = { widget.getY() };

					ImGui.text("Widget info:");

					if (ImGui.dragInt("##x" + drawableI, tempX)) {
						widget.setX(tempX[0]);
					}

					if (ImGui.dragInt("##y" + drawableI, tempY)) {
						widget.setY(tempY[0]);
					}

					ImGui.text("Size: " + widget.getWidth() + "," + widget.getHeight());
				}

				ImGui.treePop();
			}

			drawableI++;
		}
	}

	public static boolean mouseAndDevtools() {
		return ImGui.getIO().getWantCaptureMouse() && ScreenInspector.DEVTOOLS_ENABLED;
	}

	public static boolean keyboardAndDevtools() {
		return ImGui.getIO().getWantCaptureKeyboard() && ScreenInspector.DEVTOOLS_ENABLED;
	}
}
