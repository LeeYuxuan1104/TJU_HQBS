package cn.model.tool;

public class Test {
	public static void main(String[] args) {
		MTDataBaseTool	baseTool=new MTDataBaseTool();
		String sql=
		"create table collection (" +
		"id integer primary key auto_increment," +
		"ownner varchar(20) default null," +
		"driver varchar(20) default null," +
		"photo varchar(255) default null," +
		"deadline datetime default null," +
		"message varchar(255) default null," +
		"lng double default null," +
		"lat double default null," +
		"state integer default null)";
		baseTool.doDBUpdate(sql);
		sql=
		"create table delivery (" +
		"id integer primary key auto_increment," +
		"ownner varchar(20) default null," +
		"driver varchar(20) default null," +
		"model varchar(100) default null," +
		"price double default null," +
		"count integer default null," +
		"lng double default null," +
		"lat double default null," +
		"deadline varchar(32) default null," +
		"goal varchar(500) not null)";
		baseTool.doDBUpdate(sql);
		
		sql=
		"create table driver (" +
		"id varchar(20) primary key not null," +
		"password varchar(20) default null," +
		"carNumber varchar(20) default null," +
		"tel varchar(20) default null," +
		"lng double default null," +
		"lat double default null," +
		"name varchar(20) default null)";
		
		baseTool.doDBUpdate(sql);
		sql=
		"create table good (" +
		"model varchar(100) primary key not null," +
		"brand varchar(20) default null," +
		"information varchar(255) default null," +
		"price double default null," +
		"count integer default null" +
		")";
		baseTool.doDBUpdate(sql);
		
		sql=
		"create table node (" +
		"id varchar(20) primary key not null," +
		"password varchar(20) default null," +
		"lng double default null," +
		"lat double default null," +
		"name varchar(20) default null," +
		"tel varchar(20) default null," +
		"address varchar(500) not null)";
		
		baseTool.doDBUpdate(sql);
		sql="insert into node (id,password,lng,lat,name,tel,address) values ('npx1234500001','1',1,1,'22','123','2222')";
		baseTool.doDBUpdate(sql);
		sql="insert into node (id,password,lng,lat,name,tel,address) values ('npx1234500003','1',1,1,'22','123','2222')";
		int o=baseTool.doDBUpdate(sql);
		System.out.println("o="+o);
		sql=
		"create table node_driver (" +
		"id integer primary key not null auto_increment," +
		"node_id varchar(20) default null," +
		"driver_id varchar(20) default null" +
		")";
		baseTool.doDBUpdate(sql);
	}
}
