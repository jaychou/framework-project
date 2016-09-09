package com.liyang.webapps.modules.ftp;




import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;

public class MyFTPFileFilter implements FTPFileFilter{

	private String prefix;
	private String suffix;
	private String excludes;
	
	
	
	public MyFTPFileFilter(String prefix, String suffix, String excludes) {
		super();
		this.prefix = prefix;
		this.suffix = suffix;
		this.excludes = excludes;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public String getExcludes() {
		return excludes;
	}
	public void setExcludes(String excludes) {
		this.excludes = excludes;
	}
	
	@Override
	public boolean accept(FTPFile file) {
		boolean mark = true;
		String name = file.getName();
		if(prefix!=null && !name.startsWith(prefix)) {
			mark = false;
		}
		
		if(suffix!=null && !name.endsWith(suffix)) {
			mark = false;
		}
		
		if(excludes!=null) {
			String[] exs = excludes.split(",");
			for(String ex:exs) {
				if(name.equals(ex)) mark = false;
			}
		}
	
		return mark;
	}
	
	
	
	

}
