/*
 * Copyright (c) 2019, FPS BOSA
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package be.fedict.justel2book.dao;

import java.util.ArrayList;
import java.util.List;

/**
 * Book content helper class
 * 
 * @author Bart Hanssens
 */
public class BookContent {
	/**
	 * Content entry helper class
	 */
	public class Entry {
		private final String href;
		private final String text;
		
		/**
		 * Get link reference
		 * 
		 * @return 
		 */
		public String getHref() {
			return href;
		}

		/**
		 * Constructor
		 * 
		 * @param href HTML href
		 * @param text text
		 */
		public Entry(String href, String text) {
			this.href = href;
			this.text = text;
		}
	}
	
	private final List<BookContent.Entry> content = new ArrayList();
	
	/**
	 * Get full content
	 * 
	 * @return list of content parts
	 */
	public List<BookContent.Entry> getContent() {
		return content;
	}

	/**
	 * Add an entry to the contents
	 * 
	 * @param href HTML reference
	 * @param text
	 */
	public void add(String href, String text) {
		content.add(new Entry(href, text));
	}
}
