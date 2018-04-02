package com.model.entity;

import com.model.tool.system.MTConfigure;

public abstract class Information {
	public MTConfigure mtConfigure;
	public Information() {
		if(mtConfigure==null){
			mtConfigure=new MTConfigure();
		}
	}
}
