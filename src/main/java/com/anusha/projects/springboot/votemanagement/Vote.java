package com.anusha.projects.springboot.votemanagement;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * The Class Vote.
 */
@Entity
public class Vote {

	/** The id. */
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	/** The id. */
	private int id;
	
	/** The name. */
	private String name;
	
	/** The age. */
	private int age;
	
	/** The gender. */
	private String gender;
	
	/** The locality. */
	private String locality;
	
	/** The option. */
	private String votingOption;
	
	/**
	 * Instantiates a new vote.
	 */
	public Vote() {
		
	}
	
	/**
	 * Instantiates a new vote.
	 *
	 * @param name the name
	 * @param age the age
	 * @param gender the gender
	 * @param locality the locality
	 * @param votingOption the voting option
	 */
	public Vote(String name, int age , String gender, String locality, String votingOption) {
		super();
		this.name = name;
		this.age = age;
		this.gender = gender;
		this.locality = locality;
		this.votingOption = votingOption;
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
	 * Gets the gender.
	 *
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}
	
	/**
	 * Sets the gender.
	 *
	 * @param gender the new gender
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	/**
	 * Gets the age.
	 *
	 * @return the age
	 */
	public int getAge() {
		return age;
	}
	
	/**
	 * Sets the age.
	 *
	 * @param age the new age
	 */
	public void setAge(int age) {
		this.age = age;
	}
	/**
	 * Gets the locality.
	 *
	 * @return the locality
	 */
	public String getLocality() {
		return locality;
	}
	
	/**
	 * Sets the locality.
	 *
	 * @param locality the new locality
	 */
	public void setLocality(String locality) {
		this.locality = locality;
	}
	
	/**
	 * Gets the option.
	 *
	 * @return the option
	 */
	public String getVotingOption() {
		return votingOption;
	}
	
	/**
	 * Sets the option.
	 *
	 * @param votingOption the new voting option
	 */
	public void setVotingOption(String votingOption) {
		this.votingOption = votingOption;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Vote [id=" + id + ", name=" + name + ", gender=" + gender + ", locality=" + locality + ", option="
				+ votingOption + "]";
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + age;
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result + ((locality == null) ? 0 : locality.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vote other = (Vote) obj;
		if (age != other.age)
			return false;
		if (gender == null) {
			if (other.gender != null)
				return false;
		} else if (!gender.equals(other.gender))
			return false;
		if (locality == null) {
			if (other.locality != null)
				return false;
		} else if (!locality.equals(other.locality))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
	
}
