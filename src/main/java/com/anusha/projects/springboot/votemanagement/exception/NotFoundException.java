package com.anusha.projects.springboot.votemanagement.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;;

/**
 * The Class ForbiddenException.
 */
@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

	/**
	 * Instantiates a new forbidden exception.
	 *
	 * @param exception the exception
	 */
	public NotFoundException(String exception) {
		super(exception);
	}
}