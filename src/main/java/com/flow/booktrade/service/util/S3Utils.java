package com.flow.booktrade.service.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.flow.booktrade.service.S3TokenService;


public class S3Utils {

    public static String S3_BUCKET;
    
    public static String S3_HOST_NAME;
    
    static {
		Properties props = new Properties();
		try {
			InputStream stream = S3TokenService.class.getResourceAsStream("/S3Resources.properties");
			props.load(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		S3_BUCKET = props.getProperty("BUCKET_NAME");
		S3_HOST_NAME = props.getProperty("HOST_NAME");
    }
	
}
