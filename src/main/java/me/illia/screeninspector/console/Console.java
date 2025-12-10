package me.illia.screeninspector.console;

import imgui.ImGui;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiKey;
import imgui.type.ImString;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.illia.screeninspector.Util;
import me.illia.screeninspector.imgui.ImGuiTab;
import net.minecraft.client.Keyboard;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import net.minecraft.util.Colors;
import party.iroiro.luajava.LuaException;
import party.iroiro.luajava.luajit.LuaJit;

public class Console extends ImGuiTab {
	private final ObjectArrayList<RichText> items = new ObjectArrayList<>();
	private boolean scrollToBottom = false;
	private final ImString inp = new ImString(256);
	private final StringBuilder stdout = new StringBuilder();
	private LuaJit lua;
	private String pendingClear;

	public Console(Window window, Screen screen, Keyboard keyboard) {
		super(window, screen, keyboard);

		this.lua = new LuaJit();

		initLua();
	}

	@Override
	public void render() {
		float inputHeight = ImGui.getFrameHeightWithSpacing();
		float remaining = ImGui.getWindowContentRegionMaxY() - ImGui.getCursorPosY();
		ImGui.beginChild("ScrollingRegion", 0, remaining - inputHeight, false);

		for (RichText item : items) {
			ImGui.pushTextWrapPos(0);

			ImGui.textColored(Util.argbToImGuiColor(item.color()), item.str());

			ImGui.popTextWrapPos();
		}

		if (scrollToBottom) {
			ImGui.setScrollHereY(1.0f);
			scrollToBottom = false;
		}

		ImGui.endChild();

		if (pendingClear != null) {
			inp.set(pendingClear);
			pendingClear = null;
		}

		int flags = ImGuiInputTextFlags.AutoSelectAll | ImGuiInputTextFlags.EnterReturnsTrue;
		if (ImGui.inputText("Inp", inp, flags)) {
			run();
			inp.set("");
			scrollToBottom = true;
			ImGui.setKeyboardFocusHere(-1);
		}
	}

	@Override
	public void update() {
		reinitLua();
		super.update();
	}

	public void reinitLua() {
		System.out.println("reinit luaaa");

		if (lua != null)
			lua.close();

		lua = new LuaJit();

		initLua();
	}

	public void initLua() {
		lua.openLibraries();

		System.out.println("init lua");
		System.out.println("screen is null: " + (screen == null));
		System.out.println("window is null: " + (window == null));
		System.out.println("keyboard is null: " + (keyboard == null));

		LuaUtil.add(lua, screen, "screen");
		LuaUtil.add(lua, window, "window");
		LuaUtil.add(lua, keyboard, "keyboard");

		LuaUtil.add(lua, (state) -> {
			int n = lua.getTop();
			for (int i = 1; i <= n; i++) {
				if (i > 1) stdout.append("\t");

				lua.getGlobal("tostring");
				lua.pushValue(i);
				lua.pCall(1, 1);

				String s = lua.toString(-1);
				lua.pop(1);

				stdout.append(s);
			}
			stdout.append("\n");
			return 0;
		}, "print");
	}

	public void run() {
		try {
			lua.run(inp.get());

			items.add(new RichText(stdout.toString(), Colors.WHITE));

			stdout.setLength(0);
		} catch (LuaException e) {
			items.add(new RichText(e.toString(), Colors.RED));

			stdout.setLength(0);
		}
	}
}