package cn.model.tool;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;

@SuppressWarnings("deprecation")
public class MTConfig {
	private long    currenttime;
	private String  currentdate;
	
	
	//	数据库交互的工具类;
	public MTConfig() {
		currenttime=System.currentTimeMillis();
		currentdate=transLongToDate(currenttime);
	}
	//	获得当前的时间格式;
	private String transLongToDate(long millisecond) {
	    Date date = new Date(millisecond);
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	    return format.format(date);
	}
	
	//	上传图片的类;
	@SuppressWarnings({"rawtypes" })
	public String uploadMap(HttpServletRequest req,String kind,String folder){
		File file=null;
		//	临时目录;
		String 		pathTemp = req.getSession().getServletContext().getRealPath("/")+ "temp"; 
		//	文件检测;
		file 				 = new File(pathTemp);
		if (!file.exists()) {
			file.mkdirs();
		}
		//  正式目录;
		String 		pathTrue =req.getSession().getServletContext().getRealPath("/")+ "photo"; 
		//	文件检测;
		file 				 = new File(pathTrue);
		if (!file.exists()) {
			file.mkdirs();
		}
		//	Disk上传的内容;
		DiskFileUpload fu 	 = new DiskFileUpload();

		fu.setSizeMax(1 * 1024 * 1024); // 设置允许用户上传文件大小,单位:字节
		fu.setSizeThreshold(4096); 		// 设置最多只允许在内存中存储的数据,单位:字节
		fu.setRepositoryPath(pathTemp); // 设置一旦文件大小超过getSizeThreshold()的值时数据存放在硬盘的目录

		// 开始读取上传信息
		List 	fileItems  	 = null;

		try {
			fileItems = fu.parseRequest(req);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Iterator 	  iter  = fileItems.iterator();   // 依次处理每个上传的文件
		while (iter.hasNext()){

			FileItem  item  = (FileItem) iter.next(); // 忽略其他不是文件域的所有表单信息
			
			if (!item.isFormField()){

				String name = item.getName();		  // 获取上传文件名,包括路径
				
				pathTrue=pathTrue+File.separator+kind+File.separator+folder;
					
				file=new File(pathTrue);
				if(!file.exists()){
					file.mkdirs();
				}
				long   size = item.getSize();
				if ((name == null || name.equals("")) && size == 0)
					continue;
				file 	    = new File(pathTrue, name+".jpg");
				try {
					item.write(file);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return "success";
			}
		}
		return "fail";
	}
	//	获得当前的时间以及毫秒数;
	public long getCurrenttime() {
		return currenttime;
	}
	public void setCurrenttime(long currenttime) {
		this.currenttime = currenttime;
	}
	public String getCurrentdate() {
		return currentdate;
	}
	public void setCurrentdate(String currentdate) {
		this.currentdate = currentdate;
	}
}
