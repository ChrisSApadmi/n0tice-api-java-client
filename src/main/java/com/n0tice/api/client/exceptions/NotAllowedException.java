package com.n0tice.api.client.exceptions;

public class NotAllowedException extends ServerException
{
	public NotAllowedException()
	{

	}

	public NotAllowedException(String aMessage)
	{
		super(aMessage);
	}

	private static final long serialVersionUID = 1L;

}
