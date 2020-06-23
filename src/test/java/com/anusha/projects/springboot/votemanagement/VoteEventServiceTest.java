package com.anusha.projects.springboot.votemanagement;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.anusha.projects.springboot.votemanagement.Vote;
import com.anusha.projects.springboot.votemanagement.VoteEvent;
import com.anusha.projects.springboot.votemanagement.VoteEventRepository;
import com.anusha.projects.springboot.votemanagement.VoteEventService;
import com.anusha.projects.springboot.votemanagement.VoteRepository;

/**
 * The Class VoteEventServiceTest.
 */
@RunWith(SpringRunner.class)
public class VoteEventServiceTest {

	/**
	 * The Class VoteEventServiceTestContextConfiguration.
	 */
	@TestConfiguration
	static class VoteEventServiceTestContextConfiguration {

		/**
		 * Todo management service.
		 *
		 * @return the todo management service
		 */
		@Bean
		public VoteEventService voteEventService() {
			return new VoteEventService();
		}
	}

	/** The vote event service. */
	@Autowired
	private VoteEventService voteEventService;

	/** The vote event repository. */
	@MockBean
	private VoteEventRepository voteEventRepository;

	/** The vote repository. */
	@MockBean
	private VoteRepository voteRepository;

	/** The expiry date 20 april. */
	Date expiryDate20April;

	Date expiryDateFuture;

	VoteEvent mockedVoteEvent;

	List<VoteEvent> listVoteEvents;

	/**
	 * Sets the up.
	 *
	 * @throws ParseException the parse exception
	 */
	@Before
	public void setUp() throws ParseException {
		expiryDate20April = new SimpleDateFormat("yyyy-MM-dd").parse("2018-04-20");
		expiryDateFuture = new SimpleDateFormat("yyyy-MM-dd").parse("9999-12-31");
		// Set up mocked data
		mockedVoteEvent = new VoteEvent("Mocked VoteEvent1", expiryDate20April,
				Stream.of("Yes", "No", "Maybe").collect(Collectors.toList()));

		mockedVoteEvent.setListOfVotes(
				Stream.of(new Vote("Elsa", 19, "F", "Helsinki", "Yes"), new Vote("Hans", 24, "M", "Espoo", "No"))
						.collect(Collectors.toList()));
		listVoteEvents = Stream.of(mockedVoteEvent).collect(Collectors.toList());
		// Define behaviors
		Mockito.when(voteEventRepository.save(Mockito.any(VoteEvent.class))).thenReturn(mockedVoteEvent);
		Mockito.when(voteEventRepository.findById(1)).thenReturn(mockedVoteEvent);
		Mockito.when(voteEventRepository.findByExpiryDate(Mockito.any(Date.class))).thenReturn(listVoteEvents);
		Mockito.when(voteEventRepository.findAll()).thenReturn(listVoteEvents);
	}

	/**
	 * Test get all vote events.
	 */
	@Test
	public void testGetAllVoteEvents() {
		assertThat(voteEventService.getAllVoteEvents().get(0).getName()).isEqualTo("Mocked VoteEvent1");
	}


	@Test
	public void getAllActiveVoteEventsWithNoEmptyList() {
		// define behavior
		Mockito.when(voteEventRepository.findAll()).thenReturn(null);
		// assert
		assertThat(voteEventService.getAllActiveVoteEvents().size()).isEqualTo(0);
	}

	/**
	 * Test get vote event by id.
	 */
	@Test
	public void testGetVoteEventById() {
		assertThat(voteEventService.getVoteEventById(1).getName()).isEqualTo("Mocked VoteEvent1");
	}

	/**
	 * Test create vote event.
	 */
	@Test
	public void testCreateVoteEvent() {
		// invoke the method and Check name
		assertThat(voteEventService.createVoteEvent(new VoteEvent()).getName()).isEqualTo("Mocked VoteEvent1");
	}

	/**
	 * Test update vote event.
	 */
	@Test
	public void testUpdateVoteEvent() {
		// Invoke the method and Check if the list is intact as defined in the mock
		// setup
		assertThat(voteEventService.updateVoteEvent(new VoteEvent(), mockedVoteEvent).getListOfVotes().size()).isEqualTo(2);
	}

