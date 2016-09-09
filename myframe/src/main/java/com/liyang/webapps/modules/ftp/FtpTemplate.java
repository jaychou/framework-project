package com.liyang.webapps.modules.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.log4j.Logger;

import com.liyang.webapps.modules.utils.FileOp;


public class FtpTemplate {
	
	private static Logger logger = Logger.getLogger(FtpTemplate.class);
	

	public static boolean isConnected(String hostname, int post, String username, String password) {
		boolean mark = true;
		FTPClient entity = null;
		try {
			 entity = FtpTemplate.connected(hostname, post, username, password);
			if(entity==null) mark = false;
		} catch (Exception e) {
			mark = false;
			logger.error(e,e);
		} finally {
			entity = null;
		}
		return mark;
	}
	
	/**
	 * 连接目标服务器的FTP服务
	 * @return
	 */
	public static FTPClient connected(String hostname, int post, String username, String password) {
		FTPClient client = new FTPClient();
		try {
			client.connect(hostname, post);
			client.login(username, password);
			if(client.getStatus()==null) {
				logger.error("用户名密码错误");
				return null;
			} else {
				return client;
			}
		} catch (Exception e) {
			logger.error("权限验证出错");
		}
		return null;
	}
	
	
	/*public static void download(String directory, String filename, String localpath, Ftpserver ftpserver) throws IOException {
		FTPClient client = null;
		OutputStream output = null;
		try {
			String source = localpath +File.separator+ filename;
			String tmp = source+".tmp";
			output = new FileOutputStream(tmp);
			String hostname = ftpserver.getHostname();
			String username = ftpserver.getUsername();
			String password = ftpserver.getPassword();
			int post = ftpserver.getPost();
			 
	    	client = FtpTemplate.connected(hostname, post, username, password);
			
		    client.changeWorkingDirectory(directory);  
		    client.setBufferSize(1024); 
		    client.setControlEncoding("GBK"); 
		      //设置文件类型（二进制） 
		     client.setFileType(FTPClient.BINARY_FILE_TYPE); 
		
		     client.retrieveFile(filename, output);
		     output.flush();
		     output.close();
		     output = null;
		     client.disconnect();
		     client = null;
		     FileOp.mvFile(tmp, source);
		       
			 logger.info("完成下载"+ filename + "到" + source );
			 
			 
			 String name = localpath +File.separator+"data";
			 //重命名数据文件
			 FileOp.mvFile(source, name);
			 FileOp.delFile(tmp);
		     
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			if(output!=null) {
				output.close();
			}
			if(client!=null) {
				client = null;
			}
		}
		
		
	}*/
	
	
	public static boolean download(String directory, String filename, String localpath, Ftpserver ftpserver, String lastname) throws IOException {
		FTPClient client = null;
		OutputStream output = null;
		try {
			String source = localpath +File.separator+ filename;
			String tmp = source+".tmp";
			output = new FileOutputStream(tmp);
			String hostname = ftpserver.getHostname();
			String username = ftpserver.getUsername();
			String password = ftpserver.getPassword();
			int post = ftpserver.getPost();
			 
	    	client = FtpTemplate.connected(hostname, post, username, password);
			
		    client.changeWorkingDirectory(directory);  
		    client.setBufferSize(1024); 
		    client.setControlEncoding("GBK"); 
		      //设置文件类型（二进制） 
		     client.setFileType(FTPClient.BINARY_FILE_TYPE); 
		
		     client.retrieveFile(filename, output);
		     output.flush();
		     output.close();
		     output = null;
		    
		     client.disconnect();
		     client = null;
		     
		     //FileOp.delFile(source);
		     FileOp.mvFile(tmp, source);
		       
			 logger.info("完成下载"+ filename + "到" + source );
			 
			 logger.info("重命名为"+lastname);
			 String name = localpath +File.separator+lastname;
			 //重命名数据文件
			 FileOp.mvFile(source, name);
			 FileOp.delFile(tmp);
		     
			 File file = new File(name);
			 long space = file.length();
			 if(space==0) {
				 logger.info("找不到相应的文件");
				 return false;
			 }
			 file = null;
			 return true;
			 
		} catch (Exception e) {
			logger.error(e,e);
			 return false;
		} finally {
			if(output!=null) {
				output.close();
			}
			if(client!=null) {
				client = null;
			}
		}
		
		
	}
	

	
	
