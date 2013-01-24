package com.n0tice.api.client.exceptions;

public class NotFoundException extends ServerException
{
	private static final long serialVersionUID = 1L;

	public NotFoundException(String message)
	{
		super(message);
	}

}
