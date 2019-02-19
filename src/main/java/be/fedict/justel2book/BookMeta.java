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
package be.fedict.justel2book;

import java.net.URL;
import java.time.LocalDate;

/**
 * Book metadata helper class
 * 
 * @author Bart Hanssens
 */
public class BookMeta {
	private String title;
	private String description;
	private String language;
	private LocalDate genDate;
	private LocalDate pubDate;
	private LocalDate fromDate;
	private URL eli;

	/**
	 * Get book title
	 * 
	 * @return title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set book title
	 * 
	 * @param title title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Get description
	 * 
	 * @return text 
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Set description
	 * 
	 * @param description 
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Get ISO language code
	 * 
	 * @return language code 
	 */
	public String getLanguage() {
		return language;
	}
	
	/**
	 * Set ISO language code
	 * 
	 * @param language code 
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * Get book generation data
	 * 
	 * @return date 
	 */
	public LocalDate getGenDate() {
		return genDate;
	}
	
	/**
	 * Set book generation date
	 * 
	 * @param genDate date 
	 */
	public void setGenDate(LocalDate genDate) {
		this.genDate = genDate;
	}

	/**
	 * Set publication date
	 * 
	 * @return publication date
	 */
	public LocalDate getPubDate() {
		return pubDate;
	}

	/**
	 * Set publication date
	 * 
	 * @param pubDate publication date
	 */
	public void setPubDate(LocalDate pubDate) {
		this.pubDate = pubDate;
	}

	/**
	 * Set from date
	 * 
	 * @return from date
	 */
	public LocalDate getFromDate() {
		return fromDate;
	}

	/**
	 * Set valid from date
	 * 
	 * @param fromDate start date
	 */
	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * Get European Legislation Identifier
	 * 
	 * @return URI
	 */
	public URL getEli() {
		return eli;
	}

	/**
	 * Set European Legislation Identifier
	 * 
	 * @param eli URI 
	 */
	public void setEli(URL eli) {
		this.eli = eli;
	}
}
