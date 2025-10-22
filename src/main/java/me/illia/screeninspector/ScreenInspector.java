package me.illia.screeninspector;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

public class ScreenInspector implements ModInitializer {
	public static final String MODID = "screeninspector";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
	public static boolean DEVTOOLS_ENABLED = false;
	public static boolean MINECRAFT_WANT_UNLOCK = false;

	@Override
	public void onInitialize() {
	}
}
