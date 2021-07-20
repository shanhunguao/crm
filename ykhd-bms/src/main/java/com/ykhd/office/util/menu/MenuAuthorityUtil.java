package com.ykhd.office.util.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ykhd.office.domain.resp.FirstMenu;
import com.ykhd.office.domain.resp.SecondMenu;
import com.ykhd.office.domain.resp.SetupMenu;
import com.ykhd.office.util.menu.MenuAuthority.MenuFirst;
import com.ykhd.office.util.menu.MenuAuthority.MenuSecond;
import com.ykhd.office.util.menu.MenuAuthority.MenuThird;

public final class MenuAuthorityUtil {

	/**
	 * 超级管理员权限导航菜单
	 */
	public static List<FirstMenu> superAdminMenu() {
		HashMap<String, List<SecondMenu>> childMap = new HashMap<>();
		Arrays.stream(MenuSecond.values()).forEach(v -> {
			String first;
			List<SecondMenu> secondList = childMap.getOrDefault(first = v.name().substring(0, 1), new ArrayList<>());
			secondList.add(new SecondMenu(v.getTitle(), v.getUri(), v.getMethod()));
			childMap.put(first, secondList);
		});
		List<FirstMenu> list = new ArrayList<>();
		Arrays.stream(MenuFirst.values()).forEach(v -> {
			list.add(new FirstMenu(v.getTitle(), v.getSign(), v.getIcon(), childMap.get(v.name())));
		});
		return list;
	}

	/**
	 * 分管理员权限导航菜单
	 */
	public static List<FirstMenu> adminMenu(List<String> auths) {
		List<String> authList = auths != null ? auths : Collections.emptyList();
		HashMap<String, List<SecondMenu>> childMap = new HashMap<>();
		Arrays.stream(MenuSecond.values()).filter(v -> authList.contains(v.name())).forEach(v -> {
			String first;
			List<SecondMenu> secondList = childMap.getOrDefault(first = v.name().substring(0, 1), new ArrayList<>());
			secondList.add(new SecondMenu(v.getTitle(), v.getUri(), v.getMethod()));
			childMap.put(first, secondList);
		});
		List<FirstMenu> list = new ArrayList<>();
		Arrays.stream(MenuFirst.values()).filter(v -> childMap.containsKey(v.name())).forEach(v -> {
			list.add(new FirstMenu(v.getTitle(), v.getSign(), v.getIcon(), childMap.get(v.name())));
		});
		return list;
	}

	/**
	 * 分管理员拥有的三级操作菜单标记
	 */
	public static List<String> operateMenuSign(List<String> auths) {
		List<String> authList = auths != null ? auths : Collections.emptyList();
		return Arrays.stream(MenuThird.values()).filter(v -> authList.contains(v.name())).map(v -> v.getSign()).collect(Collectors.toList());
	}

	/**
	 * 权限菜单设置信息
	 */
	public static List<SetupMenu> menuSetupInfo(List<String> auths) {
		final List<String> authList = auths != null ? auths : Collections.emptyList();
		HashMap<String, List<SetupMenu>> map2 = new HashMap<>();
		Arrays.stream(MenuThird.values()).forEach(v -> {
			String two;
			List<SetupMenu> list = map2.getOrDefault(two = v.name().substring(0, 2), new ArrayList<>());
			list.add(new SetupMenu().setName(v.name()).setTitle(v.getTitle()).setSelected(authList.contains(v.name())));
			map2.put(two, list);
		});
		HashMap<String, List<SetupMenu>> map1 = new HashMap<>();
		Arrays.stream(MenuSecond.values()).forEach(v -> {
			String first;
			List<SetupMenu> list = map1.getOrDefault(first = v.name().substring(0, 1), new ArrayList<>());
			list.add(new SetupMenu().setName(v.name()).setTitle(v.getTitle()).setChildren(map2.get(v.name())).setSelected(authList.contains(v.name())));
			map1.put(first, list);
		});
		List<SetupMenu> list = new ArrayList<>(map1.size());
		Arrays.stream(MenuFirst.values()).filter(v -> map1.containsKey(v.name())).forEach(v -> {
			list.add(new SetupMenu().setName(v.name()).setTitle(v.getTitle()).setChildren(map1.get(v.name())));
		});
		return list;
	}
	
	/**
	 * 查询uri, method对应的菜单常量
	 */
	public static String getNameByURI(String uri, String method) {
		Optional<MenuSecond> optional = Arrays.stream(MenuSecond.values()).filter(v -> v.getUri().equals(uri) && v.getMethod().equalsIgnoreCase(method)).findFirst();
		if (optional.isPresent())
			return optional.get().name();
		Optional<MenuThird> optional_ = Arrays.stream(MenuThird.values()).filter(v -> v.getUri().equals(uri) && v.getMethod().equalsIgnoreCase(method)).findFirst();
		return optional_.isPresent() ? optional_.get().name() : null;
	}
	
}
