package com.javamicroservice.productservice;

import com.javamicroservice.productservice.dto.ProductRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
//import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
//import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {


	// To access statically this MongoDB container and fetch
	// the URL of MongoDB URL
	// We need to set the URI of this container in our REST properties
	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

	@Autowired
	private MockMvc mockMvc;


	// Object Mapper will convert the POJO into JSON
	@Autowired
	private ObjectMapper objectMapper;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
		dynamicPropertyRegistry.add("spring.data.mongodb.uri",mongoDBContainer::getReplicaSetUrl);
	}

	// At the time of starting the integration test, first the TEST will start the MongoDB container by
	// downloading the mongoDB 4.4.2 image and after that starting the container, get
	// the replica set URL and add it to spring dta mongoDB URI dynamically at
	// the time of creating the test.

	@Test
	void shouldCreateProduct() throws Exception {

		ProductRequest productRequest = getProductRequest();
		String productRequestString = objectMapper.writeValueAsString(productRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
						.contentType(MediaType.APPLICATION_JSON)
						.content(productRequestString))
				.andExpect(status().isCreated());
	}

	private ProductRequest getProductRequest(){
		return ProductRequest.builder()
				.name("iphone15")
				.description("titanium colour")
				.price(BigDecimal.valueOf(1200))
				.build();
	}

}
