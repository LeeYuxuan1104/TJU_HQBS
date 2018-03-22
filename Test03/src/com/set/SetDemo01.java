package com.set;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SetDemo01 {
	public static void main(String[] args) {

		// 创建list对象
		List<String> list = new ArrayList<String>();

		// 添加数据
		list.add("张三");
		list.add("李四");
		list.add("王二");
		list.add("王二");
		list.add("麻子");

		// 来一个华丽分割线
		System.out.println("--------list--------");

		// 遍历集合元素
		for (String name : list) {
			System.out.println("name" + name);
		}

		// 创建set对象
		Set<String> set = new HashSet<String>();

		// 添加数据
		set.add("张三");
		set.add("李四");
		set.add("王二");
		set.add("王二");
		set.add("麻子");
		// 再来一个华丽的分割线
		System.out.println("--------set--------");

		Iterator<String> it = set.iterator();  
        while (it.hasNext()) {  
            System.out.println(it.next());  
        }  

	}
}
