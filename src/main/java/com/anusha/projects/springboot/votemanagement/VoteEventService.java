package com.anusha.projects.springboot.votemanagement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The Class VoteEventService.
 */
@Service
public class VoteEventService {

	/** The logger. */
	private Logger logger = Logger.getLogger(this.getClass());

	/** The vote event repository. */
	@Autowired
	private VoteEventRepository voteEventRepository;

	/** The vote repository. */
	@Autowired
	private VoteRepository voteRepository;

	/**
	 * Gets the all vote events.
	 *
	 * @return the all vote events
	 */
	public List<VoteEvent> getAllVoteEvents() {
		return (List<VoteEvent>) voteEventRepository.findAll();
	}

	/**
	 * Gets the all active vote events.
	 *
	 * @return the all active vote events
	 */
	public List<VoteEvent> getAllActiveVoteEvents() {
		List<VoteEvent> voteEventList = (List<VoteEvent>) voteEventRepository.findAll();
		List<VoteEvent> activeVoteEventsList = new ArrayList<>();
		Date currentDate = new Date();
		if (voteEventList != null) {
			voteEventList.forEach(t -> {
				// Check if current date <= retrieved date.
				if (DateUtils.isSameDay(currentDate, t.getExpiryDate()) || currentDate.before(t.getExpiryDate())) {
					activeVoteEventsList.add(t);
				}
			});
		}
		return activeVoteEventsList;
	}

	/**
	 * Gets the vote event by id.
	 *
	 * @param id the id
	 * @return the vote event by id
	 */
	public VoteEvent getVoteEventById(int id) {
		return voteEventRepository.findById(id);
	}


	/**
	 * Gets the voting results.
	 *
	 * @param voteEventInDB the vote event in DB
	 * @return the voting results
	 */
	public List<VoteResult> getVotingResults(VoteEvent voteEventInDB) {
		// group by the options and count.
		Map<String, Long> groupedMap = voteEventInDB.getListOfVotes().stream()
				.collect(Collectors.groupingBy(Vote::getVotingOption, Collectors.counting()));
		List<VoteResult> listVoteResult = new ArrayList<>();
		/**
		 * Iterate and construct the VoteResult by passing the option and the count.
		 * split field will be null as this is used for specific filters.
		 */
		groupedMap.entrySet().forEach(entry -> {
			VoteResult voteResult = new VoteResult(null, entry.getKey(), entry.getValue());
			listVoteResult.add(voteResult);

		});
		return listVoteResult;
	}


	/**
	 * Split vote results.
	 *
	 * @param voteEventInDB the vote event in DB
	 * @param splitField the split field
	 * @return the list
	 */
	public List<VoteResult> splitVoteResults(VoteEvent voteEventInDB, String splitField) {
		// default - split by age
		Function<Vote, List<Object>> compositeKey = vote -> Arrays.<Object>asList(vote.getAge(),
				vote.getVotingOption());
		switch (StringUtils.lowerCase(splitField)) {
		case "gender":
			// Define a function that creates a composite key using Gender and Option.
			compositeKey = vote -> Arrays.<Object>asList(vote.getGender(), vote.getVotingOption());
			break;
		case "locality":
			// Define a function that creates a composite key using Gender and Option.
			compositeKey = vote -> Arrays.<Object>asList(vote.getLocality(), vote.getVotingOption());
			break;
		}

		// Group by the composite Key and count
		Map<List<Object>, Long> groupedMap = voteEventInDB.getListOfVotes().stream()
				.collect(Collectors.groupingBy(compositeKey, Collectors.counting()));

		List<VoteResult> listVoteResult = new ArrayList<>();
		/**
		 * Iterate and construct the VoteResult using the Split Field , Option and
		 * count. split field -> 1st object in the List Option -> 2nd object in the
		 * list.
		 */
		groupedMap.entrySet().forEach(entry -> {
			VoteResult voteResult = new VoteResult(entry.getKey().get(0).toString(), entry.getKey().get(1).toString(),
					entry.getValue());
			listVoteResult.add(voteResult);

		});
		return listVoteResult;
	}

	/**
	 * Creates the vote event.
	 *
	 * @param voteEvent the vote event
	 * @return the vote event
	 */
	public VoteEvent createVoteEvent(VoteEvent voteEvent) {
		// Initialize fresh list of votes before saving.
		voteEvent.setListOfVotes(new ArrayList<>());
		VoteEvent savedVoteEvent = voteEventRepository.save(voteEvent);
		logger.info("Saved to DB: " + savedVoteEvent.toString());
		return savedVoteEvent;
	}

	/**
	 * Update vote event.
	 *
	 * @param voteEvent the vote event
	 * @param voteEventInDB the vote event in DB
	 * @return the vote event
	 */
	public VoteEvent updateVoteEvent(VoteEvent voteEvent, VoteEvent voteEventInDB) {
		// Set Id and List of votes first before updating the record in DB. Rest of the values
		// are taken from JSON.
		voteEvent.setId(voteEventInDB.getId());
		voteEvent.setListOfVotes(new ArrayList<>());
		voteEvent.getListOfVotes().addAll(voteEventInDB.getListOfVotes());
		logger.info("Updated in DB: " + voteEvent.toString());
		return voteEventRepository.save(voteEvent);
	}

	
	/**
	 * Adds the vote to vote event.
	 *
	 * @param vote the vote
	 * @param voteEventInDB the vote event in DB
	 * @return the vote event
	 */
	public VoteEvent addVoteToVoteEvent(Vote vote, VoteEvent voteEventInDB) {
		Vote savedVote = voteRepository.save(vote);
		voteEventInDB.getListOfVotes().add(savedVote);
		logger.info("Vote Saved to DB: " + voteEventInDB.toString());
		return voteEventRepository.save(voteEventInDB);
	}


	/**
	 * Check if vote event is expired.
	 *
	 * @param voteEventInDB the vote event in DB
	 * @return true, if successful
	 */
	public boolean checkIfVoteEventIsExpired(VoteEvent voteEventInDB) {
		// Check if retrieved date comes before the current date
		Date currentDate = new Date();
		if (DateUtils.isSameDay(currentDate, voteEventInDB.getExpiryDate())) {
			return false;
		} else if (currentDate.before(voteEventInDB.getExpiryDate())) {
			return false;
		} else {
			return true;
		}
	}

	
	/**
	 * Check if vote already exists.
	 *
	 * @param vote the vote
	 * @param voteEventInDB the vote event in DB
	 * @return true, if successful
	 */
	public boolean checkIfVoteAlreadyExists(Vote vote,VoteEvent voteEventInDB) {
		// Vote has equals() method that takes care of the uniqueness of Name , Age,
		// Gender and Locality Combination.
		return voteEventInDB.getListOfVotes().stream().anyMatch(t -> t.equals(vote));
	}
}
