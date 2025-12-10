package me.illia.screeninspector.console;

import party.iroiro.luajava.JFunction;
import party.iroiro.luajava.Lua;
import party.iroiro.luajava.luajit.LuaJit;

import java.lang.reflect.Field;

public class LuaUtil {
	public static void add(LuaJit lua, Object obj, String name) {
		if (obj != null) {
			lua.push(obj, Lua.Conversion.FULL);
		} else {
			lua.pushNil();
		}

		lua.setGlobal(name);
	}

	public static void add(LuaJit lua, JFunction func, String name) {
		lua.push(func);
		lua.setGlobal(name);
	}
}
