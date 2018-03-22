package com.model.tool.system;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class MTWritepadHelper extends MTImgHelper{
	private String path=null;
	private Bitmap zoombm = null;
	
	//	构造函数的内容;
	public MTWritepadHelper(Object object,String folderpath,String filename) {
		super();
		this.zoombm		=getWriteImg(object, folderpath, filename);
	}
	
	//	获得路径;
	public String getPath() {
		return path;
	}

	//	获得图片的内容;
	public Bitmap getZoombm() {
		return zoombm;
	}


	//	进行手写板图像的绘制;
	public Bitmap getWriteImg(Object object,String folderPath,String fileName){
		Bitmap 		zoombm = null;
		//	进行数据的长宽设置;
		Bitmap mSignBitmap = (Bitmap) object;
		path    		   = getPhoto(mSignBitmap,folderPath,fileName);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 15;
		options.inTempStorage = new byte[5 * 1024];
		try {										
			zoombm	  = BitmapFactory.decodeFile(path, options);
		} catch (Exception e) {
			zoombm	  = null;
		}
		return zoombm;
	}
	//	进行照片的获取
	private String getPhoto(Bitmap mSignBitmap,String folderPath,String fileName) {
		ByteArrayOutputStream 	baos 	  = null;
		File					file	  = null;
		String 					filePath  = null;
		try {
			file	  	= new File(folderPath);
			//	生成文件夹的方式;
			if(!file.exists()){
				file.mkdirs();
			}
			filePath					  = folderPath+File.separator+fileName+".jpg";
			file=new File(filePath);
			if(!file.exists()){
				file.createNewFile();
			}
			baos 						  = new ByteArrayOutputStream();
			mSignBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			byte[] 			  photoBytes  = baos.toByteArray();
			if (photoBytes != null) {
				new FileOutputStream(new File(filePath)).write(photoBytes);
			}

		} catch (IOException e) {
			return null;
		} finally {
			try {
				if (baos != null)
					baos.close();
			} catch (IOException e) {
				return null;
			}
		}
		return filePath;
	}
}