	/**
	 * Test add vote to vote event.
	 */
	@Test
	public void testAddVoteToVoteEvent() {
		//// Invoke the method and Check if the list size is 3 now. Also check the name
		//// for the newly added Vote.
		Vote vote = new Vote("Olaf", 14, "M", "Vantaa", "Yes");
		assertThat(voteEventService.addVoteToVoteEvent(vote, mockedVoteEvent).getListOfVotes().size()).isEqualTo(3);
		// assertThat(voteEventService.addVoteToVoteEvent(vote,
		// 1).getListOfVotes().get(2).getName()).isEqualTo("Olaf");
	}

	/**
	 * Test check if vote event is expired.
	 *
	 * @throws ParseException the parse exception
	 */
	@Test
	public void testCheckIfVoteEventIsExpired() throws ParseException {
		assertThat(voteEventService.checkIfVoteEventIsExpired(mockedVoteEvent)).isEqualTo(true);

		// Check for future date by setting up mocks
		// Set up mocked data
		VoteEvent mockedVoteEventWithFutureDate = new VoteEvent("Mocked VoteEvent1", expiryDateFuture,
				Stream.of("Yes", "No", "Maybe").collect(Collectors.toList()));

		// check for false
		assertThat(voteEventService.checkIfVoteEventIsExpired(mockedVoteEventWithFutureDate)).isEqualTo(false);
	}

	/**
	 * Test check if vote already exists.
	 */
	@Test
	public void testCheckIfVoteAlreadyExists() {
		// Exact Match of Name, age, Gender and Locality. Should return true.
		assertThat(voteEventService.checkIfVoteAlreadyExists(new Vote("Elsa", 19, "F", "Helsinki", "Yes"), mockedVoteEvent))
				.isEqualTo(true);

		// Partial Match With Name , Gender and Locality. Should return false.
		assertThat(voteEventService.checkIfVoteAlreadyExists(new Vote("Elsa", 21, "F", "Helsinki", "Yes"), mockedVoteEvent))
				.isEqualTo(false);

		// Partial Match With Name and Gender.Should return false.
		assertThat(voteEventService.checkIfVoteAlreadyExists(new Vote("Hans", 26, "M", "Oulu", "No"), mockedVoteEvent))
				.isEqualTo(false);

		// Total mismatch . Should return false.
		assertThat(voteEventService.checkIfVoteAlreadyExists(new Vote("Sven", 26, "M", "Turku", "No"), mockedVoteEvent))
				.isEqualTo(false);
	}

	/**
	 * Test group by.
	 */
	@Test
	public void testGroupBy() {
		List<Vote> listVotes = Stream.of(new Vote("Elsa", 19, "F", "Helsinki", "Yes"),
				new Vote("Hans", 24, "M", "Espoo", "No"), new Vote("Sven", 18, "M", "Espoo", "No"),
				new Vote("Anna", 11, "F", "Espoo", "Yes"), new Vote("Olaf", 11, "M", "Vantaa", "Yes"))
				.collect(Collectors.toList());

		// group by and count - finalized
		Map<String, Long> counting = listVotes.stream()
				.collect(Collectors.groupingBy(Vote::getVotingOption, Collectors.counting()));

		System.out.println(counting);

		Function<Vote, List<Object>> ageAndOptionKey = vote -> Arrays.<Object>asList(vote.getAge(),
				vote.getVotingOption());

		Map<List<Object>, Long> map = listVotes.stream()
				.collect(Collectors.groupingBy(ageAndOptionKey, Collectors.counting()));

		System.out.println(map);

		Function<Vote, List<Object>> genderAndOptionKey = vote -> Arrays.<Object>asList(vote.getGender(),
				vote.getVotingOption());

		map = listVotes.stream().collect(Collectors.groupingBy(genderAndOptionKey, Collectors.counting()));

		System.out.println(map);

		Function<Vote, List<Object>> genderAndLocalityKey = vote -> Arrays.<Object>asList(vote.getLocality(),
				vote.getVotingOption());

		map = listVotes.stream().collect(Collectors.groupingBy(genderAndLocalityKey, Collectors.counting()));

		System.out.println(map);
	}

