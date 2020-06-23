package com.anusha.projects.springboot.votemanagement;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anusha.projects.springboot.votemanagement.exception.ForbiddenException;
import com.anusha.projects.springboot.votemanagement.exception.NotFoundException;

/**
 * The Class VoteEventController.
 */
@RestController
public class VoteEventController {

	private Logger logger = Logger.getLogger(this.getClass());

	/** The vote event service. */
	@Autowired
	private VoteEventService voteEventService;

	/**
	 * Gets the vote events.
	 *
	 * @return the vote events
	 */
	@RequestMapping("/voteEvents")
	public List<VoteEvent> getVoteEvents(@RequestParam(required = false) String showActive) {
		if (StringUtils.isEmpty(showActive)) {
			return voteEventService.getAllVoteEvents();
		} else {
			// for non true and false values - throw an exception.
			if (showActive.equalsIgnoreCase("true")) {
				return voteEventService.getAllActiveVoteEvents();
			}
			logger.error("Invalid showActive parameter" + showActive + "Passed. Permissible value is true");
			throw new ForbiddenException(
					"Invalid showActive parameter" + showActive + "Passed. Permissible value is true");
		}

	}

	/**
	 * Gets the vote events by id.
	 *
	 * @param voteEventId the vote event id
	 * @return the vote events by id
	 */
	@RequestMapping("/voteEvents/{voteEventId}")
	public VoteEvent getVoteEventsById(@PathVariable int voteEventId) {
		VoteEvent voteEvent = voteEventService.getVoteEventById(voteEventId);
		if (voteEvent == null) {
			logger.error("VoteEvent Not Present for id : " + voteEventId + ". Cannot display Results");
			throw new NotFoundException("VoteEvent Not Present. Cannot display Results");
		}
		return voteEvent;
	}

