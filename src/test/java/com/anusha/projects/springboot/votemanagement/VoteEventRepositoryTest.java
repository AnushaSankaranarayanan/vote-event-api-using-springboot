package com.anusha.projects.springboot.votemanagement;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.anusha.projects.springboot.votemanagement.Vote;
import com.anusha.projects.springboot.votemanagement.VoteEvent;
import com.anusha.projects.springboot.votemanagement.VoteEventRepository;

/**
 * The Class VoteEventRepositoryTest.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class VoteEventRepositoryTest {

	/** The vote event repository. */
	@Autowired
	private VoteEventRepository voteEventRepository;

	/**
	 * Test save S.
	 *
	 * @throws ParseException the parse exception
	 */
	@Test
	public void testSaveS() throws ParseException {

		VoteEvent voteEvent = new VoteEvent("VoteEvent1", new SimpleDateFormat("yyyy-MM-dd").parse("2018-04-20"),
				Stream.of("Yes", "No", "Maybe").collect(Collectors.toList()));

		voteEvent.setListOfVotes(
				Stream.of(new Vote("Elsa", 19, "F", "Helsinki", "Yes"), new Vote("Anna", 17, "F", "Espoo", "No"))
						.collect(Collectors.toList()));
		voteEvent = voteEventRepository.save(voteEvent);
		assertThat(voteEvent.getId()).isGreaterThan(0);
		assertThat(voteEvent.getListOfVotes().size()).isEqualTo(2);
	}

	/**
	 * Test find all.
	 *
	 * @throws ParseException the parse exception
	 */
	@Test
	public void testFindAll() throws ParseException {
		VoteEvent voteEvent = new VoteEvent("VoteEvent1", new SimpleDateFormat("yyyy-MM-dd").parse("2018-04-20"),
				Stream.of("Yes", "No", "Maybe").collect(Collectors.toList()));
		voteEventRepository.save(voteEvent);
		voteEvent = new VoteEvent("VoteEvent2", new SimpleDateFormat("yyyy-MM-dd").parse("2018-04-30"),
				Stream.of("Yes", "No").collect(Collectors.toList()));
		voteEventRepository.save(voteEvent);
		List<VoteEvent> listVoteEvent = (List<VoteEvent>) voteEventRepository.findAll();
		assertThat(listVoteEvent.size()).isEqualTo(2);
	}

	/**
	 * Test find by id.
	 *
	 * @throws ParseException the parse exception
	 */
	@Test
	public void testFindById() throws ParseException {
		VoteEvent voteEvent = new VoteEvent("VoteEvent1", new SimpleDateFormat("yyyy-MM-dd").parse("2018-04-20"),
				Stream.of("Yes", "No", "Maybe").collect(Collectors.toList()));
		voteEvent = voteEventRepository.save(voteEvent);
		assertThat(voteEventRepository.findById(voteEvent.getId()).getName()).isEqualTo("VoteEvent1");
		//check for invalid Id
		assertThat(voteEventRepository.findById(9999)).isNull();
	}

	/**
	 * Test find by date.
	 *
	 * @throws ParseException the parse exception
	 */
	@Test
	public void testFindByDate() throws ParseException {
		VoteEvent voteEvent = new VoteEvent("VoteEvent1", new SimpleDateFormat("yyyy-MM-dd").parse("2018-04-20"),
				Stream.of("Yes", "No", "Maybe").collect(Collectors.toList()));
		voteEventRepository.save(voteEvent);
		voteEvent = new VoteEvent("VoteEvent2", new SimpleDateFormat("yyyy-MM-dd").parse("2018-04-30"),
				Stream.of("Yes", "No").collect(Collectors.toList()));
		voteEventRepository.save(voteEvent);
		List<VoteEvent> listVoteEventDueOn20April = (List<VoteEvent>) voteEventRepository
				.findByExpiryDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-04-20"));
		assertThat(listVoteEventDueOn20April.size()).isEqualTo(1);

		listVoteEventDueOn20April = (List<VoteEvent>) voteEventRepository
				.findByExpiryDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-04-21"));
		assertThat(listVoteEventDueOn20April.size()).isEqualTo(0);
	}

	/**
	 * Test update.
	 *
	 * @throws ParseException the parse exception
	 */
	@Test
	public void testUpdate() throws ParseException {
		VoteEvent voteEvent = new VoteEvent("VoteEvent1", new SimpleDateFormat("yyyy-MM-dd").parse("2018-05-31"),
				Stream.of("Yes", "No", "Maybe").collect(Collectors.toList()));
		voteEvent = voteEventRepository.save(voteEvent);
		voteEvent.setExpiryDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-06-30"));
		VoteEvent voteEventUpdated = voteEventRepository.save(voteEvent);
		assertThat(new SimpleDateFormat("yyyy-MM-dd").format(voteEventUpdated.getExpiryDate())).isEqualTo("2018-06-30");
	}

}
