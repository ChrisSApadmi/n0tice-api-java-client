package com.n0tice.api.client.parsers;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class ContentLoader {

	public static String loadContent(String filename) throws IOException {
		StringBuffer content = new StringBuffer();
		File contentFile = new File(ClassLoader.getSystemClassLoader().getResource(filename).getFile());
		Reader freader = new FileReader(contentFile);
		BufferedReader in = new BufferedReader(freader);
		String str;
		while ((str = in.readLine()) != null) {
			content.append(str);
			content.append("\n");
		}
		in.close();
		freader.close();
		return content.toString();
	}

}
