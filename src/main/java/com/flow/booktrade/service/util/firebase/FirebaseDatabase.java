package com.flow.booktrade.service.util.firebase;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestTemplate;

import com.flow.booktrade.domain.RComment;
import com.flow.booktrade.domain.RConversation;

public class FirebaseDatabase {
	private static final Logger log = LoggerFactory.getLogger(FirebaseDatabase.class);

	
	private static String databaseEndpoint;
	private static String databaseSecret;

	static {
		log.debug("==================Initializing Database Endpoint================== ");
		Properties props = new Properties();
		try {
			InputStream stream = FirebaseDatabase.class.getResourceAsStream("/FCMCredentials.properties");
			props.load(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        // ip address of the service URL(like.23.28.244.244)
	    //HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> hostname.equals("127.0.0.1"));
		databaseEndpoint = props.getProperty("FCMDatabaseUrl");
		databaseSecret = props.getProperty("databaseSecret");
		log.debug("The endpoint is: {}", databaseEndpoint);
		log.debug("==================Begin Updating Data================== ");

	}
	
	/**
	 * Update Model
	 * @param entity
	 * @param url
	 * @return
	 */
	@Async
	public static CompletableFuture<FirebaseUserModel> requestModelUpdate(HttpEntity<?> entity, String url) {
		log.debug("Built url is:{}", url);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<FirebaseUserModel> updated = null;
		try {
			updated = restTemplate.exchange(url, HttpMethod.PUT, entity, FirebaseUserModel.class);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return CompletableFuture.completedFuture(updated.getBody());
	}
	
	@Async
	public static CompletableFuture<FirebaseConversationModel> requestConversationModelUpdate(HttpEntity<?> entity, String url){
		log.debug("Built url is:{}", url);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<FirebaseConversationModel> updated = null;
		try {
			updated = restTemplate.exchange(url, HttpMethod.PUT, entity, FirebaseConversationModel.class);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return CompletableFuture.completedFuture(updated.getBody());
	}
	
	private static String buildUserEndpoint(Long userId){
		return databaseEndpoint.concat("users/").concat(userId.toString()).concat(".json")
				.concat("?auth=".concat(databaseSecret));
	}
	
	private static String buildConversationEndpoint(Long conversationId){
		return databaseEndpoint.concat("conversations/").concat(conversationId.toString()).concat(".json")
				.concat("?auth=".concat(databaseSecret));
	}

	/**
	 * Update the train crossing status to active/not active
	 * @param trainCrossingId
	 * @param isActive
	 * @return
	 */
	public static ResponseEntity<String> updateUserProperties(Long userId, String name, String email, String avatar){
		String url = buildUserEndpoint(userId);
		HttpHeaders headers = buildHeaders();
		FirebaseUserModel updateModel = buildUserModel(name, email, avatar);
		HttpEntity<FirebaseUserModel> request = new HttpEntity<FirebaseUserModel>(updateModel, headers);
		try {
			CompletableFuture<FirebaseUserModel> userUpdate = requestModelUpdate(request, url);
			CompletableFuture.allOf(userUpdate).join();
			FirebaseUserModel response = userUpdate.get();
			if(response.getName().equals(name)){
				log.info("Successfully updated user: {}, {}, {}", name, email, avatar);
			} else {
				log.error("Error updating user.");
			}
			return new ResponseEntity<>(userUpdate.toString(), HttpStatus.OK);
		} catch (InterruptedException e){
			e.printStackTrace();
		} catch (ExecutionException e){
			e.printStackTrace();
		}
		
		return new ResponseEntity<>("Update could not be processed.", HttpStatus.BAD_REQUEST);
	}
	
	public static ResponseEntity<String> createConversation(RConversation conversation){
		String url = buildConversationEndpoint(conversation.getId());
		HttpHeaders headers = buildHeaders();
		FirebaseConversationModel model = buildConversationModel(conversation);
		HttpEntity<FirebaseConversationModel> request = new HttpEntity<FirebaseConversationModel>(model, headers);
		try {
			CompletableFuture<FirebaseConversationModel> convoUpdate = requestConversationModelUpdate(request, url);
			CompletableFuture.allOf(convoUpdate).join();
			FirebaseConversationModel response = convoUpdate.get();
			if(response != null){
				log.info("Conversation updated!");
			} else {
				log.error("Error updating conversation.");
			}
			return new ResponseEntity<>(convoUpdate.toString(), HttpStatus.OK);
		} catch (InterruptedException e){
			e.printStackTrace();
		} catch (ExecutionException e){
			e.printStackTrace();
		}
		
		return new ResponseEntity<>("Update could not be processed.", HttpStatus.BAD_REQUEST);

	}
	
	private static HttpHeaders buildHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		return headers;
	}
	
	private static FirebaseUserModel buildUserModel(String name, String email, String avatar){
		FirebaseUserModel model = new FirebaseUserModel();
		model.setName(name);
		model.setEmail(email);
		if(avatar != null){
			model.setAvatar(avatar);
		}
		return model;
	}
	
	private static FirebaseConversationModel buildConversationModel(RConversation conversation){
		FirebaseConversationModel fcm = new FirebaseConversationModel();
		fcm.setFrom_id(conversation.getInitiator().getId().toString());
		fcm.setTo_id(conversation.getRecipient().getId().toString());
		for(RComment c : conversation.getComments()){
			FirebaseMessageModel fmm = new FirebaseMessageModel();
			fmm.setSent_date(c.getCreatedDate().toString());
			fmm.setText(c.getText());
			fcm.getMessages().add(fmm);
		}
		
		return fcm;
	}
	
	public static class FirebaseConversationModel {
		
		private String to_id;
		private String from_id;
		private List<FirebaseMessageModel> messages = new ArrayList<FirebaseMessageModel>();
		
		public FirebaseConversationModel(){}
		
		public String getTo_id(){
			return to_id;
		}
		
		public void setTo_id(String to_id){
			this.to_id = to_id;
		}
		
		public String getFrom_id(){
			return from_id;
		}
		
		public void setFrom_id(String from_id){
			this.from_id = from_id;
		}
		
		public List<FirebaseMessageModel> getMessages(){
			return messages;
		}
		
		public void setMessages(List<FirebaseMessageModel> messages){
			this.messages = messages;
		}
	}
	
	public static class FirebaseMessageModel {
		private String text;
		private String sent_date;
		
		public FirebaseMessageModel(){}
		
		public String getText(){
			return text;
		}
		
		public void setText(String text){
			this.text = text;
		}
		
		public String getSent_date(){
			return sent_date;
		}
		
		public void setSent_date(String sent_date){
			this.sent_date = sent_date;
		}
		
	}
	
	
	/**
	 * Class to represent the firebase train crossing model for
	 * Real time data.
	 * @author Dayna
	 *
	 */
	public static class FirebaseUserModel{
		
		private String name;
		private String email;
		private String avatar;
		
		public FirebaseUserModel(){}
		
		public String getName(){
			return name;
		}
		
		public void setName(String name){
			this.name = name;
		}
		
		public String getEmail(){
			return email;
		}
		
		public void setEmail(String email){
			this.email = email;
		}
		
		public String getAvatar(){
			return avatar;
		}
		
		public void setAvatar(String avatar){
			this.avatar = avatar;
		}
	}
	
}



