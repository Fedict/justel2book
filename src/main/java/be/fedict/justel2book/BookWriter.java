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

import be.fedict.justel2book.dao.Book;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Generic interface for writing ebook
 * 
 * @author Bart Hanssens
 */
public interface BookWriter {
	/**
	 *
	 * @param file
	 * @param book
	 * @throws IOException
	 */
	public void write(Path file, Book book) throws IOException;

	/**
	 * Start book creation
	 * 
	 * @throws java.io.IOException
	 */
	public void startBook() throws IOException;
	
	/**
	 * Write cover
	 * 
	 * @throws IOException 
	 */
	public void writeCover() throws IOException;
	
	/**
	 * Write preface
	 * 
	 * @throws IOException 
	 */
	public void writePreface() throws IOException;
	
	/**
	 * Write Table of Contents
	 * 
	 * @throws IOException 
	 */
	public void writeTOC() throws IOException;
	
	/**
	 * Write content
	 * 
	 * @throws IOException 
	 */
	public void writeContent() throws IOException;
	
	/**
	 * End book creation
	 * 
	 * @throws IOException 
	 */
	public void endBook() throws IOException;
}
