package com.pilot.util;

import java.util.Date;

public interface Writer {
	public void setImage(String fixedPath);   
	public void setContent(String content);   
	public void setPassword(String password); 
	public void setRegdate(Date date);
	public void setUser(Integer id); 
}
