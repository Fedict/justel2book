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

/**
 * Book helper class
 * 
 * @author Bart Hanssens
 */
public class Book {
	private BookMeta meta;
	private BookTOC toc;
	private BookContent content;

	/**
	 * Get meta data
	 * 
	 * @return title
	 */
	public BookMeta getMeta() {
		return meta;
	}

	/**
	 * Set book meta data
	 * 
	 * @param meta meta data
	 */
	public void setMeta(BookMeta meta) {
		this.meta = meta;
	}

	/**
	 * Get table of contents
	 * 
	 * @return table of contents
	 */
	public BookTOC getTOC() {
		return toc;
	}
	
	/**
	 * Set table of contents
	 * 
	 * @param toc table of contents 
	 */
	public void setTOC(BookTOC toc) {
		this.toc = toc;
	}
	
	/**
	 * Get content
	 * 
	 * @return content
	 */
	public BookContent getContent() {
		return content;
	}
	
	/**
	 * Set contents
	 * 
	 * @param content content
	 */
	public void setContent(BookContent content) {
		this.content = content;
	}
}
