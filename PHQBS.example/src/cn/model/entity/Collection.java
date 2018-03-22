package cn.model.entity;

public class Collection extends Basedata{
	private int id;
	private String ownner;
	private String driver;
	private String photo;
	private String deadline;
	private String message;
	private String lng;
	private String lat;
	private String state;
	public Collection() {
		super();
	}
	public Collection(int id, String ownner, String driver, String photo,
			String deadline, String message, String lng, String lat,
			String state) {
		super();
		this.id = id;
		this.ownner = ownner;
		this.driver = driver;
		this.photo = photo;
		this.deadline = deadline;
		this.message = message;
		this.lng = lng;
		this.lat = lat;
		this.state = state;
	}
	public Collection(String ownner, String driver, String photo,
			String deadline, String message, String lng, String lat,
			String state) {
		super();
		this.ownner = ownner;
		this.driver = driver;
		this.photo = photo;
		this.deadline = deadline;
		this.message = message;
		this.lng = lng;
		this.lat = lat;
		this.state = state;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOwnner() {
		return ownner;
	}
	public void setOwnner(String ownner) {
		this.ownner = ownner;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getDeadline() {
		return deadline;
	}
	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	@Override
	public String queryAllSQL() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String queryBySQL(String param) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String queryItemBySQL(String sql) {
		// TODO Auto-generated method stub
		return null;
	}
}
