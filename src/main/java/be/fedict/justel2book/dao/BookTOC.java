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
 * Book table of contents helper class
 * 
 * @author Bart Hanssens
 */
public class BookTOC {
	/**
	 * TOC entry helper class
	 */
	public class Entry {
		private final String href;
		private final String prefix;
		private final String title;
		
		/**
		 * Get link reference
		 * 
		 * @return 
		 */
		public String getHref() {
			return href;
		}

		/**
		 * Get prefix
		 * 
		 * @return prefix
		 */
		public String getPrefix() {
			return prefix;
		}

		/**
		 * Get title
		 * 
		 * @return title
		 */
		public String getTitle() {
			return title;
		}

		/**
		 * Constructor
		 * 
		 * @param href HTML href
		 * @param prefix title prefix
		 * @param title TOC entry title
		 */
		public Entry(String href, String prefix, String title) {
			this.href = href;
			this.prefix = prefix;
			this.title = title;
		}
	}
	
	private final List<BookTOC.Entry> toc = new ArrayList();
	

	/**
	 * Get full ToC
	 * 
	 * @return list with ToC
	 */
	public List<BookTOC.Entry> getTOC() {
		return toc;
	}

	/**
	 * Add an entry to the ToC
	 * 
	 * @param href HTML reference
	 * @param prefix title prefix
	 * @param title name of the entry 
	 */
	public void add(String href, String prefix, String title) {
		toc.add(new Entry(href, prefix, title));
	}
}
