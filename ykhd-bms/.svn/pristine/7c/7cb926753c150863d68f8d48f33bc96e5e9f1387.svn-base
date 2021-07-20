package com.ykhd.office.util;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.ykhd.office.domain.bean.MsgSentBean;

/**
 * 消息推送助手
 */
public final class MsgSentHelper {

	private static Map<Integer, PrintWriter> pw_map = new HashMap<>();
	private static Map<Integer, Thread> thread_map = new HashMap<>();
	
	public static void add(Integer id, PrintWriter pw, Thread thread) {
		pw_map.put(id, pw);
		thread_map.put(id, thread);
	}
	
	public static boolean online(Integer id) {
		return pw_map.containsKey(id);
	}
	
	public static void destroy(Integer id) {
		Thread old_thread = thread_map.get(id);
		if (old_thread != null)
			old_thread.interrupt();
		thread_map.remove(id);
		PrintWriter pw = pw_map.get(id);
		if (pw != null)
			pw.close();
		pw_map.remove(id);
	}
	
	public static void cleanPW(Integer id) {
		pw_map.remove(id);
	}
	
	public static void sendMsg(MsgSentBean msg) {
		if (pw_map.containsKey(msg.getReceiver())) {
			PrintWriter pw = pw_map.get(msg.getReceiver());
			if (pw != null) {
				pw.write("data:" + JacksonHelper.toJsonStr(msg) + "\n\n");
				pw.flush();
			} else
				pw_map.remove(msg.getReceiver());
		}
	}
}
