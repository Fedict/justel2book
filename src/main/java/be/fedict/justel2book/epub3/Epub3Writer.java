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
package be.fedict.justel2book.epub3;

import be.fedict.justel2book.BookMeta;
import be.fedict.justel2book.BookWriter;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Write ePUB3 book.
 * This creates a temp directory to generate the metadata and all the XHTML5 files, then ZIP it.
 * 
1 * @author Bart Hanssens
 */
public class Epub3Writer implements BookWriter {
	private final static Logger LOG = LoggerFactory.getLogger(Epub3Writer.class);
	
	private final static String PREFIX = "/be/fedict/justel2book/epub3";
	private final ClassLoader cld;
	private final Configuration fm = new Configuration(Configuration.VERSION_2_3_28);
	
	private Path tempDir;
	private Path tempDirOEBPS;
	
	private Path file;
	private BookMeta meta;
	
	/**
	 * Constructor
	 */
	public Epub3Writer() {
		this.cld = this.getClass().getClassLoader();
		fm.setClassLoaderForTemplateLoading(cld, PREFIX);
	}
	
	@Override
	public void startBook(Path file, BookMeta meta) throws IOException {
		this.file = file;
		this.meta = meta;
		
		// metadata directory
		tempDir = Files.createTempDirectory("epub");
		Path tempDirMeta = Paths.get(tempDir.toString(), "META-INF");
		Files.createDirectories(tempDirMeta);
		Path container = Paths.get(tempDirMeta.toString(), "container.xml");
		Files.copy(cld.getResourceAsStream(PREFIX + "/container.xml"), container);
		
		// content directory
		tempDirOEBPS = Paths.get(tempDir.toString(), "OEBPS");		
		Files.createDirectories(tempDirOEBPS);
	}

	@Override
	public void writeCover() throws IOException  {
		Path cover = Paths.get(tempDirOEBPS.toString(), "cover.xhtml");
		try (BufferedWriter bw = 
				Files.newBufferedWriter(cover, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
			Template tmpl = fm.getTemplate("cover.ftl");
			tmpl.process(meta, bw);
		} catch (TemplateException ex) {
			LOG.debug("Could not writer cover page");
			throw new IOException(ex);
		}
	}

	@Override
	public void writePreface() {
		//
	}

	@Override
	public void writeTOC() {
		//
	}

	@Override
	public void writeContent() {
		//	
	}

	@Override
	public void endBook() {
		try (	FileOutputStream fos = new FileOutputStream(file.toFile());
				ZipOutputStream zip = new ZipOutputStream(fos)) {
			
			ZipEntry zeMime = new ZipEntry("mime-type");
			zip.putNextEntry(zeMime);
			InputStream is = cld.getResourceAsStream(PREFIX + "/mime-type");
			zip.closeEntry();
			
			Path[] paths = Files.walk(tempDirOEBPS).toArray(Path[]::new);
			for (Path p : paths) {
				ZipEntry ze = new ZipEntry("OEBPS/" + p.toFile().getName());
				InputStream ois = Files.newInputStream(p, StandardOpenOption.READ);
				zip.putNextEntry(ze);
				zip.closeEntry();
			}
		} catch (IOException ex) {
			LOG.error("Could not write eBook", ex);
		}
		cleanup();
	}

	private void cleanup() {
		if (tempDir != null) {
			try {
				Files.walk(tempDir).forEach(f -> f.toFile().delete());
				Files.delete(tempDir);
			} catch (IOException ioe) {
				LOG.error("Could not delete (part of) temp dir", ioe);
			}
		}
	}

}
