package com.anusha.projects.springboot.votemanagement;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.anusha.projects.springboot.votemanagement.Vote;
import com.anusha.projects.springboot.votemanagement.VoteEvent;
import com.anusha.projects.springboot.votemanagement.VoteEventController;
import com.anusha.projects.springboot.votemanagement.VoteEventService;
import com.anusha.projects.springboot.votemanagement.VoteResult;

// TODO: Auto-generated Javadoc
/**
 * The Class VoteEventControllerTest.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = VoteEventController.class, secure = false)
public class VoteEventControllerTest {

	/** The mock mvc. */
	@Autowired
	private MockMvc mockMvc;

	/** The vote event service mock. */
	@MockBean
	private VoteEventService voteEventServiceMock;

	/** The vote events url. */
	private String voteEventsUrl = "/voteEvents";

	/** The active vote events url. */
	private String activeVoteEventsUrl = "/voteEvents?showActive=true";

	/** The vote events id url. */
	private String voteEventsIdUrl = "/voteEvents/1";

	/** The add vote to vote events url. */
	private String addVoteToVoteEventsUrl = "/voteEvents/1/vote";

	/** The votes results url. */
	private String votesResultsUrl = "/voteEvents/1/voteResults";

	/** The votes results split by age url. */
	private String votesResultsSplitByAgeUrl = "/voteEvents/1/voteResults?splitBy=age";

	/** The votes results split by gender url. */
	private String votesResultsSplitByGenderUrl = "/voteEvents/1/voteResults?splitBy=gender";

	/** The votes results split by locality url. */
	private String votesResultsSplitByLocalityUrl = "/voteEvents/1/voteResults?splitBy=locality";

	/** The expiry date 30 june. */
	private Date expiryDate30June;

	/** The mocked vote event. */
	private VoteEvent mockedVoteEvent;

	/**
	 * Sets the up.
	 *
	 * @throws ParseException the parse exception
	 */
	@Before
	public void setUp() throws ParseException {
		expiryDate30June = new SimpleDateFormat("yyyy-MM-dd").parse("2018-06-30");
		// Set up mocked data
		mockedVoteEvent = new VoteEvent("Mocked VoteEvent1", expiryDate30June,
				Stream.of("Yes", "No", "Maybe").collect(Collectors.toList()));
		mockedVoteEvent.setId(1);
		mockedVoteEvent.setListOfVotes(
				Stream.of(new Vote("Elsa", 19, "F", "Helsinki", "Yes"), new Vote("Hans", 24, "M", "Espo", "No"))
						.collect(Collectors.toList()));
		List<VoteEvent> listVoteEvents = Stream.of(mockedVoteEvent).collect(Collectors.toList());

		List<VoteResult> voteResultsList = Stream.of(new VoteResult(null, "Yes", 2), new VoteResult(null, "No", 2))
				.collect(Collectors.toList());

		List<VoteResult> voteResultsListSplit = Stream.of(new VoteResult("XX", "Yes", 2), new VoteResult("YY", "No", 1))
				.collect(Collectors.toList());

		// Define behaviors
		Mockito.when(voteEventServiceMock.getAllActiveVoteEvents()).thenReturn(listVoteEvents);
		Mockito.when(voteEventServiceMock.getAllVoteEvents()).thenReturn(listVoteEvents);
		Mockito.when(voteEventServiceMock.getVoteEventById(1)).thenReturn(mockedVoteEvent);
		Mockito.when(voteEventServiceMock.getVoteEventById(9999)).thenReturn(null);
		Mockito.when(voteEventServiceMock.createVoteEvent(Mockito.any(VoteEvent.class))).thenReturn(mockedVoteEvent);
		Mockito.when(voteEventServiceMock.updateVoteEvent(Mockito.any(VoteEvent.class), Mockito.any(VoteEvent.class)))
				.thenReturn(mockedVoteEvent);
		Mockito.when(voteEventServiceMock.addVoteToVoteEvent(Mockito.any(Vote.class), Mockito.any(VoteEvent.class)))
				.thenReturn(mockedVoteEvent);

		Mockito.when(voteEventServiceMock.getVotingResults(Mockito.any(VoteEvent.class))).thenReturn(voteResultsList);
		Mockito.when(voteEventServiceMock.splitVoteResults(Mockito.any(VoteEvent.class), Mockito.anyString()))
				.thenReturn(voteResultsListSplit);
	}

	/**
	 * Test get vote events.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetVoteEvents() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(voteEventsUrl).accept(MediaType.APPLICATION_JSON);

		// Invoke the method
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		String expected = "[{\"id\":1,\"name\":\"Mocked VoteEvent1\",\"expiryDate\":\"2018-06-29\",\"listOfOptions\":[\"Yes\",\"No\",\"Maybe\"]}]";
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
	}

	/**
	 * Test get vote events active.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetVoteEventsActive() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(activeVoteEventsUrl)
				.accept(MediaType.APPLICATION_JSON);

		// Invoke the method
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		String expected = "[{\"id\":1,\"name\":\"Mocked VoteEvent1\",\"expiryDate\":\"2018-06-29\",\"listOfOptions\":[\"Yes\",\"No\",\"Maybe\"]}]";
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
	}

	/**
	 * Test get vote events invalid param.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetVoteEventsInvalidParam() throws Exception {
		String invalidParamUrl = "/voteEvents?showActive=any";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(invalidParamUrl).accept(MediaType.APPLICATION_JSON);

		// Invoke the method
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		// assert the exception.
		assertEquals(HttpStatus.FORBIDDEN.value(), result.getResponse().getStatus());
	}

	/**
	 * Test get vote events by id.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetVoteEventsById() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(voteEventsIdUrl).accept(MediaType.APPLICATION_JSON);

		// Invoke the method
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		String expected = "{\"id\":1,\"name\":\"Mocked VoteEvent1\",\"expiryDate\":\"2018-06-29\",\"listOfOptions\":[\"Yes\",\"No\",\"Maybe\"]}";
		// Assert response JSON
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
	}

	/**
	 * Test get vote events by id not found.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetVoteEventsByIdNotFound() throws Exception {
		// Define Mocks
		Mockito.when(voteEventServiceMock.getVoteEventById(1)).thenReturn(null);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(voteEventsIdUrl).accept(MediaType.APPLICATION_JSON);

		// Invoke the method
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		// assert the exception.
		assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
	}

	/**
	 * Test create vote event.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testCreateVoteEvent() throws Exception {
		// pass in any JSON
		String jsonString = "{\"name\":\"Mocked VoteEvent1\",\"expiryDate\":\"2018-06-29\",\"listOfOptions\":[\"Yes\",\"No\",\"Maybe\"]}";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(voteEventsUrl).accept(MediaType.APPLICATION_JSON)
				.content(jsonString).contentType(MediaType.APPLICATION_JSON);

		// Invoke the Method
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		String expected = "{\"id\":1,\"name\":\"Mocked VoteEvent1\",\"expiryDate\":\"2018-06-29\",\"listOfOptions\":[\"Yes\",\"No\",\"Maybe\"]}";
		// Assert Response JSON
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	/**
	 * Test create vote event with missing fields.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testCreateVoteEventWithMissingFields() throws Exception {
		// pass in any JSON without required fields.
		String exampleJson = "{\"name\":\"testname\"}";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(voteEventsUrl).accept(MediaType.APPLICATION_JSON)
				.content(exampleJson).contentType(MediaType.APPLICATION_JSON);

		// Invoke the Method
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		//assert for exception since mandatory fields are missing.
		assertEquals(HttpStatus.FORBIDDEN.value(), result.getResponse().getStatus());
	}
	
	/**
	 * Test edit vote event.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testEditVoteEvent() throws Exception {
		// pass in any JSON
		String jsonString = "{\"name\":\"Mocked VoteEvent1\",\"expiryDate\":\"2018-06-29\",\"listOfOptions\":[\"Yes\",\"No\",\"Maybe\"]}";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put(voteEventsIdUrl).accept(MediaType.APPLICATION_JSON)
				.content(jsonString).contentType(MediaType.APPLICATION_JSON);

		// Invoke the Method
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		String expected = "{\"id\":1,\"name\":\"Mocked VoteEvent1\",\"expiryDate\":\"2018-06-29\",\"listOfOptions\":[\"Yes\",\"No\",\"Maybe\"]}";
		// Assert Response JSON
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}
	
	@Test
	public void testEditVoteEventWithInvalidId() throws Exception {
		String voteEventsIdUrl = "/voteEvents/9999";
		// pass in any JSON
		String exampleJson = "{\"name\":\"testname\"}";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put(voteEventsIdUrl).accept(MediaType.APPLICATION_JSON)
				.content(exampleJson).contentType(MediaType.APPLICATION_JSON);

		// Invoke the Method
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		//assert for exception since the Id is not found in DB.
		assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
	}
	/**
	 * Test edit vote event with missing fields.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testEditVoteEventWithMissingFields() throws Exception {
		// pass in any JSON
		String exampleJson = "{\"name\":\"testname\"}";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put(voteEventsIdUrl).accept(MediaType.APPLICATION_JSON)
				.content(exampleJson).contentType(MediaType.APPLICATION_JSON);

		// Invoke the Method
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		//assert for exception since mandatory fields are missing.
		assertEquals(HttpStatus.FORBIDDEN.value(), result.getResponse().getStatus());
	}

	/**
	 * Test add vote to event.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testAddVoteToEvent() throws Exception {
		// pass in any JSON
		String exampleJson = "{\"name\":\"testname\" , \"age\":\"19\" ,\"gender\":\"F\",\"locality\":\"Helsinki\",\"votingOption\":\"Yes\" }";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(addVoteToVoteEventsUrl)
				.accept(MediaType.APPLICATION_JSON).content(exampleJson).contentType(MediaType.APPLICATION_JSON);

		// Invoke the Method
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();
		// assert Status as OK
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}
	
	
	@Test
	public void testAddVoteToEventWithInvalidEventId() throws Exception {
		String votesResultsUrl = "/voteEvents/9999/vote";
		// pass in any JSON
		String exampleJson = "{\"name\":\"testname\"}";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(votesResultsUrl)
				.accept(MediaType.APPLICATION_JSON).content(exampleJson).contentType(MediaType.APPLICATION_JSON);

		// Invoke the Method
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();
		// assert Status as OK
		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());

	}
	/**
	 * Test add vote to event with missing fields.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testAddVoteToEventWithMissingFields() throws Exception {
		// pass in any JSON
		String exampleJson = "{\"name\":\"testname\"}";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(addVoteToVoteEventsUrl)
				.accept(MediaType.APPLICATION_JSON).content(exampleJson).contentType(MediaType.APPLICATION_JSON);

		// Invoke the Method
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();
		// assert Status as OK
		assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());

	}

	/**
	 * Test add vote to expired.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testAddVoteToExpired() throws Exception {
		Mockito.when(voteEventServiceMock.checkIfVoteEventIsExpired(Mockito.any(VoteEvent.class))).thenReturn(true);

		// pass in any JSON
		String exampleJson = "{\"name\":\"testname\" , \"age\":\"19\" ,\"gender\":\"F\",\"locality\":\"Helsinki\",\"votingOption\":\"Yes\" }";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(addVoteToVoteEventsUrl)
				.accept(MediaType.APPLICATION_JSON).content(exampleJson).contentType(MediaType.APPLICATION_JSON);

		// Invoke the Method
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();
		// assert if the operation is forbidden
		assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
	}

	/**
	 * Test add vote to event with duplicate vote.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testAddVoteToEventWithDuplicateVote() throws Exception {
		Mockito.when(voteEventServiceMock.checkIfVoteAlreadyExists(Mockito.any(Vote.class), Mockito.any(VoteEvent.class)))
				.thenReturn(true);

		// pass in any JSON
		String exampleJson = "{\"name\":\"testname\" , \"age\":\"19\" ,\"gender\":\"F\",\"locality\":\"Helsinki\",\"votingOption\":\"Yes\" }";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(addVoteToVoteEventsUrl)
				.accept(MediaType.APPLICATION_JSON).content(exampleJson).contentType(MediaType.APPLICATION_JSON);

		// Invoke the Method
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();
		// assert if the operation is forbidden
		assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
	}

	/**
	 * Test get vote results.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetVoteResults() throws Exception {
		// Set it to true forcefully
		Mockito.when(voteEventServiceMock.checkIfVoteEventIsExpired(Mockito.any(VoteEvent.class))).thenReturn(true);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(votesResultsUrl).accept(MediaType.APPLICATION_JSON);

		// Invoke the method
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		String expected = "[{\"splitField\":null,\"votingOption\":\"Yes\",\"count\":2},{\"splitField\":null,\"votingOption\":\"No\",\"count\":2}]";
		System.out.println(result.getResponse().getContentAsString());
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
	}

	/**
	 * Test get vote results split by.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetVoteResultsSplitBy() throws Exception {
		// Set it to true forcefully
		Mockito.when(voteEventServiceMock.checkIfVoteEventIsExpired(Mockito.any(VoteEvent.class))).thenReturn(true);

		// Split by Age
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(votesResultsSplitByAgeUrl)
				.accept(MediaType.APPLICATION_JSON);
		// Invoke the method
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		String expected = "[{\"splitField\":\"XX\",\"votingOption\":\"Yes\",\"count\":2},{\"splitField\":\"YY\",\"votingOption\":\"No\",\"count\":1}]";
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

		// Split by Gender
		requestBuilder = MockMvcRequestBuilders.get(votesResultsSplitByGenderUrl).accept(MediaType.APPLICATION_JSON);

		// Invoke the method
		result = mockMvc.perform(requestBuilder).andReturn();

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

		// Split by Locality
		requestBuilder = MockMvcRequestBuilders.get(votesResultsSplitByLocalityUrl).accept(MediaType.APPLICATION_JSON);

		// Invoke the method
		result = mockMvc.perform(requestBuilder).andReturn();

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), true);
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
	}

	/**
	 * Test get vote results split by name.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetVoteResultsSplitByName() throws Exception {
		// Set it to true forcefully
		Mockito.when(voteEventServiceMock.checkIfVoteEventIsExpired(Mockito.any(VoteEvent.class))).thenReturn(true);

		String votesResultsSplitByNameUrl = "/voteEvents/1/voteResults?splitBy=name";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(votesResultsSplitByNameUrl)
				.accept(MediaType.APPLICATION_JSON);
		// Invoke the method
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		// assert if the operation is forbidden. Only Age / Gender / Locality is
		// permitted.
		assertEquals(HttpStatus.FORBIDDEN.value(), result.getResponse().getStatus());
	}

	/**
	 * Test get vote results for ongoing vote event.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetVoteResultsForOngoingVoteEvent() throws Exception {
		Mockito.when(voteEventServiceMock.checkIfVoteEventIsExpired(Mockito.any(VoteEvent.class))).thenReturn(false);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(votesResultsUrl).accept(MediaType.APPLICATION_JSON);

		// Invoke the method
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		// assert if the operation is forbidden
		assertEquals(HttpStatus.FORBIDDEN.value(), result.getResponse().getStatus());
	}
	
	/**
	 * Test get vote results for invalid vote eventd.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetVoteResultsForInvalidVoteEventd() throws Exception {
		String votesResultsUrl = "/voteEvents/9999/voteResults";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(votesResultsUrl).accept(MediaType.APPLICATION_JSON);

		// Invoke the method
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		// assert if the operation is forbidden
		assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
	}
}