	/**
	 * Test get voting results.
	 */
	@Test
	public void testGetVotingResults() {
		// Set up mocked data
		VoteEvent mockedVoteEvent = new VoteEvent("Mocked VoteEvent1", expiryDate20April,
				Stream.of("Yes", "No", "Maybe").collect(Collectors.toList()));

		mockedVoteEvent.setListOfVotes(
				Stream.of(new Vote("Elsa", 19, "F", "Helsinki", "Yes"), new Vote("Hans", 24, "M", "Espoo", "No"),
						new Vote("Sven", 18, "M", "Espoo", "No"), new Vote("Anna", 11, "F", "Espoo", "Yes"),
						new Vote("Olaf", 11, "M", "Vantaa", "Yes")).collect(Collectors.toList()));
		// Invoke method and do assertions .
		// Grouping based on the list above. {No=2, Yes=3}
		assertThat(voteEventService.getVotingResults(mockedVoteEvent).size()).isEqualTo(2);

		assertThat(voteEventService.getVotingResults(mockedVoteEvent).get(0).getSplitField()).isNull();
		assertThat(voteEventService.getVotingResults(mockedVoteEvent).get(0).getVotingOption()).isEqualTo("No");
		assertThat(voteEventService.getVotingResults(mockedVoteEvent).get(0).getCount()).isEqualTo(2);

		assertThat(voteEventService.getVotingResults(mockedVoteEvent).get(1).getSplitField()).isNull();
		assertThat(voteEventService.getVotingResults(mockedVoteEvent).get(1).getVotingOption()).isEqualTo("Yes");
		assertThat(voteEventService.getVotingResults(mockedVoteEvent).get(1).getCount()).isEqualTo(3);

	}

