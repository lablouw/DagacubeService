package com.dagacube;

import com.dagacube.api.v1.model.WagerWinRequest;
import com.dagacube.domain.repository.entity.Player;
import com.google.gson.Gson;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DagacubeControllerV1Test {

	private static final String CONTEXT_PATH = "/v1/dagacubeService";
	private static final String URI_BALANCE = CONTEXT_PATH+"/{playerId}/balance";
	private static final String URI_WAGER = CONTEXT_PATH+"/wager";
	private static final String URI_WIN = CONTEXT_PATH+"/win";

	@Autowired
	private MockMvc mvc;

	private final Gson gson = new Gson();

	@Test
	public void test_playerCreation() throws Exception {
		String username = RandomStringUtils.randomAlphanumeric(10);
		mvc.perform(MockMvcRequestBuilders
						.post(CONTEXT_PATH)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(gson.toJson(Player.builder().username(username).balance(BigDecimal.TEN).build()))
						.accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isCreated());

		mvc.perform(MockMvcRequestBuilders
						.post(CONTEXT_PATH)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(gson.toJson(Player.builder().username(username).balance(BigDecimal.TEN).build()))
						.accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isConflict());
	}

	@Test
	public void test_balance() throws Exception {
		//Create player
		String username = RandomStringUtils.randomAlphanumeric(10);
		ResultActions resultActions = mvc.perform(MockMvcRequestBuilders
						.post(CONTEXT_PATH)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(gson.toJson(Player.builder().username(username).balance(BigDecimal.TEN).build()))
						.accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isCreated());
		long playerId = Long.parseLong(resultActions.andReturn().getResponse().getContentAsString());

		resultActions = mvc.perform(get(URI_BALANCE, playerId)).andExpect(status().isOk());
		assertEquals(0, BigDecimal.TEN.compareTo(new BigDecimal(resultActions.andReturn().getResponse().getContentAsString())));
	}

	@Test
	public void test_win() throws Exception {
		//Create player
		String username = RandomStringUtils.randomAlphanumeric(10);
		ResultActions resultActions = mvc.perform(MockMvcRequestBuilders
						.post(CONTEXT_PATH)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(gson.toJson(Player.builder().username(username).balance(BigDecimal.TEN).build()))
						.accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isCreated());
		long playerId = Long.parseLong(resultActions.andReturn().getResponse().getContentAsString());

		BigDecimal amount = new BigDecimal(new Random().nextInt(10));
		String txnId = UUID.randomUUID().toString();

		//Test first win
		WagerWinRequest wagerWinRequest = WagerWinRequest.builder().playerId(playerId).amount(amount).build();
		ResultActions winResultActions1 = mvc.perform(MockMvcRequestBuilders
						.post(URI_WIN)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(gson.toJson(wagerWinRequest))
						.header("transactionId", txnId))
				.andExpect(status().isOk());

		String winSystemId1 = winResultActions1.andReturn().getResponse().getContentAsString();
		ResultActions balanceResultActions1 = mvc.perform(get(URI_BALANCE, playerId)).andExpect(status().isOk());
		BigDecimal balance1 = new BigDecimal(balanceResultActions1.andReturn().getResponse().getContentAsString());
		assertEquals(0, BigDecimal.TEN.add(amount).compareTo(balance1));

		//Test second win to be idempotent
		ResultActions winResultActions2 = mvc.perform(MockMvcRequestBuilders
						.post(URI_WIN)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(gson.toJson(wagerWinRequest))
						.header("transactionId", txnId))
				.andExpect(status().isOk());

		String winSystemId2 = winResultActions2.andReturn().getResponse().getContentAsString();
		ResultActions balanceResultActions2 = mvc.perform(get(URI_BALANCE, playerId)).andExpect(status().isOk());
		BigDecimal balance2 = new BigDecimal(balanceResultActions2.andReturn().getResponse().getContentAsString());
		assertEquals(0, balance2.compareTo(balance1));

		assertEquals(winSystemId1, winSystemId2);
	}

	@Test
	public void test_wager() throws Exception {
		//Create player
		String username = RandomStringUtils.randomAlphanumeric(10);
		ResultActions resultActions = mvc.perform(MockMvcRequestBuilders
						.post(CONTEXT_PATH)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(gson.toJson(Player.builder().username(username).balance(BigDecimal.TEN).build()))
						.accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isCreated());
		long playerId = Long.parseLong(resultActions.andReturn().getResponse().getContentAsString());

		BigDecimal amount = new BigDecimal(new Random().nextInt(10));
		String txnId = UUID.randomUUID().toString();

		//Test first wager
		WagerWinRequest wagerWinRequest = WagerWinRequest.builder().playerId(playerId).amount(amount).build();
		ResultActions wagerResultActions1 = mvc.perform(MockMvcRequestBuilders
						.post(URI_WAGER)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(gson.toJson(wagerWinRequest))
						.header("transactionId", txnId))
				.andExpect(status().isOk());

		String wagerSystemId1 = wagerResultActions1.andReturn().getResponse().getContentAsString();
		ResultActions balanceResultActions1 = mvc.perform(get(URI_BALANCE, playerId)).andExpect(status().isOk());
		BigDecimal balance1 = new BigDecimal(balanceResultActions1.andReturn().getResponse().getContentAsString());
		assertEquals(0, BigDecimal.TEN.subtract(amount).compareTo(balance1));

		//Test second wager to be idempotent
		ResultActions wagerResultActions2 = mvc.perform(MockMvcRequestBuilders
						.post(URI_WAGER)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(gson.toJson(wagerWinRequest))
						.header("transactionId", txnId))
				.andExpect(status().isOk());

		String wagerSystemId2 = wagerResultActions2.andReturn().getResponse().getContentAsString();
		ResultActions balanceResultActions2 = mvc.perform(get(URI_BALANCE, playerId)).andExpect(status().isOk());
		BigDecimal balance2 = new BigDecimal(balanceResultActions2.andReturn().getResponse().getContentAsString());
		assertEquals(0, balance2.compareTo(balance1));

		assertEquals(wagerSystemId1, wagerSystemId2);
	}

	@Test
	public void test_outOfFunds() throws Exception {
		//Create player
		String username = RandomStringUtils.randomAlphanumeric(10);
		ResultActions resultActions = mvc.perform(MockMvcRequestBuilders
						.post(CONTEXT_PATH)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(gson.toJson(Player.builder().username(username).balance(BigDecimal.TEN).build()))
						.accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isCreated());
		long playerId = Long.parseLong(resultActions.andReturn().getResponse().getContentAsString());

		String txnId = UUID.randomUUID().toString();
		WagerWinRequest wagerWinRequest = WagerWinRequest.builder().playerId(playerId).amount(new BigDecimal(11)).build();

		ResultActions wagerResultActions = mvc.perform(MockMvcRequestBuilders
						.post(URI_WAGER)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(gson.toJson(wagerWinRequest))
						.header("transactionId", txnId))
				.andExpect(status().isIAmATeapot());

	}


}
