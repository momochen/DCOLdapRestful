package edu.rpi.tw.dcoldap;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserInfo {

  private String uid;
  private String pw;
  private String email;
  private String status;
  private String action; 
  
  public enum Action{
	  USERNEW,USERCHANGEPW,USERREQUESTPW,USERCHECKVALID
  }
  
  public enum Status{
	  INITIAL,PENDING,ACTIVATED
  }
  
  public UserInfo(){
  }
  
  public UserInfo(String id,String pw,String email,String status,String action){
	  this.uid = id;
	  this.pw = pw;
	  this.email = email;
	  this.status = status;
	  this.action = action;
  }
  
  public String toString(){
	  
	return "uid:"+this.uid+
			" pw:"+this.pw+
			" email:"+this.email+
			" status:"+this.status+
			" action:"+this.action;
	  
  }
  public String getUid() {
    return uid;
  }

  public void setUid(String id) {
    this.uid = uid;
  }

  public String getPassword(){
	  return pw;
  }
  
  public void setPassword(String pw){
	  this.pw = pw;
  }
  
  public void setEmail(String email){
	  this.email = email;
  }

  public String getEmail(){
	  return this.email;
  }
  
  public void setStatus(String status){
	  boolean validity = false;
	  for(Status s: Status.values()){
		  if (s.name().equals(status)){
			  validity = true;
			  this.status = status;
			  break;
		  }
	  }
	  
	  if(validity==false){
		  this.status = "";
	  }
  }
  
  public String getStatus(){
	  return this.status;
  }
  
  public void setAction(String action){
	  boolean validity = false;
	  for(Action a: Action.values()){
		  if (a.name().equals(action)){
			  validity = true;
			  this.action = action;
			  break;
		  }
	  }
	  if(validity==false){
		  this.action = "";
	  }
  }
  
  public String getAction(){
	  return this.action;
  }
  
}