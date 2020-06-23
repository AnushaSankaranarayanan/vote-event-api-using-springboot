package com.anusha.projects.springboot.votemanagement;

/**
 * The Class VoteResult.
 */
public class VoteResult {

	
	/** The split field. */
	private String splitField;
	
	/** The voting option. */
	private String votingOption;
	
	/** The count. */
	private long count;
	
	/**
	 * Instantiates a new vote result.
	 */
	public VoteResult() {
		
	}

	/**
	 * Instantiates a new vote result.
	 *
	 * @param splitField the split field
	 * @param votingOption the voting option
	 * @param count the count
	 */
	public VoteResult(String splitField, String votingOption, long count) {
		super();
		this.splitField = splitField;
		this.votingOption = votingOption;
		this.count = count;
	}

	/**
	 * Gets the split field.
	 *
	 * @return the split field
	 */
	public String getSplitField() {
		return splitField;
	}

	/**
	 * Sets the split field.
	 *
	 * @param splitField the new split field
	 */
	public void setSplitField(String splitField) {
		this.splitField = splitField;
	}

	/**
	 * Gets the voting option.
	 *
	 * @return the voting option
	 */
	public String getVotingOption() {
		return votingOption;
	}

	/**
	 * Sets the voting option.
	 *
	 * @param votingOption the new voting option
	 */
	public void setVotingOption(String votingOption) {
		this.votingOption = votingOption;
	}

	/**
	 * Gets the count.
	 *
	 * @return the count
	 */
	public long getCount() {
		return count;
	}

	/**
	 * Sets the count.
	 *
	 * @param count the new count
	 */
	public void setCount(long count) {
		this.count = count;
	}

	}
