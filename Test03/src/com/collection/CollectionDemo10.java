package com.collection;

import java.util.ArrayList;
import java.util.Collection;

public class CollectionDemo10 {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {
		 // 创建集合对象
        Collection c = new ArrayList();

        // 添加元素
        c.add("hello"); // Object obj = "hello"; 向上转型
        c.add("world");
        c.add("java");

        Object[] objs = c.toArray();
        for (int x = 0; x < objs.length; x++) {
            // System.out.println(objs[x]);
            // 我知道元素是字符串，我在获取到元素的的同时，还想知道元素的长度。
            // System.out.println(objs[x] + "---" + objs[x].length());
            // 上面的实现不了，原因是Object中没有length()方法
            // 我们要想使用字符串的方法，就必须把元素还原成字符串
            // 向下转型
            String s = (String) objs[x];
            System.out.println(s + "---" + s.length());
        }
	}
}
