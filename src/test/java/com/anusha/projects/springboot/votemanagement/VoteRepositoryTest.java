package com.anusha.projects.springboot.votemanagement;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.anusha.projects.springboot.votemanagement.Vote;
import com.anusha.projects.springboot.votemanagement.VoteRepository;

/**
 * The Class VoteRepositoryTest.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class VoteRepositoryTest {

	/** The vote repository. */
	@Autowired
	private VoteRepository voteRepository;

	/**
	 * Test save S.
	 *
	 * @throws ParseException the parse exception
	 */
	@Test
	public void testSaveS() throws ParseException {
		
		Vote vote = new Vote("Hans",25 , "M", "Helsinki", "Yes");
		vote = voteRepository.save(vote);
		//assert of Id is created
		assertThat(vote.getId()).isEqualTo(1);
	}

}