	/**
	 * Test get vote split results.
	 */
	@Test
	public void testGetVoteSplitResults() {
		// Set up mocked data
		VoteEvent mockedVoteEvent = new VoteEvent("Mocked VoteEvent1", expiryDate20April,
				Stream.of("Yes", "No", "Maybe").collect(Collectors.toList()));

		mockedVoteEvent.setListOfVotes(
				Stream.of(new Vote("Elsa", 19, "F", "Helsinki", "Yes"), new Vote("Hans", 24, "M", "Espoo", "No"),
						new Vote("Sven", 18, "M", "Espoo", "No"), new Vote("Anna", 11, "F", "Espoo", "Yes"),
						new Vote("Olaf", 11, "M", "Vantaa", "Yes")).collect(Collectors.toList()));
		// Split based on Age and do assertions .
		// grouping by age based on the list above. {[18, No]=1, [19, Yes]=1, [24,
		// No]=1, [11, Yes]=2}
		assertThat(voteEventService.splitVoteResults(mockedVoteEvent, "age").size()).isEqualTo(4);

		assertThat(voteEventService.splitVoteResults(mockedVoteEvent, "age").get(0).getSplitField()).isEqualTo("18");
		assertThat(voteEventService.splitVoteResults(mockedVoteEvent, "age").get(0).getVotingOption()).isEqualTo("No");
		assertThat(voteEventService.splitVoteResults(mockedVoteEvent, "age").get(0).getCount()).isEqualTo(1);

		assertThat(voteEventService.splitVoteResults(mockedVoteEvent, "age").get(1).getSplitField()).isEqualTo("19");
		assertThat(voteEventService.splitVoteResults(mockedVoteEvent, "age").get(1).getVotingOption()).isEqualTo("Yes");
		assertThat(voteEventService.splitVoteResults(mockedVoteEvent, "age").get(1).getCount()).isEqualTo(1);

		assertThat(voteEventService.splitVoteResults(mockedVoteEvent, "age").get(2).getSplitField()).isEqualTo("24");
		assertThat(voteEventService.splitVoteResults(mockedVoteEvent, "age").get(2).getVotingOption()).isEqualTo("No");
		assertThat(voteEventService.splitVoteResults(mockedVoteEvent, "age").get(2).getCount()).isEqualTo(1);

		assertThat(voteEventService.splitVoteResults(mockedVoteEvent, "age").get(3).getSplitField()).isEqualTo("11");
		assertThat(voteEventService.splitVoteResults(mockedVoteEvent, "age").get(3).getVotingOption()).isEqualTo("Yes");
		assertThat(voteEventService.splitVoteResults(mockedVoteEvent, "age").get(3).getCount()).isEqualTo(2);

		// Split based on Gender and do assertions .
		// grouping by Gender based on the list above. {[F, Yes]=2, [M, No]=2, [M,
		// Yes]=1}
		assertThat(voteEventService.splitVoteResults(mockedVoteEvent, "gender").size()).isEqualTo(3);

		assertThat(voteEventService.splitVoteResults(mockedVoteEvent, "gender").get(0).getSplitField()).isEqualTo("F");
		assertThat(voteEventService.splitVoteResults(mockedVoteEvent, "gender").get(0).getVotingOption()).isEqualTo("Yes");
		assertThat(voteEventService.splitVoteResults(mockedVoteEvent, "gender").get(0).getCount()).isEqualTo(2);

		assertThat(voteEventService.splitVoteResults(mockedVoteEvent, "gender").get(1).getSplitField()).isEqualTo("M");
		assertThat(voteEventService.splitVoteResults(mockedVoteEvent, "gender").get(1).getVotingOption()).isEqualTo("No");
		assertThat(voteEventService.splitVoteResults(mockedVoteEvent, "gender").get(1).getCount()).isEqualTo(2);

		assertThat(voteEventService.splitVoteResults(mockedVoteEvent, "gender").get(2).getSplitField()).isEqualTo("M");
		assertThat(voteEventService.splitVoteResults(mockedVoteEvent, "gender").get(2).getVotingOption()).isEqualTo("Yes");
		assertThat(voteEventService.splitVoteResults(mockedVoteEvent, "gender").get(2).getCount()).isEqualTo(1);

		// Split based on Locality and do assertions .
		// grouping by Gender based on the list above. {[Helsinki, Yes]=1, [Vantaa,
		// Yes]=1, [Espoo, No]=2, [Espoo, Yes]=1}
		assertThat(voteEventService.splitVoteResults(mockedVoteEvent, "locality").size()).isEqualTo(4);

		assertThat(voteEventService.splitVoteResults(mockedVoteEvent, "locality").get(0).getSplitField()).isEqualTo("Helsinki");
		assertThat(voteEventService.splitVoteResults(mockedVoteEvent, "locality").get(0).getVotingOption()).isEqualTo("Yes");
		assertThat(voteEventService.splitVoteResults(mockedVoteEvent, "locality").get(0).getCount()).isEqualTo(1);

		assertThat(voteEventService.splitVoteResults(mockedVoteEvent, "locality").get(1).getSplitField()).isEqualTo("Vantaa");
		assertThat(voteEventService.splitVoteResults(mockedVoteEvent, "locality").get(1).getVotingOption()).isEqualTo("Yes");
		assertThat(voteEventService.splitVoteResults(mockedVoteEvent, "locality").get(1).getCount()).isEqualTo(1);

		assertThat(voteEventService.splitVoteResults(mockedVoteEvent, "locality").get(2).getSplitField()).isEqualTo("Espoo");
		assertThat(voteEventService.splitVoteResults(mockedVoteEvent, "locality").get(2).getVotingOption()).isEqualTo("No");
		assertThat(voteEventService.splitVoteResults(mockedVoteEvent, "locality").get(2).getCount()).isEqualTo(2);

		assertThat(voteEventService.splitVoteResults(mockedVoteEvent, "locality").get(3).getSplitField()).isEqualTo("Espoo");
		assertThat(voteEventService.splitVoteResults(mockedVoteEvent, "locality").get(3).getVotingOption()).isEqualTo("Yes");
		assertThat(voteEventService.splitVoteResults(mockedVoteEvent, "locality").get(3).getCount()).isEqualTo(1);
		

	}
}
