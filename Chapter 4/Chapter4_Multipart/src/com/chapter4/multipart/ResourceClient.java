package com.chapter4.multipart;

import java.io.File;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

public class ResourceClient {
	private Client client ;
	
	private final static String HOST_NAME = "http://localhost:8080/Chapter4_Multipart/services/" ;
	
	public ResourceClient(){
		client = ClientBuilder.newBuilder()
				.register(MultiPartFeature.class)
		        .build();
	}
	
	public static void main(String[] args) {
		ResourceClient resourceClient = new ResourceClient();
		resourceClient.callMultipart();
		resourceClient.callMultipartWithBean();
		resourceClient.callMultipartWithFile();
	}

	public void callMultipart(){
		WebTarget target = client.target(HOST_NAME + "multipartResource");

		 final FormDataMultiPart mp = new FormDataMultiPart();
	        final FormDataBodyPart p = new FormDataBodyPart(FormDataContentDisposition.name("part").fileName("file").build(),
	                "CONTENT");
	        mp.bodyPart(p);

        final String s = target.request().post(Entity.entity(mp, MediaType.MULTIPART_FORM_DATA_TYPE), String.class);
        System.out.println("Response Data: " + s);
	}
	
	public void callMultipartWithBean(){
		WebTarget target = client.target(HOST_NAME + "multipartResource/withBean");

		final FormDataMultiPart mp = new FormDataMultiPart();
        mp.bodyPart(new FormDataBodyPart(FormDataContentDisposition.name("bean").fileName("bean").build(),new User(1,"John"), MediaType.APPLICATION_XML_TYPE));
        mp.bodyPart(new FormDataBodyPart(FormDataContentDisposition.name("string").fileName("string").build(),"STRING"));

        final String s = target.request().post(Entity.entity(mp, MediaType.MULTIPART_FORM_DATA_TYPE), String.class);

        System.out.println("Response Data: " + s);
	}
	
	public void callMultipartWithFile(){
		File file = new File("G:\\Black-earth.jpg");
		FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file", file, MediaType.APPLICATION_OCTET_STREAM_TYPE);

		final MultiPart multipart = new FormDataMultiPart().bodyPart(fileDataBodyPart);
		
	    WebTarget target = client.target(HOST_NAME + "multipartResource/upload");
	    String response = target.request().post(Entity.entity(multipart, MediaType.MULTIPART_FORM_DATA_TYPE), String.class);
	    System.out.println("Response: " + response);
	}
}
