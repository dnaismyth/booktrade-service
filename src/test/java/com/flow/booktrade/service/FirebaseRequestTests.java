package com.flow.booktrade.service;

import org.json.JSONException;
import org.junit.Ignore;
import org.junit.Test;

import com.flow.booktrade.service.util.firebase.FirebaseAuthentication;
import com.flow.booktrade.service.util.firebase.FirebaseMobilePush;


@Ignore
public class FirebaseRequestTests {

	@Test
	public void testSendNotification() throws JSONException{
		FirebaseMobilePush.sendNotification("----fcm device token---", "Hello", "testing");
	}
	
	@Test
	public void testGetCustomToken(){
		FirebaseAuthentication.createCustomFirebaseToken();
	}
}
