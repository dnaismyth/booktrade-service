package com.flow.booktrade.web.rest.vm;

import java.io.Serializable;

import com.amazonaws.auth.BasicSessionCredentials;

public class S3TokenResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String secretKey;
	private String accessKey;
	private String sessionToken;
	private String s3Bucket;
	
	public S3TokenResponse(BasicSessionCredentials credentials, String s3Bucket){
		this.secretKey = credentials.getAWSSecretKey();
		this.accessKey = credentials.getAWSAccessKeyId();
		this.sessionToken = credentials.getSessionToken();
		this.s3Bucket = s3Bucket;
	}
		
	public String getSecretKey(){
		return secretKey;
	}
	
	public String getAccessKey(){
		return accessKey;
	}
	
	public String getS3Bucket(){
		return s3Bucket;
	}
	
	public void setS3Bucket(String s3Bucket){
		this.s3Bucket = s3Bucket;
	}
	
	public String getSessionToken(){
		return sessionToken;
	}
}
