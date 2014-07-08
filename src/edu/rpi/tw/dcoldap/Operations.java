package edu.rpi.tw.dcoldap;

import java.io.File;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.io.File;
import java.io.IOException;
import java.security.PrivateKey;
import java.util.Vector;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.common.io.Files;
import com.sun.jersey.spi.resource.Singleton;

import org.apache.directory.api.ldap.model.cursor.CursorException;
import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;

/**
* @author yu chen
*/

// @Path here defines class level path. Identifies the URI path that&nbsp;
// a resource class will serve requests for.
@Path("/UserInfoService")
@Singleton
@Produces("application/xml")
public class Operations {

	// @GET here defines, this method will method will process HTTP GET
	// requests.
	@GET
	// @Path here defines method level path. Identifies the URI path that a
	// resource class method will serve requests for.
	@Path("/name/{i}")
	// @Produces here defines the media type(s) that the methods
	// of a resource class can produce.
	@Produces(MediaType.TEXT_XML)
	// @PathParam injects the value of URI parameter that defined in @Path
	// expression, into the method.
	public String userName(@PathParam("i") String i) {
	
		String name = i;
		return "<User>" + "<Name>" + name + "</Name>" + "</User>";
		
	}
	
	@GET
	@Path("/info/")
	@Produces(MediaType.TEXT_XML)
	public String info() throws LdapException, CursorException {
		LdapConnection connection = new LdapNetworkConnection( "deepcarbon.tw.rpi.edu", 10389 );
		if(connection!=null){
			System.out.println("Conection not null");
		}
		
		connection.bind( "uid=admin, ou=system", "secret" );
		System.out.println("UID bind successful");
		EntryCursor cursor = connection.search( "ou=users,ou=system", "(objectclass=*)", SearchScope.ONELEVEL, "*" );
		int counter = 0;
		while ( cursor.next() )
		{
		     Entry entry = cursor.get();
		     counter+=1;
		     System.out.println(entry.toString());
		}
		System.out.println("A total number of "+counter+" entries");
		connection.unBind();
		return "<User><Name>"+counter+"</Name></User>";
	}
	
	@POST
	@Path("/requestpw")
	@Consumes("application/xml")
	@Produces("application/xml")
	public String requestPw(UserInfo userinfo) throws LdapException, CursorException {
		/**
		 * Request password for a user
		 */
		
		/*
		LdapConnection conn = new LdapNetworkConnection( "deepcarbon.tw.rpi.edu", 10389 );
		
		conn.bind( "uid=admin, ou=system", "secret" );
		EntryCursor cursor = conn.search( "ou=users,ou=system", "(objectclass=*)", SearchScope.ONELEVEL, "*" );
		
		String userEmail = "";
		while ( cursor.next() )
		{
		     Entry entry = cursor.get();
		     System.out.println("uid value: "+entry.get("uid").getString());
		     System.out.println("user info: "+userinfo.toString());
		     if(entry.get("uid").getString().equals(userinfo.getUid())){
		    	 userEmail = entry.get("mail").getString();
		    	 break;
		     }
		}
		*/
		System.out.println(userinfo);
		
		return "<User><email>"+userinfo+"</email></User>";
	}
	
	@GET
	@Path("/age/{j}")
	@Produces(MediaType.TEXT_XML)
	public String userAge(@PathParam("j") int j) {
	
		int age = j;
		return "<User>" + "<Age>" + age + "</Age>" + "</User>";
	}
	
	public File createXML(String uid,String pw,String email, String status, String action) throws TransformerException{
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			String[] labelArray = {"uid","pw","email","status","action"};
			String[] elementArray = {uid,pw,email,status,action};
			docBuilder = docFactory.newDocumentBuilder();
			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("UserInfo");
			doc.appendChild(rootElement);
	 
			for(int i=0;i<labelArray.length;i++){
				Element eachElement = doc.createElement(labelArray[i]);
				eachElement.appendChild(doc.createTextNode(elementArray[i]));
				rootElement.appendChild(eachElement);
			}
						
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			File outputFile = new File("output.xml");
			StreamResult result = new StreamResult(outputFile);
			transformer.transform(source, result);
			
			return outputFile;
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
		return null;
		
		
	}


}
