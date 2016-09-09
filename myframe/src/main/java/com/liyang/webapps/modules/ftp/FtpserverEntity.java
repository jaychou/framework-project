package com.liyang.webapps.modules.ftp;


public class FtpserverEntity implements Ftpserver{
	

	private String name;
	private String hostname;
	private int post;
	private String username;
    private String password;
    
    
    
    
    
    
	
	public FtpserverEntity(String name, String hostname, int post,
			String username, String password) {
		super();
		this.name = name;
		this.hostname = hostname;
		this.post = post;
		this.username = username;
		this.password = password;
	}
	
	public static Ftpserver init(String name, String hostname, int post, String username, String password) {
		Ftpserver server= new FtpserverEntity(name, hostname, post, username, password);
		return server;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public int getPost() {
		return post;
	}
	public void setPost(int post) {
		this.post = post;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
    
	
	

}