	/**
	 * Creates the vote event.
	 *
	 * @param voteEvent the vote event
	 * @return the vote event
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/voteEvents")
	public VoteEvent createVoteEvent(@RequestBody VoteEvent voteEvent) {
		if (validateVoteEvent(voteEvent)) {
			return voteEventService.createVoteEvent(voteEvent);
		}
		logger.error("Mandatory Fields missing in VoteEvent. Required Fields are : name  / expiryDate / listOfOptions");
		throw new ForbiddenException(
				"Mandatory Fields missing in VoteEvent. Required Fields are : name  / expiryDate / listOfOptions");
	}

	/**
	 * Edits the vote event.
	 *
	 * @param voteEvent   the vote event
	 * @param voteEventId the vote event id
	 * @return the vote event
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/voteEvents/{voteEventId}")
	public VoteEvent editVoteEvent(@RequestBody VoteEvent voteEvent, @PathVariable int voteEventId) {
		/**
		 * If the ID is not present in the DB or required fields are missing - throw
		 * exception.
		 */
		VoteEvent voteEventInDB = voteEventService.getVoteEventById(voteEventId);
		if (voteEventInDB == null) {
			logger.error("VoteEvent Not Present for id : " + voteEventId + ". Cannot Update");
			throw new NotFoundException("VoteEvent Not Present. Cannot Update");
		}
		if (validateVoteEvent(voteEvent)) {
			return voteEventService.updateVoteEvent(voteEvent, voteEventInDB);
		}
		logger.error("Mandatory Fields missing in VoteEvent. Required Fields are : name  / expiryDate / listOfOptions");
		throw new ForbiddenException(
				"Mandatory Fields missing in VoteEvent. Required Fields are : name  / expiryDate / listOfOptions");
	}

	/**
	 * Adds the vote to event.
	 *
	 * @param vote        the vote
	 * @param voteEventId the vote event id
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/voteEvents/{voteEventId}/vote")
	public void addVoteToEvent(@RequestBody Vote vote, @PathVariable int voteEventId) {
		/**
		 * If the ID is not present in the DB or required fields are missing - throw
		 * exception.
		 */
		VoteEvent voteEventInDB = voteEventService.getVoteEventById(voteEventId);
		if (voteEventInDB == null) {
			logger.error("VoteEvent Not Present for id : " + voteEventId + ". Cannot Update");
			throw new NotFoundException("VoteEvent Not Present. Cannot Update");
		}
		if (!validateVote(vote)) {
			logger.error(
					"Mandatory Fields missing in Vote. Required Fields are : name  / age / gender / locality / votingoption");
			throw new ForbiddenException(
					"Mandatory Fields missing in Vote. Required Fields are : name  / age / gender / locality / votingoption");
		}
		/**
		 * Check if the vote has expired. If Yes throw an exception. Check if the vote
		 * with Name , Age , Gender , Locality Combination exists. If yes - throw an
		 * exception.
		 **/
		if (voteEventService.checkIfVoteEventIsExpired(voteEventInDB)) {
			logger.error("VoteEvent Is Already Expired for Id : " + voteEventId);
			throw new ForbiddenException("VoteEvent Is Already Expired.");
		}
		if (voteEventService.checkIfVoteAlreadyExists(vote, voteEventInDB)) {
			logger.error("A Vote with similar details is already registered for Id : " + voteEventId);
			throw new ForbiddenException("A Vote with similar details is already registered.");
		}

		voteEventService.addVoteToVoteEvent(vote, voteEventInDB);
	}

	/**
	 * Gets the vote results.
	 *
	 * @param splitBy     the split by
	 * @param voteEventId the vote event id
	 * @return the vote results
	 */
	@RequestMapping(value = "/voteEvents/{voteEventId}/voteResults")
	public List<VoteResult> getVoteResults(@RequestParam(required = false) String splitBy,
			@PathVariable int voteEventId) {
		VoteEvent voteEventInDB = voteEventService.getVoteEventById(voteEventId);
		if (voteEventInDB == null) {
			logger.error("VoteEvent Not Present for id : " + voteEventId + ". Cannot display Results");
			throw new NotFoundException("VoteEvent Not Present. Cannot display Results");
		}
		if (!voteEventService.checkIfVoteEventIsExpired(voteEventInDB)) {
			/**
			 * check if the vote is ongoing. If yes throw an exception since results will be
			 * shown only for completed vote events.
			 **/
			logger.error("VoteEvent Is Still Open. Cannot display Results for Id : " + voteEventId);
			throw new ForbiddenException("VoteEvent Is Still Open. Cannot display Results");
		}
		if (StringUtils.isNotEmpty(splitBy)) {
			/**
			 * check if the queryparam is either age or gender or locality. IF not, throw an
			 * exception.
			 **/
			if (splitBy.equalsIgnoreCase("age") || splitBy.equalsIgnoreCase("gender")
					|| splitBy.equalsIgnoreCase("locality")) {
				return voteEventService.splitVoteResults(voteEventInDB, splitBy);
			} else {
				logger.error("Splitting by " + splitBy
						+ "Not allowed. Permissible fields are age/gender/locality for Id : " + voteEventId);
				throw new ForbiddenException(
						"Splitting by " + splitBy + "Not allowed. Permissible fields are age/gender/locality.");
			}
		} else {
			return voteEventService.getVotingResults(voteEventInDB);
		}
	}

	/**
	 * Validate vote event.
	 *
	 * @param voteEvent the vote event
	 * @return true, if successful
	 */
	private boolean validateVoteEvent(VoteEvent voteEvent) {
		// check for not null non empty values
		if (StringUtils.isEmpty(voteEvent.getName()) || null == voteEvent.getExpiryDate()
				|| CollectionUtils.isEmpty(voteEvent.getListOfOptions())) {
			return false;
		}

		return true;
	}

	/**
	 * Validate vote.
	 *
	 * @param vote the vote
	 * @return true, if successful
	 */
	private boolean validateVote(Vote vote) {
		// check for not null non empty values
		if (StringUtils.isEmpty(vote.getName()) || StringUtils.isEmpty(vote.getGender()) || vote.getAge() <= 0
				|| StringUtils.isEmpty(vote.getLocality()) || StringUtils.isEmpty(vote.getVotingOption())) {
			return false;
		}
		return true;
	}

}
