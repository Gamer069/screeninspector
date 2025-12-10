package me.illia.screeninspector;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.ImVec4;
import imgui.flag.ImGuiKey;
import me.illia.screeninspector.mixin.HandledScreenAccessor;
import me.illia.screeninspector.mixin.ScreenAccessor;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.LayoutWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.input.MouseInput;
import net.minecraft.client.util.Window;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import party.iroiro.luajava.Lua;
import party.iroiro.luajava.luajit.LuaJitConsts;

import java.nio.ByteBuffer;
import java.util.List;

public class Util {
	public static Identifier id(String id) {
		return Identifier.of(ScreenInspector.MODID, id);
	}

	public static int not(int i) {
		return i == 0 ? 1 : 0;
	}

	public static ImVec2 guiToWindow(Window win, ImVec2 guiPos) {
		int guiWidth = win.getScaledWidth();
		int guiHeight = win.getScaledHeight();
		int windowWidth = win.getWidth();
		int windowHeight = win.getHeight();

		return new ImVec2(
			guiPos.x * ((float) windowWidth / guiWidth),
			guiPos.y * ((float) windowHeight / guiHeight)
		);
	}

	public static ImVec2 windowToGui(Window win, ImVec2 windowPos) {
		int guiWidth = win.getScaledWidth();
		int guiHeight = win.getScaledHeight();
		int windowWidth = win.getWidth();
		int windowHeight = win.getHeight();

		return new ImVec2(
			windowPos.x * ((float) guiWidth / windowWidth),
			windowPos.y * ((float) guiHeight / windowHeight)
		);
	}

	public static int argbToImGuiColor(int argb) {
		int a = (argb >> 24) & 0xFF;
		int r = (argb >> 16) & 0xFF;
		int g = (argb >> 8) & 0xFF;
		int b = argb & 0xFF;
		return ImGui.colorConvertFloat4ToU32(r / 255f, g / 255f, b / 255f, a / 255f);
	}

	public static String bool(Object obj, Class<?> clazz) {
		return clazz.isInstance(obj) ? "yes" : "no";
	}

	public static boolean mouseAndDevtools() {
		return ImGui.getIO().getWantCaptureMouse() && ScreenInspector.DEVTOOLS_ENABLED;
	}

	public static boolean keyboardAndDevtools() {
		return ImGui.getIO().getWantCaptureKeyboard() && ScreenInspector.DEVTOOLS_ENABLED;
	}

	public static Identifier mc(String id) {
		return Identifier.ofVanilla(id);
	}

	public static void highlight(ImVec2 pos, ImVec2 pos1, float r, float g, float b, float a, float round, float thick) {
		ImGui.getForegroundDrawList().addRect(pos, pos1, ImGui.getColorU32(r, g, b, a), round, 0, thick);
	}

	public static void basicHighlight(ImVec2 pos, ImVec2 pos1) {
		highlight(pos, pos1, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 3.0f);
	}
}
