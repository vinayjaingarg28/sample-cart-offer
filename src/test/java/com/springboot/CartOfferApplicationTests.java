package com.springboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.controller.ApplyOfferRequest;
import com.springboot.controller.ApplyOfferResponse;
import com.springboot.controller.OfferRequest;

import com.springboot.controller.SegmentResponse;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class CartOfferApplicationTests {


	@Test
	public void checkFlatXForOneSegment() throws Exception {
		List<String> segments = new ArrayList<>();
		segments.add("p1");
		OfferRequest offerRequest = new OfferRequest(1,"FLATX",10,segments);
		boolean result = addOffer(offerRequest);
		Assertions.assertTrue(result);


		ApplyOfferRequest applyRequest = new ApplyOfferRequest(200, 1, 1);
		ApplyOfferResponse response = applyOffer(applyRequest);
		Assertions.assertEquals(190, response.getCart_value(), "Cart value should be updated to 190");


	}

	@Test
	public void checkFlatXForTwoSegment() throws Exception {
		List<String> segments = new ArrayList<>();
		segments.add("p2");
		OfferRequest offerRequest = new OfferRequest(1,"FLATX",15,segments);
		boolean result = addOffer(offerRequest);
		Assertions.assertTrue(result);


		ApplyOfferRequest applyRequest = new ApplyOfferRequest(200, 1, 2);
		ApplyOfferResponse response = applyOffer(applyRequest);
		Assertions.assertEquals(185, response.getCart_value(), "Cart value should be updated to 185");


	}

	@Test
	public void checkFlatXForThreeSegment() throws Exception {
		List<String> segments = new ArrayList<>();
		segments.add("p3");
		OfferRequest offerRequest = new OfferRequest(1,"FLATX",20,segments);
		boolean result = addOffer(offerRequest);
		Assertions.assertTrue(result);


		ApplyOfferRequest applyRequest = new ApplyOfferRequest(200, 1, 3);
		ApplyOfferResponse response = applyOffer(applyRequest);
		Assertions.assertEquals(180, response.getCart_value(), "Cart value should be updated to 180");


	}

	@Test
	public void checkPercentXForOneSegment() throws Exception {
		List<String> segments = new ArrayList<>();
		segments.add("p1");
		OfferRequest offerRequest = new OfferRequest(2,"Flat%",10,segments);
		boolean result = addOffer(offerRequest);
		Assertions.assertTrue(result);


		ApplyOfferRequest applyRequest = new ApplyOfferRequest(300, 2, 4);
		ApplyOfferResponse response = applyOffer(applyRequest);
		Assertions.assertEquals(270, response.getCart_value(), "Cart value should be updated to 270");


	}

	@Test
	public void checkPercentXForTwoSegment() throws Exception {
		List<String> segments = new ArrayList<>();
		segments.add("p2");
		OfferRequest offerRequest = new OfferRequest(2,"Flat%",15,segments);
		boolean result = addOffer(offerRequest);
		Assertions.assertTrue(result);


		ApplyOfferRequest applyRequest = new ApplyOfferRequest(100, 2, 2);
		ApplyOfferResponse response = applyOffer(applyRequest);
		Assertions.assertEquals(85, response.getCart_value(), "Cart value should be updated to 85");


	}

	@Test
	public void checkPercentXForThreeSegment() throws Exception {
		List<String> segments = new ArrayList<>();
		segments.add("p3");
		OfferRequest offerRequest = new OfferRequest(2,"Flat%",20,segments);
		boolean result = addOffer(offerRequest);
		Assertions.assertTrue(result);


		ApplyOfferRequest applyRequest = new ApplyOfferRequest(300, 2, 3);
		ApplyOfferResponse response = applyOffer(applyRequest);
		Assertions.assertEquals(240, response.getCart_value(), "Cart value should be updated to 240");


	}


	private ApplyOfferResponse applyOffer(ApplyOfferRequest applyOfferRequest) throws Exception {
		String urlString = "http://localhost:9001/api/v1/cart/apply_offer";
		URL url = new URL(urlString);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setDoOutput(true);
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestMethod("POST");

		ObjectMapper mapper = new ObjectMapper();
		String POST_PARAMS = mapper.writeValueAsString(applyOfferRequest);

		try (OutputStream os = con.getOutputStream()) {
			os.write(POST_PARAMS.getBytes());
			os.flush();
		}

		int responseCode = con.getResponseCode();
		System.out.println("Apply Offer Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) {
			try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
				String inputLine;
				StringBuilder response = new StringBuilder();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				System.out.println("Apply Offer Response: " + response);
				return mapper.readValue(response.toString(), ApplyOfferResponse.class);
			}
		} else {
			System.out.println("Request Failed");
			throw new RuntimeException("Failed Request " + responseCode);
		}
	}


	public boolean addOffer(OfferRequest offerRequest) throws Exception {
		String urlString = "http://localhost:9001/api/v1/offer";
		URL url = new URL(urlString);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setDoOutput(true);
		con.setRequestProperty("Content-Type", "application/json");

		ObjectMapper mapper = new ObjectMapper();

		String POST_PARAMS = mapper.writeValueAsString(offerRequest);
		OutputStream os = con.getOutputStream();
		os.write(POST_PARAMS.getBytes());
		os.flush();
		os.close();
		int responseCode = con.getResponseCode();
		System.out.println("POST Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) { //success
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			// print result
			System.out.println(response.toString());
		} else {
			System.out.println("POST request did not work.");
		}
		return true;
	}
}
