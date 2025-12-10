package me.illia.screeninspector.inspector.info;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.ImVec4;
import me.illia.screeninspector.MappingsUtil;
import me.illia.screeninspector.Util;
import me.illia.screeninspector.imgui.ImGuiTab;
import me.illia.screeninspector.mixin.HandledScreenAccessor;
import net.minecraft.client.Keyboard;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.Window;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;

public class HandledScreenInfo extends ImGuiTab {
	public static int SLOT_WIDTH = 16;
	public static int SLOT_HEIGHT = 16;

	public HandledScreenInfo(Window window, Screen screen, Keyboard keyboard) {
		super(window, screen, keyboard);
	}

	@Override
	public void render() {
		if (screen instanceof HandledScreen<?> handledScreen) {
			ImGui.text("Title pos:");
			int titleX = ((HandledScreenAccessor)handledScreen).screeninspector$getTitleX();
			int titleY = ((HandledScreenAccessor)handledScreen).screeninspector$getTitleY();
			int[] tempTitleX = { titleX };
			int[] tempTitleY = { titleY };

			if (ImGui.dragInt("##titleX", tempTitleX)) {
				((HandledScreenAccessor)handledScreen).screeninspector$setTitleX(tempTitleX[0]);
			}

			if (ImGui.dragInt("##titleY", tempTitleY)) {
				((HandledScreenAccessor)handledScreen).screeninspector$setTitleY(tempTitleY[0]);
			}

			String screenHandlerClass = MappingsUtil.intermediaryToYarn(handledScreen.getScreenHandler().getClass());

			ImGui.text("Handled by:");
			ImGui.textColored(new ImVec4(0.0f, 1.0f, 0.0f, 1.0f), screenHandlerClass);

			slots(handledScreen);
		}
	}

	private void slots(HandledScreen<?> handledScreen) {
		ImGui.text("Slots:");

		for (Slot slot : handledScreen.getScreenHandler().slots) {
			if (ImGui.treeNode("##slot" + slot.id, "" + slot.id)) {
				ImGui.text("Inventory:");
				ImGui.text(MappingsUtil.intermediaryToYarn(slot.inventory.getClass()));

				ImGui.text("Player inventory? " + Util.bool(slot.inventory, PlayerInventory.class));

				if (slot.inventory instanceof PlayerInventory playerInventory) {
					ImGui.text("Player username: " + playerInventory.player.getGameProfile().name());
					ImGui.text("Player UUID: " + playerInventory.player.getGameProfile().id().toString());
				}

				ImGui.text("X: " + slot.x);
				ImGui.text("Y: " + slot.y);

				ImGui.treePop();
			}

			if (ImGui.isItemHovered()) {
				HandledScreenAccessor accessor = ((HandledScreenAccessor)handledScreen);

				int x = accessor.screeninspector$getX() + slot.x;
				int y = accessor.screeninspector$getY() + slot.y;

				ImVec2 pos = Util.guiToWindow(window, new ImVec2(x, y));
				ImVec2 pos1 = Util.guiToWindow(window, new ImVec2(x + SLOT_WIDTH, y + SLOT_HEIGHT));

				Util.basicHighlight(pos, pos1);
			}
		}
	}
}
