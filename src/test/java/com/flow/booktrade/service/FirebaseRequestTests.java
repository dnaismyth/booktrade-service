package com.flow.booktrade.service;

import org.json.JSONException;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flow.booktrade.service.util.firebase.FirebaseAuthentication;
import com.flow.booktrade.service.util.firebase.FirebaseDatabase;
import com.flow.booktrade.service.util.firebase.FirebaseMobilePush;


//@Ignore
public class FirebaseRequestTests {
	
	private static final Logger log = LoggerFactory.getLogger(FirebaseRequestTests.class);


	@Test
	public void testSendNotification() throws JSONException{
		FirebaseMobilePush.sendNotification("----fcm device token---", "Hello", "testing");
	}
	
	@Test
	public void testGetCustomToken(){
		String token = FirebaseAuthentication.createCustomFirebaseToken();
		log.info(token);
	}
	
	@Test
	public void testUpdateUser(){
		FirebaseDatabase.updateUserProperties(1L,"Testing", "testing", "testing");
	}

}
