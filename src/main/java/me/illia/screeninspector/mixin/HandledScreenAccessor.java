package me.illia.screeninspector.mixin;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HandledScreen.class)
public interface HandledScreenAccessor {
	@Accessor("titleX")
	public int screeninspector$getTitleX();

	@Accessor("titleX")
	public void screeninspector$setTitleX(int titleX);

	@Accessor("titleY")
	public int screeninspector$getTitleY();

	@Accessor("titleY")
	public void screeninspector$setTitleY(int titleY);

	@Accessor("x")
	public int screeninspector$getX();

	@Accessor("x")
	public void screeninspector$setX(int x);

	@Accessor("y")
	public int screeninspector$getY();

	@Accessor("y")
	public void screeninspector$setY(int y);
}