	/**
	 * 上传FTP文件
	 * @param source   源文件目录
	 * @param target　目标文件目录
	 * @param ftpserver　目标服务器
	 * @param filter　文件过滤器
	 * @param isupdate　是否进行覆盖更新
	 * @param passivemodel 主动模式与被动模式 2=被动模式
	 */
	public  static  void transport(String source,  String target, Ftpserver ftpserver, FtpFilter filter, int isupdate, int passivemodel) {
		FTPClient client = null;
		try {
			String hostname = ftpserver.getHostname();
			String username = ftpserver.getUsername();
			String password = ftpserver.getPassword();
			int post = ftpserver.getPost();
			 
			 client = FtpTemplate.connected(hostname, post, username, password);
			String[] files = getFiles(source, filter);
			
		    client.changeWorkingDirectory(target);  
		    client.setBufferSize(1024); 
		    client.setControlEncoding("GBK"); 
		      //设置文件类型（二进制） 
		     client.setFileType(FTPClient.BINARY_FILE_TYPE); 
		     
		     //主动模式与被动模式
		     if(passivemodel==2)client.enterLocalPassiveMode();
		     
		     String[] remotefilenames = client.listNames();
		    
		     
		     
		     for(String filename: files) {
		    	if(isupdate==0) {
		    		//覆盖更新
		    		move(source, filename, target, client);
		    	} else {
		    		//不覆盖不更新
		    		if(!exist(filename, remotefilenames))move(source, filename, target, client);
		    	}
		    	
			     
		     }
		        
		     
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			if(client!=null) {
				client = null;
			}
		}
	
	}
	
	
	public static String getFilename(String pathname, FTPFileFilter filter, Ftpserver ftpserver) {
		String filename = null;
		FTPClient client = null;
		try {
			String hostname = ftpserver.getHostname();
			String username = ftpserver.getUsername();
			String password = ftpserver.getPassword();
			int post = ftpserver.getPost();
			 
			 client = FtpTemplate.connected(hostname, post, username, password);
			 
			 client.changeWorkingDirectory(pathname);
			 FTPFile[]  files = client.listFiles(pathname, filter);
			 for(FTPFile file : files) {
				 //System.out.println(file.getName());
				 filename = file.getName();
			 }
		} catch (Exception e) {
			logger.error(e,e);
		}
		return filename;
	}
	
	private static void move(String source, String filename, String target, FTPClient client) throws IOException {
		InputStream fis  = null;
		try {
			
			 //保存临时文件再移动，防止上传文件的时候读取文件
	    	 String path = source + File.separator + filename;
	    	 fis = getInputStream(path);
		     client.storeFile(filename+".tmp", fis); 
		     client.rename(filename+".tmp", filename);
		    // logger.info("同步完成"+ path + "到" + target );
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			if(fis!=null) fis.close();
		}
	}
	
	private static String[] getFiles(String source, FtpFilter filter) {
		String[]  files = null;
		try {
			File file = new File(source);
			if(filter!=null) files = file.list(filter);
			else files = file.list();
		} catch (Exception e) {
			logger.error(e,e);
		}
		return files;
	}

	/**
	 * 获取源文件的二进制流，用来进行文件的写入
	 * @param filename 
	 * @return
	 * @throws SocketException
	 * @throws IOException
	 */
	private static InputStream getInputStream(String filename) throws SocketException, IOException {
		InputStream fis = null;
	
		File srcFile = new File(filename); 
			
	    fis = new FileInputStream(srcFile); 
		
		
		return fis;
	}
	
	
	private static boolean exist(String filename, String[] filenames) {
		boolean mark = false;
		if(filenames==null) return mark;
		for(String temp : filenames) {
			if(temp.equals(filename)) mark=true;
		}
		
		return mark;
	}

}
