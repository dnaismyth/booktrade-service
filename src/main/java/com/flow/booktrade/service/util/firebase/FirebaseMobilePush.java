package com.flow.booktrade.service.util.firebase;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestTemplate;

public class FirebaseMobilePush {
	private static final Logger log = LoggerFactory.getLogger(FirebaseMobilePush.class);

	private static String apiKey;
	private static String FCM_SEND_URL;
	
	private static final String NOTIFICATION_TITLE = "Active Train Crossing Alert";
	private static final String NOTIFICATION_PRIORITY = "high";
	//private static final String NOTIFICATION_SOUND = "default";

	static {
		log.debug("==================Initializing Credentials================== ");
		Properties props = new Properties();
		try {
			InputStream stream = FirebaseMobilePush.class.getResourceAsStream("/FCMCredentials.properties");
			props.load(stream);
		} catch (IOException e) {
			e.printStackTrace();;
		}
		apiKey = props.getProperty("apiKey");
		FCM_SEND_URL = props.getProperty("FCMUrl");
		log.debug("==================Begin sending Notification================== ");

	}

	/**
	 * Push a notification to the provided input topic.
	 * 
	 * @param topic
	 * @throws Exception
	 */
	@Async
	public static CompletableFuture<FirebaseResponse> send(HttpEntity<FirebaseMessagingRequest> entity) {

		RestTemplate restTemplate = new RestTemplate();
		FirebaseResponse firebaseResponse = null;
		try {
			firebaseResponse = restTemplate.postForObject(FCM_SEND_URL, entity, FirebaseResponse.class);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return CompletableFuture.completedFuture(firebaseResponse);
	}

	private static HttpHeaders buildHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "key=" + apiKey);
		headers.add("Content-Type", "application/json");
		return headers;
	}

	public static ResponseEntity<String> sendNotification(String deviceToken,  String messageTitle, String messageBody) {
		FirebaseMessagingRequest req = buildMessagingRequest(deviceToken, messageBody, messageTitle);
		HttpHeaders headers = buildHeaders();
		HttpEntity<FirebaseMessagingRequest> request = new HttpEntity<FirebaseMessagingRequest>(req, headers);
		JSONObject obj = new JSONObject(request);
		log.info(obj.toString());
		try {
			CompletableFuture<FirebaseResponse> pushNotification = send(request);
			CompletableFuture.allOf(pushNotification).join();
			FirebaseResponse firebaseResponse = pushNotification.get();
			if (firebaseResponse != null) {
				if (firebaseResponse.getMessage_Id() != null) {
					log.info("Push notification successfully sent!");
				} else if (firebaseResponse.getError() != null) {
					log.error("Error sending push notifications: " + firebaseResponse.toString());
					log.error("Error message: {} ", firebaseResponse.getError());
				}
				return new ResponseEntity<>(firebaseResponse.toString(), HttpStatus.OK);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>("The push notification cannot be sent.", HttpStatus.BAD_REQUEST);
	}

	private static FirebaseMessagingRequest buildMessagingRequest(String deviceToken, String messageTitle, String messageBody) {
		FirebaseMessagingRequest req = new FirebaseMessagingRequest();
		FCMNotificationData data = buildNotificationData(messageTitle, messageBody);
		FCMNotification notification = buildNotification(messageTitle, messageBody);
		req.setTo(deviceToken);
		req.setNotification(notification);
		req.setData(data);
		req.setContent_available(true);
		req.setPriority(NOTIFICATION_PRIORITY);
		return req;
	}

	private static FCMNotificationData buildNotificationData(String messageTitle, String messageBody) {
		FCMNotificationData data = new FCMNotificationData();
		data.setTitle(messageTitle);
		data.setBody(messageBody);
		//data.setUrl("myurl");
		return data;
	}

	private static FCMNotification buildNotification(String messageTitle, String messageBody) {
		FCMNotification notify = new FCMNotification();
		notify.setTitle(messageTitle);
		notify.setBody(messageBody);
		return notify;
	}

	/**
	 * Represents a Firebase message request for a POST request
	 * 
	 * @author Dayna
	 *
	 */
	public static class FirebaseMessagingRequest {

		private String to; // Topic to send notification to
		private FCMNotificationData data;
		private FCMNotification notification;
		private Boolean content_available;
		private String priority;

		public FirebaseMessagingRequest() {
		}

		public String getTo() {
			return to;
		}

		public void setTo(String to) {
			this.to = to;
		}

		public FCMNotificationData getData() {
			return data;
		}

		public void setData(FCMNotificationData data) {
			this.data = data;
		}

		public FCMNotification getNotification() {
			return notification;
		}

		public void setNotification(FCMNotification notification) {
			this.notification = notification;
		}

		public Boolean getContent_available() {
			return content_available;
		}

		public void setContent_available(Boolean content_available) {
			this.content_available = content_available;
		}

		public String getPriority() {
			return priority;
		}

		public void setPriority(String priority) {
			this.priority = priority;
		}

	}

}

