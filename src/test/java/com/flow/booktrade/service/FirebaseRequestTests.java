package com.flow.booktrade.service;

import org.json.JSONException;
import org.junit.Test;

import com.flow.booktrade.service.util.firebase.FirebaseAuthentication;
import com.flow.booktrade.service.util.firebase.FirebaseMobilePush;


//@Ignore
public class FirebaseRequestTests {

	@Test
	public void testSendNotification() throws JSONException{
		FirebaseMobilePush.sendNotification("D964B8799581423F7E1B6C22E81C0F044CC2B95D2030483C4F3A4FC7454CA354", "Hello", "testing");
	}
	
	@Test
	public void testGetCustomToken(){
		FirebaseAuthentication.createCustomFirebaseToken();
	}
}
