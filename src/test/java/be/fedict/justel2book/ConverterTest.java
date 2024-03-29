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

import be.fedict.justel2book.dao.BookTOC;
import be.fedict.justel2book.dao.BookMeta;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 * @author Bart Hanssens
 */
@RunWith(JUnitPlatform.class)
public class ConverterTest {
	private final static ClassLoader cl = ConverterTest.class.getClassLoader();

	@TempDir
	static Path tmpdir;
	
	private static File infile;
	private static JustelReader conv;

	@BeforeAll
	static void getTestFile() throws IOException {
		Path p = Paths.get(tmpdir.toString(), "file.html");
		try(InputStream is = cl.getResourceAsStream("be/fedict/justel2book/strafwetboek_nl.html")) {
			Files.copy(is, p);
		}
		infile = p.toFile();
		conv = new JustelReader();
		conv.fetch(infile);
	}
	
	@AfterAll
	static void deleteTestFile() {
		if (infile != null) {
			infile.delete();
		}
	}
	
	@Test
	void testMeta() throws IOException {
		BookMeta meta = conv.getMeta();

		assertEquals(new URL("http://www.ejustice.just.fgov.be/eli/wet/1867/06/08/1867060850/justel"), meta.getEli());
		assertEquals(LocalDate.of(1867, 6, 9), meta.getPubDate());
		assertEquals(LocalDate.of(1867, 10, 15), meta.getFromDate());
	}
	
	@Test
	void testTOC() throws IOException {
		BookTOC toc = conv.getTOC();
		
		assertEquals(toc.getTOC().size(), 164);
	}
}
