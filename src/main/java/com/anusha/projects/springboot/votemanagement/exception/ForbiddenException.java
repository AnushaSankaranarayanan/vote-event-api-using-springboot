package com.anusha.projects.springboot.votemanagement.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;;

/**
 * The Class ForbiddenException.
 */
@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {

	/**
	 * Instantiates a new forbidden exception.
	 *
	 * @param exception the exception
	 */
	public ForbiddenException(String exception) {
		super(exception);
	}
}