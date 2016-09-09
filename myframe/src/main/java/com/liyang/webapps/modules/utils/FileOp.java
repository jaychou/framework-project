package com.liyang.webapps.modules.utils;


import java.io.*;
import java.util.Calendar;

import org.apache.log4j.Logger;

public class FileOp {
	private final static Logger log = Logger.getLogger(FileOp.class);

	public void FileCopy() {

	}
	
	/**
	 * 文件是否已存在
	 * 
	 * 陈海生
	 */
	public static boolean isFile(String path){ 
	    try{
	      File f=new File( path );
	      if (f.exists()){
	        return true;
	      }
	    }
	    catch (Exception e)
	    {
	    	System.out.print(e.getMessage());
	    	return false;
	    }
	    return false;
	  }

	private void copyFile1(String src, String des) throws Exception {
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			java.io.File f1 = new java.io.File(src);
			java.io.File f2 = new java.io.File(des);
			if (!f1.isFile()) {
				throw new Exception("源文件不存在:" + src);
			}

			if (!f2.exists()) {
				f2.createNewFile();
			}

			// do straight file copy
			in = new FileInputStream(src);
			out = new FileOutputStream(des);
			byte[] buffer = new byte[1024 * 4]; // 4k buffer
			int len = 0;
			while ((len = in.read(buffer, 0, buffer.length)) > 0) {
				out.write(buffer, 0, len);
			}
			
			out.flush();
		} catch (Exception e) {
			throw new Exception("copy文件出错!");
		} finally {
			if (in != null)
				in.close();
			if (in != null)
				out.close();
		}
	}

	/**
	 * 创建一个文件夹
	 * @param path
	 * @throws Exception
	 */
	public static void mkDir(String path) throws Exception {

		try {

			java.io.File f1 = new java.io.File(path);
			if (!f1.isDirectory())
				f1.mkdirs();

		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new Exception(ex.getMessage());
		} finally {
		}
	}
	
	/**
	 * 取得最后修改时间
	 * 0 为找不到文件或文件夹
	 * */
	public static long getLastModiTime(String path) {
		long l = 0;
		try {
			//System.out.println("--------------:"+path);
			java.io.File f1 = new java.io.File(path);
			if (f1.isFile() || f1.isDirectory())
				l = f1.lastModified();

		} catch (Exception ex) {
			log.error(ex.getMessage());
			//throw new Exception(ex.getMessage());
		}

		return l;
	}
	
	/**
	 * 取得最后修改时间，两个文件取大的那个
	 * 0 为找不到文件或文件夹
	 * */
	public static long getLastModiTime(String path,String path1) {
		long l = 0;
		try {
			long l1=0;
			long l2=0;
			java.io.File f1 = new java.io.File(path);
			if (f1.isFile() || f1.isDirectory())
				l1 = f1.lastModified();
			
			f1 = new java.io.File(path1);
			if (f1.isFile() || f1.isDirectory())
				l2 = f1.lastModified();

			if (l1>l2) l=l1;
			else l=l2;
		} catch (Exception ex) {
			log.error(ex,ex);
			//throw new Exception(ex.getMessage());
		}

		return l;
	}

	/**
	 * 拷贝一个文件
	 * @param opath     源文件
	 * @param lpath     目标文件
	 * @throws Exception
	 */
	public static void copyFile(String opath, String lpath) throws Exception {

		try {
			java.io.File f1File = new java.io.File(opath);
			if (f1File.isFile()) { // f1File.createNewFile();
				FileOp cp = new FileOp();
				cp.copyFile1(opath, lpath);
			}
		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new Exception(ex.getMessage());
		} finally {
		}
	}

	/**
	 * 移动一个文件
	 * @param opath    源文件
	 * @param lpath    目标文件
	 * @throws Exception
	 */
	public static void mvFile(String opath, String lpath) throws Exception {

		try {
			java.io.File f1File = new java.io.File(opath);
			if (f1File.isFile()) {
				copyFile(opath, lpath);
				f1File.delete();
			}
		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new Exception(ex.getMessage());
		} finally {
		}
	}

	public static long getFileSize(String path){ 
		long l=0;
	    try{
	      File f=new File( path );
	      if (f.isFile()){
	        l=f.length();
	      }
	    }
	    catch (Exception e)
	    {
	    	e.printStackTrace();
	    }
	    return l;
	  }
	
	/**
	 * 删除一个文件
	 * @param opath
	 * @throws Exception
	 */
	public static void delFile(String opath) throws Exception {

		try {
			java.io.File f1File = new java.io.File(opath);
			if (f1File.isFile()) {
				f1File.delete();
			}
		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new Exception(ex.getMessage());
		} finally {
		}
	}

	/**
	 * 删除文件夹下所有内容，包括此文件夹
	 * @param spath
	 * @return
	 */
	public static boolean delAll(String spath) {
		boolean b = false;
		if (Utils.bBlank(spath))
			return b;
		File f = new File(spath);
		delAll(f, spath);
		return true;
	}

	/**
	 * 删除文件夹下所有N天前的内容，不包括此文件夹 N=0时，表示删除所有
	 * @param spath
	 * @param nday
	 * @param i_re
	 * @return
	 */
	public static int delTimeFile(String spath, int nday) {
		int i_re = 0;
		if (Utils.bBlank(spath))
			return i_re;
		File f = new File(spath);
		i_re = delTimeFile(f, nday, i_re);
		return i_re;
	}

	/**
	 * 删除文件夹下所有内容，包括此文件夹
	 * @param f
	 * @param spath
	 */
	public static void delAll(File f, String spath) {
		if (!f.exists()) { // 如果文件夹不存在
			log.info("指定目录不存在:" + spath);
			return;
		}
		boolean rslt = true; // 保存中间结果
		if (!(rslt = f.delete())) { // 先尝试直接删除
			// 若文件夹非空。枚举、递归删除里面内容
			File subs[] = f.listFiles();
			for (int i = 0; i <= subs.length - 1; i++) {
				if (subs[i].isDirectory())
					delAll(subs[i], spath); // 递归删除子文件夹内容
				rslt = subs[i].delete(); // 删除子文件夹本身
			}
			rslt = f.delete(); // 删除此文件夹本身
		}
		if (!rslt)
			log.error("无法删除:" + f.getName());
		return;
	}

	/**
	 * 删除文件夹下所有N天前的内容，不包括此文件夹 N=0时，表示删除所有
	 * @param f
	 * @param nday
	 * @param i_re
	 * @return
	 */
	public static int delTimeFile(File f, int nday, int i_re) {
		if (!f.exists()) // 如果文件夹不存在
			log.error("指定目录不存在:" + f.getName());

		// 若文件夹非空。枚举、递归删除里面内容
		File subs[] = f.listFiles();
		if (subs != null) {
			for (int i = 0; i <= subs.length - 1; i++) {
				if (subs[i].isDirectory()) {
					i_re = delTimeFile(subs[i], nday, i_re); // 递归删除子文件夹内容
					// subs[i].delete(); //删除子文件夹本身
				} else {
					if (nday == 0 || fileTimeOver(subs[i], nday)) { // 按时间删除文件
						// System.out.println("删除文件:"+subs[i].getName());
						if (!subs[i].delete()) {
							log.error("无法删除文件:" + subs[i].getName());
						} else
							i_re++;
					}
				}

			}
		}

		return i_re;
	}

	/**
	 * 判断文件的最后一次修改时间，是否在n天前
	 * @param f
	 * @param nday
	 * @return
	 */
	public static boolean fileTimeOver(File f, int nday) {
		boolean b = false;
		Long time = f.lastModified();

		Calendar c1 = Calendar.getInstance();

		Calendar c2 = Calendar.getInstance();
		c2.setTimeInMillis(time);

		int d = (int) ((c1.getTimeInMillis() - c2.getTimeInMillis()) / (24 * 3600 * 1000));

		if (d > nday)
			b = true;
		return b;
	}

	/**
	 * 拷贝一个文件夹(包括子文件夹)，文件夹不存在，则创建
	 * @param SrcDirectoryPath    源文件夹
	 * @param DesDirectoryPath    目标文件夹
	 * @return
	 */
	public static boolean copyDir(String SrcDirectoryPath,
			String DesDirectoryPath) {

		try {
			File F = new File(SrcDirectoryPath);
			if (F.isFile()) {
				String filename = "";
				if (SrcDirectoryPath.lastIndexOf("/") >= 0)
					filename = SrcDirectoryPath.substring(SrcDirectoryPath
							.lastIndexOf("/"));
				if (SrcDirectoryPath.lastIndexOf("\\") >= 0)
					filename = SrcDirectoryPath.substring(SrcDirectoryPath
							.lastIndexOf("\\"));
				copyFile(SrcDirectoryPath, DesDirectoryPath + filename);
				return true;
			}

			// 創建不存在的目錄
			File F0 = new File(DesDirectoryPath);
			if (!F0.exists()) {
				if (!F0.mkdir()) {
					System.out.println("目标文件夹不存在，创建失败!");
				}
			}

			File[] allFile = F.listFiles(); // 取得當前目錄下面的所有文件，將其放在文件數組中
			int totalNum = allFile.length; // 取得當前文件夾中有多少文件（包括文件夾）
			String srcName = "";
			String desName = "";
			int currentFile = 0;
			// 一個一個的拷貝文件
			for (currentFile = 0; currentFile < totalNum; currentFile++) {
				if (!allFile[currentFile].isDirectory()) {
					// 如果是文件是采用處理文件的方式
					srcName = allFile[currentFile].toString();
					desName = DesDirectoryPath + "\\"
							+ allFile[currentFile].getName();
					copyFile(srcName, desName);
				}
				// 如果是文件夾就采用遞歸處理
				else {
					// 利用遞歸讀取文件夾中的子文件下的內容，再讀子文件夾下面的子文件夾下面的內容...
					if (copyDir(allFile[currentFile].getPath().toString(),
							DesDirectoryPath + "\\"
									+ allFile[currentFile].getName().toString())) {
						// System.out.println("D Copy Successfully!");
					} else {
						System.out.println("SubDirectory Copy Error!");
					}
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
