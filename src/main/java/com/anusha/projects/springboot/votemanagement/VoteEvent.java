package com.anusha.projects.springboot.votemanagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The Class VoteEvent.Represents the VoteEvent Entity in DB.
 */
@Entity
public class VoteEvent {

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	/** The id. */
	private int id;

	/** The name. */
	private String name;

	/** The expiry date. */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date expiryDate;

	/** The list of options. */
	@ElementCollection(targetClass=String.class)
	private List<String> listOfOptions;

	/** The list of votes. */
	@JsonIgnore
	@OneToMany
	private List<Vote> listOfVotes;

	/** The sample. */

	/**
	 * Instantiates a new vote event.
	 */
	public VoteEvent() {
	}

	/**
	 * Instantiates a new vote event.
	 *
	 * @param name          the name
	 * @param expiryDate    the expiry date
	 * @param listOfOptions the list of options
	 */
	public VoteEvent(String name, Date expiryDate, List<String> listOfOptions) {
		super();
		this.name = name;
		this.expiryDate = expiryDate;
		this.listOfOptions = listOfOptions;
		// Instantiate a new Array for the Votes.
		this.listOfVotes = new ArrayList<>();
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the expiry date.
	 *
	 * @return the expiry date
	 */
	public Date getExpiryDate() {
		return expiryDate;
	}

	/**
	 * Sets the expiry date.
	 *
	 * @param expiryDate the new expiry date
	 */
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	/**
	 * Gets the list of options.
	 *
	 * @return the list of options
	 */
	public List<String> getListOfOptions() {
		return listOfOptions;
	}

	/**
	 * Sets the list of options.
	 *
	 * @param listOfOptions the new list of options
	 */
	public void setListOfOptions(List<String> listOfOptions) {
		this.listOfOptions = listOfOptions;
	}

	/**
	 * Gets the list of votes.
	 *
	 * @return the list of votes
	 */
	public List<Vote> getListOfVotes() {
		return listOfVotes;
	}

	/**
	 * Sets the list of votes.
	 *
	 * @param listOfVotes the new list of votes
	 */
	public void setListOfVotes(List<Vote> listOfVotes) {
		this.listOfVotes = listOfVotes;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VoteEvent [id=" + id + ", name=" + name + ", expiryDate=" + expiryDate + ", listOfOptions="
				+ listOfOptions + ", listOfVotes=" + listOfVotes + "]";
	}

}
