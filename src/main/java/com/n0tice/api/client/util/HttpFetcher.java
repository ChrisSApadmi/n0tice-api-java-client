package com.n0tice.api.client.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;

import com.n0tice.api.client.exceptions.HttpFetchException;
import com.n0tice.api.client.exceptions.NotFoundException;

public class HttpFetcher
{
	private static final int HTTP_TIMEOUT = 15000;

	public HttpFetcher()
	{
	}

	public String fetchContent(String url, String charEncoding) throws HttpFetchException, NotFoundException
	{
		try
		{
			InputStream inputStream = httpFetch(url);
			return readResponseBody(charEncoding, inputStream);
		}
		catch (UnsupportedEncodingException e)
		{
			throw new HttpFetchException(e.getMessage());
		}
		catch (IOException e)
		{
			throw new HttpFetchException(e.getMessage());
		}
	}

	private InputStream httpFetch(String url) throws NotFoundException, HttpFetchException, IllegalStateException, IOException
	{
		HttpGet get = new HttpGet(url);

		get.addHeader(new BasicHeader("User-agent", "gzip"));
		get.addHeader(new BasicHeader("Accept-Encoding", "gzip"));

		HttpResponse execute = executeRequest(get);
		final int statusCode = execute.getStatusLine().getStatusCode();
		if (statusCode == 200)
		{
			return execute.getEntity().getContent();
		}
		if (statusCode == 404)
		{
			throw new NotFoundException("Not found: " + url);
		}
		throw new HttpFetchException();
	}

	private HttpResponse executeRequest(HttpRequestBase request) throws IOException, ClientProtocolException
	{
		log.debug("<< " + request.getMethod() + " " + request.getURI() + " " + request.getProtocolVersion().getProtocol());
		final HttpClient setupHttpClient = setupHttpClient();
		final HttpResponse response = setupHttpClient.execute(request);
		log.debug(">> " + response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());
		return response;
	}

	private HttpClient setupHttpClient()
	{
		HttpClient client = new DefaultHttpClient();
		((AbstractHttpClient) client).addRequestInterceptor(new HttpRequestInterceptor()
		{
			@Override
			public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException
			{
				if (!request.containsHeader("Accept-Encoding"))
				{
					request.addHeader("Accept-Encoding", "gzip");
				}
			}
		});

		((AbstractHttpClient) client).addResponseInterceptor(new HttpResponseInterceptor()
		{
			@Override
			public void process(final HttpResponse response, final HttpContext context) throws HttpException, IOException
			{
				HttpEntity entity = response.getEntity();
				Header ceheader = entity.getContentEncoding();
				if (ceheader != null)
				{
					HeaderElement[] codecs = ceheader.getElements();
					for (int i = 0; i < codecs.length; i++)
					{
						if (codecs[i].getName().equalsIgnoreCase("gzip"))
						{
							response.setEntity(new GzipDecompressingEntity(response.getEntity()));
							return;
						}
					}
				}
			}
		});

		client.getParams().setParameter("http.socket.timeout", HTTP_TIMEOUT);
		client.getParams().setParameter("http.connection.timeout", HTTP_TIMEOUT);
		return client;
	}

	private String readResponseBody(String pageCharacterEncoding, InputStream inputStream) throws UnsupportedEncodingException, IOException
	{
		StringBuilder output = new StringBuilder();

		InputStreamReader input = new InputStreamReader(inputStream, pageCharacterEncoding);
		BufferedReader reader = new BufferedReader(input);

		String line = "";
		while ((line = reader.readLine()) != null)
		{
			output.append(line);
		}
		reader.close();
		return output.toString();
	}

	private static class GzipDecompressingEntity extends HttpEntityWrapper
	{

		public GzipDecompressingEntity(final HttpEntity entity)
		{
			super(entity);
		}

		@Override
		public InputStream getContent() throws IOException, IllegalStateException
		{
			InputStream wrappedin = wrappedEntity.getContent();
			return new GZIPInputStream(wrappedin);
		}

		@Override
		public long getContentLength()
		{
			return this.wrappedEntity.getContentLength();
		}
	}

	private static class log
	{
		public static void info(String str)
		{
			System.out.println(str);
		}

		public static void error(String str)
		{
			System.err.println(str);
		}

		public static void debug(String str)
		{
			System.err.println(str);
		}
	}

}
