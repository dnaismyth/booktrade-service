package com.flow.booktrade.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.policy.Policy;
import com.amazonaws.auth.policy.Resource;
import com.amazonaws.auth.policy.Statement;
import com.amazonaws.auth.policy.Statement.Effect;
import com.amazonaws.auth.policy.actions.S3Actions;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.amazonaws.services.securitytoken.model.GetFederationTokenRequest;
import com.amazonaws.services.securitytoken.model.GetFederationTokenResult;
import com.flow.booktrade.service.util.S3Utils;

@Service
@Transactional
@PropertySource("classpath:/S3Credentials.properties")
public class S3TokenService {

	private static AmazonS3 s3client;
	
	private static AWSCredentials credentials;
	
	private static final String awsId;
		
	private static final String awsKey;
	
	private static final String s3User;
	 
	static {
		// Load credentials
		Properties props = new Properties();
		try {
			InputStream stream = S3TokenService.class.getResourceAsStream("/S3Credentials.properties");
			props.load(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		awsId = props.getProperty("AWS_ID");
		awsKey = props.getProperty("AWS_KEY");
		s3User = props.getProperty("S3_USER");
		AmazonS3 s3Client = new AmazonS3Client(getCredentials());
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		s3Client.setRegion(usWest2);
	}
	
	public static AWSCredentials getCredentials() {
   		return new BasicAWSCredentials(awsId, awsKey);
	}
	

	public BasicSessionCredentials getS3UserCredentials() {
		AWSSecurityTokenServiceClient stsClient = new AWSSecurityTokenServiceClient(getCredentials());

		GetFederationTokenRequest getFederationTokenRequest = new GetFederationTokenRequest();
		getFederationTokenRequest.setDurationSeconds(7200);
		getFederationTokenRequest.setName(s3User);

		// Define the policy and add to the request.
		Policy policy = new Policy();
		policy.withStatements(new Statement(Effect.Allow).withActions(
				S3Actions.PutObject)
				.withResources(new Resource("arn:aws:s3:::".concat(S3Utils.S3_BUCKET))));

		getFederationTokenRequest.setPolicy(policy.toJson());

		// Get the temporary security credentials.
		GetFederationTokenResult federationTokenResult = stsClient
				.getFederationToken(getFederationTokenRequest);
		Credentials sessionCredentials = federationTokenResult.getCredentials();

		// Package the session credentials as a BasicSessionCredentials
		// object for an S3 client object to use.
		BasicSessionCredentials basicSessionCredentials = new BasicSessionCredentials(
				sessionCredentials.getAccessKeyId(),
				sessionCredentials.getSecretAccessKey(),
				sessionCredentials.getSessionToken());
		
		return basicSessionCredentials;
	}
	
	
}
