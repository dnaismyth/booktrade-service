package com.flow.booktrade.service.util;

import com.flow.booktrade.dto.DataSource;

public class ImageUtil {
	
	private static final String GOODREADS_BASE_URL = "https://images.gr-assets.com/books/";

	public static String formatImageUrl(String url, DataSource source){
		if(!source.equals(DataSource.GOODREADS)){
			return url;
		}
		
		if(url.contains(GOODREADS_BASE_URL)){
			String urlSplit = url.substring(GOODREADS_BASE_URL.length());
			urlSplit = urlSplit.replace("m/", "l/");
			url = GOODREADS_BASE_URL.concat(urlSplit);
		}
		
		return url;
	}
}
