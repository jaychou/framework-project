package com.liyang.webapps.modules.ftp;

import java.io.File;
import java.io.FilenameFilter;

public class FtpFilter implements FilenameFilter{
	
	private String prefix;
	private String suffix;
	private String excludes;
	
	
	
	public FtpFilter(String prefix, String suffix, String excludes) {
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
	public boolean accept(File dir, String name) {
		boolean mark = true;
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
