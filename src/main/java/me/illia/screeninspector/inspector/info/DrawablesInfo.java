package me.illia.screeninspector.inspector.info;

import imgui.ImGui;
import imgui.ImVec2;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import me.illia.screeninspector.MappingsUtil;
import me.illia.screeninspector.Util;
import me.illia.screeninspector.imgui.ImGuiTab;
import me.illia.screeninspector.mixin.ScreenAccessor;
import net.minecraft.client.Keyboard;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.LayoutWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.input.MouseInput;
import net.minecraft.client.util.Window;

import java.util.List;

public class DrawablesInfo extends ImGuiTab {
	private final Object2ObjectArrayMap<Object, int[]> widgetXCache = new Object2ObjectArrayMap<>();
	private final Object2ObjectArrayMap<Object, int[]> widgetYCache = new Object2ObjectArrayMap<>();

	public DrawablesInfo(Window window, Screen screen, Keyboard keyboard) {
		super(window, screen, keyboard);
	}

	@Override
	public void render() {
		ImGui.text("Drawables:");
		List<Drawable> drawables = ((ScreenAccessor)screen).screeninspector$getDrawables();

		int drawableI = 0;
		for (Drawable drawable : drawables) {
			if (ImGui.treeNode(MappingsUtil.intermediaryToYarn(drawable.getClass()) + "##" + drawableI)) {
				ImGui.text("Widget? " + Util.bool(drawable, Widget.class));
				ImGui.text("Layout widget? " + Util.bool(drawable, LayoutWidget.class));

				if (drawable instanceof Widget widget) {
					int[] tempX = widgetXCache.computeIfAbsent(widget, w -> new int[]{ widget.getX() });
					int[] tempY = widgetYCache.computeIfAbsent(widget, w -> new int[]{ widget.getY() });

					ImGui.text("Widget info:");

					ImGui.text("X:");
					if (ImGui.dragInt("##x" + drawableI, tempX)) {
						widget.setX(tempX[0]);
					}

					ImGui.text("Y:");
					if (ImGui.dragInt("##y" + drawableI, tempY)) {
						widget.setY(tempY[0]);
					}

					ImGui.text("Size: " + widget.getWidth() + "," + widget.getHeight());
				}

				if (drawable instanceof ClickableWidget clickableWidget) {
					if (ImGui.button("Click")) {
						clickableWidget.onClick(new Click(clickableWidget.getX(), clickableWidget.getY(), new MouseInput(0, 0)), false);
					}
				}

				ImGui.treePop();
			}

			if (ImGui.isItemHovered() && drawable instanceof Widget widget) {
				ImVec2 pos = Util.guiToWindow(window, new ImVec2(widget.getX(), widget.getY()));
				ImVec2 pos1 = Util.guiToWindow(window, new ImVec2(widget.getX() + widget.getWidth(), widget.getY() + widget.getHeight()));

				Util.basicHighlight(pos, pos1);
			}

			drawableI++;
		}
	}
}
