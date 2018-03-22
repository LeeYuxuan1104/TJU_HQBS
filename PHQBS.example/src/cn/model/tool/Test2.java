package cn.model.tool;

import java.util.ArrayList;
import java.util.Iterator;

public class Test2 {
	public static void main(String[] args) {
		ArrayList<String> list=new ArrayList<>();
		list.add("111");
		list.add("222");
		list.add("333");
		@SuppressWarnings("rawtypes")
		Iterator iterator=list.iterator();
		int nSize=list.size();
		int count=0;
		String str="";
		while (iterator.hasNext()) {
			String temp=(String) iterator.next();
			
			if(count<nSize-1){
				str+=temp+"_";
			}else if(count==nSize-1){
				str+=temp;
				break;		
			}
			count++;
		}
		System.out.println("sql="+str);
	}
}
