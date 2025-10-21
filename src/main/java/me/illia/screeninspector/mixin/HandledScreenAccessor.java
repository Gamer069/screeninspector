package me.illia.screeninspector.mixin;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HandledScreen.class)
public interface HandledScreenAccessor {
	@Accessor("titleX")
	public int robotmod$getTitleX();

	@Accessor("titleX")
	public void robotmod$setTitleX(int titleX);

	@Accessor("titleY")
	public int robotmod$getTitleY();

	@Accessor("titleY")
	public void robotmod$setTitleY(int titleY);
}
