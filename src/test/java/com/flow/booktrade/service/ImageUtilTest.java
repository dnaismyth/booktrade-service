package com.flow.booktrade.service;

import org.junit.Assert;
import org.junit.Test;
import com.flow.booktrade.dto.DataSource;
import com.flow.booktrade.service.util.ImageUtil;


public class ImageUtilTest {
	
	@Test
	public void testFormatImageUrl(){
		String bookUrl = "https://images.gr-assets.com/books/1327593023m/8697149.jpg";
		String expected = "https://images.gr-assets.com/books/1327593023l/8697149.jpg";
		String formatted = ImageUtil.formatImageUrl(bookUrl, DataSource.GOODREADS);
		Assert.assertEquals(expected, formatted);
	}
}