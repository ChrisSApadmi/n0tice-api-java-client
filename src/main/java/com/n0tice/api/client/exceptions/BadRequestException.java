package com.n0tice.api.client.exceptions;

public class BadRequestException extends ServerException
{

	private static final long serialVersionUID = 1L;

	public BadRequestException(String message)
	{
		super(message);
	}

}
