package cn.model.entity;

import javax.servlet.http.HttpServletRequest;

public class Weixintalk extends Basedata{
	private long id;
	private String node_id;
	private String driver_id;
	private String message;
	private int target;
	private String timename;
	public Weixintalk(long id, String node_id, String driver_id,
			String message, int target, String timename) {
		super();
		this.id = id;
		this.node_id = node_id;
		this.driver_id = driver_id;
		this.message = message;
		this.target = target;
		this.timename = timename;
	}
	public Weixintalk() {
		super();
	
	}
	//	根语句查询;
	@Override
	public String queryBySQL(String[] params,String sql) {
		String result=null;
//		ArrayList<String[]> list	= mtDBTool.query(sql);
//		Iterator 			iterator= list.iterator();
//		JSONArray   		array	= new JSONArray();
//		if(list!=null){			
//			while (iterator.hasNext()) {
//				String[] items=(String[]) iterator.next();
//				//	进行相应对象的赋值;
//				JSONObject obj = new JSONObject();
//				obj.put("id", items[0]);
//				obj.put("message", items[1]);
//				obj.put("target", items[2]);
//				obj.put("timename", items[3]);
//				array.add(obj);
//			}
//			result=array.toString();
//		}
		return result;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNode_id() {
		return node_id;
	}
	public void setNode_id(String node_id) {
		this.node_id = node_id;
	}
	public String getDriver_id() {
		return driver_id;
	}
	public void setDriver_id(String driver_id) {
		this.driver_id = driver_id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getTarget() {
		return target;
	}
	public void setTarget(int target) {
		this.target = target;
	}
	public String getTimename() {
		return timename;
	}
	public void setTimename(String timename) {
		this.timename = timename;
	}
	@Override
	public String addInfo(HttpServletRequest req) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String deleteInfo(HttpServletRequest req) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
