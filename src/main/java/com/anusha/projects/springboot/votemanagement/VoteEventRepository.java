package com.anusha.projects.springboot.votemanagement;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

/**
 * The Interface VoteEventRepository.
 * Brings along handy methods from the Super class, to be used directly from the Service layer.
 * Will be handy for Simple operations like CRUD and at the same time, custom methods can be defined.
 */
public interface VoteEventRepository extends CrudRepository<VoteEvent, String> {

	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the vote event
	 */
	public VoteEvent findById(int id);
	
	/**
	 * Find by expiry date.
	 *
	 * @param expiryDate the expiry date
	 * @return the list
	 */
	public List<VoteEvent> findByExpiryDate(Date expiryDate);

}
