package com.ldz.util.commonUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.springframework.util.FileCopyUtils;

/**
 * @since 2017/11/1
 */
public class FileUtil {
    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
    	fileExistsDir(filePath);
        FileOutputStream out = new FileOutputStream(filePath+fileName);
        out.write(file);
        out.flush();
        out.close();
    }
    
    /**
     * 判断目录是否存在，不在就创建
     * @param filePath
     */
    public static void fileExistsDir(String filePath){
    	File targetFile = new File(filePath);
    	try {
    		if(!targetFile.exists()){
                targetFile.mkdirs();
            }
		} catch (Exception e) {
			// TODO: handle exception
		}
        
    }
    
    public static void uploadCopyFile(InputStream  f,String filePath, String fileName) throws Exception {
    	fileExistsDir(filePath);
        FileOutputStream out = new FileOutputStream(filePath+fileName);
        //File out = new File(filePath+fileName);
        FileCopyUtils.copy(f,out);
        try {
        	
        	out.flush();
        	out.close();
		} catch (Exception e) {
			// TODO: handle exception
		} 
        //out.write(file);
        //out.flush();
        //out.close();
    }
}
