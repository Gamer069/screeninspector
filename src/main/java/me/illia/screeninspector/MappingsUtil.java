// stolen from https://github.com/kr1viah/redstonetools-mod/blob/08a2e576bb5fc56ec470c6af6454af7677d0c1b6/src/client/java/tools/redstone/redstonetools/utils/MappingUtils.java
// thanks @.kr1v on dc

package me.illia.screeninspector;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.lib.mappingio.MappingReader;
import net.fabricmc.loader.impl.lib.mappingio.tree.MappingTree;
import net.fabricmc.loader.impl.lib.mappingio.tree.MemoryMappingTree;
import net.minecraft.MinecraftVersion;
import net.minecraft.client.MinecraftClient;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.zip.GZIPInputStream;

public class MappingsUtil {
	private static final Map<String, String> cachedClasses = new HashMap<>();
	private static final MemoryMappingTree tree = new MemoryMappingTree();
	private static final Path mappingsPath = MinecraftClient.getInstance().runDirectory.toPath().resolve(".tiny").resolve("yarn-" + MinecraftVersion.create().name() + "+build.1-tiny");

	public static String intermediaryToYarn(Class<?> intermediaryClass) {
		String intermediaryName = intermediaryClass.getName();
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) return intermediaryName; // already yarn

		String named = cachedClasses.getOrDefault(intermediaryName, null);
		if (named != null) return named;
		for (MappingTree.ClassMapping c : tree.getClasses()) {
			String inter = c.getDstName(0).replace("/", ".");
			if (Objects.equals(inter, intermediaryName)) {
				named = c.getDstName(1).replace("/", ".");
				cachedClasses.put(inter, named);
				break;
			}
		}
		if (named == null) named = intermediaryName;
		return named;
	}

	public static String intermediaryToYarnSimple(Class<?> intermediaryClass) {
		String yarnName = intermediaryToYarn(intermediaryClass);
		return yarnName.substring(yarnName.lastIndexOf(".") + 1);
	}

	static {
		try {
			if (!mappingsPath.toFile().exists()) {
				String version = MinecraftVersion.create().name();
				String url = "https://maven.fabricmc.net/net/fabricmc/yarn/" + version + "%2Bbuild.1/yarn-" + version + "%2Bbuild.1-tiny.gz";

				var gzPath = mappingsPath.resolveSibling("temp.gz");
				InputStream in = URI.create(url).toURL().openStream();

				if (!Files.exists(gzPath)) {
					Files.createDirectories(gzPath.getParent());
					Files.createFile(gzPath);
				}

				Files.copy(in, gzPath, StandardCopyOption.REPLACE_EXISTING);
				GZIPInputStream gis = new GZIPInputStream(new FileInputStream(gzPath.toFile()));
				FileOutputStream fos = new FileOutputStream(mappingsPath.toFile());

				byte[] buffer = new byte[8192];
				int len;
				while ((len = gis.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}

				Files.delete(gzPath);
			}

			MappingReader.read(mappingsPath, tree);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
