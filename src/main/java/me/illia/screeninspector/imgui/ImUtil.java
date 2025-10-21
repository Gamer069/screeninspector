package me.illia.screeninspector.imgui;

import imgui.*;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import me.illia.screeninspector.ScreenInspector;
import me.illia.screeninspector.client.ScreenInspectorClient;
import net.minecraft.client.util.Window;
import org.lwjgl.glfw.GLFW;

public class ImUtil {
	private static final ImGuiImplGlfw glfw = new ImGuiImplGlfw();
	private static final ImGuiImplGl3 gl3 = new ImGuiImplGl3();

	public static final String GLSL_VERSION = "330";

	public static long win;

	// TODO: make this configurable
	public static void init(long win) {
		ImGui.createContext();

		final ImGuiIO io = ImGui.getIO();

		io.setIniFilename("screeninspector.ini");

		if (!Window.getGlfwPlatform().equals("wayland")) {
			io.addConfigFlags(ImGuiConfigFlags.DockingEnable);
			io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
		}

		io.addConfigFlags(ImGuiConfigFlags.NoMouseCursorChange);

		io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);
		io.setConfigViewportsNoTaskBarIcon(true);

		if (io.hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
			final ImGuiStyle style = ImGui.getStyle();
			style.setWindowRounding(0.0f);
			style.setColor(ImGuiCol.WindowBg, ImGui.getColorU32(ImGuiCol.WindowBg, 1));
		}

		ImUtil.win = win;

		glfw.init(win, true);
		gl3.init("#version " + GLSL_VERSION);
		gl3.createFontsTexture();
	}

	public static void render() {
		if (ScreenInspector.DEVTOOLS_ENABLED || ScreenInspector.MINECRAFT_WANT_UNLOCK) {
			GLFW.glfwSetInputMode(ImUtil.win, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
		} else {
			GLFW.glfwSetInputMode(ImUtil.win, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
		}

		glfw.newFrame();
		gl3.newFrame();
		ImGui.newFrame();

		ScreenInspectorClient.render();

		ImGui.render();

		gl3.renderDrawData(ImGui.getDrawData());

		if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
			final long backupWin = GLFW.glfwGetCurrentContext();

			ImGui.updatePlatformWindows();
			ImGui.renderPlatformWindowsDefault();

			GLFW.glfwMakeContextCurrent(backupWin);
		}

		// dont swap buffers and poll events bc mc does that
	}
}
