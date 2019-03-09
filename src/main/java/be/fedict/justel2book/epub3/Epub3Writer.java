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

import be.fedict.justel2book.BookWriter;
import be.fedict.justel2book.dao.Book;
import be.fedict.justel2book.dao.BookMeta;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Write ePUB3 book.
 * This creates a temp directory to generate the metadata and all the XHTML5 files, then ZIPs it.
 * 
1 * @author Bart Hanssens
 */
public class Epub3Writer implements BookWriter {
	private final static Logger LOG = LoggerFactory.getLogger(Epub3Writer.class);
	
	private final static String PREFIX = "be/fedict/justel2book/epub3";
	private final ClassLoader cld;
	private final Configuration fm = new Configuration(Configuration.VERSION_2_3_28);
	
	private Path tempDir;
	private Path tempDirOEBPS;
	
	private Path file;
	private Book book;
	
	/**
	 * Constructor
	 */
	public Epub3Writer() {
		this.cld = this.getClass().getClassLoader();
		fm.setClassLoaderForTemplateLoading(cld, PREFIX);
	}
	
	@Override
	public void startBook() throws IOException {
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

	/**
	 * Write a simple part of the EPUB file
	 * 
	 * @param partfile name of the file
	 * @param template template name
	 * @param name display name
	 * @param map variables for the template
	 * @throws IOException 
	 */
	private void writePart(String partfile, String template, String name, Map map) throws IOException {
		Path part = Paths.get(tempDirOEBPS.toString(), partfile);

		try (BufferedWriter bw = 
				Files.newBufferedWriter(part, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
			Template tmpl = fm.getTemplate(template);
			tmpl.process(map, bw);
		} catch (TemplateException ex) {
			LOG.error("Could not write {}", name);
			throw new IOException(ex);
		}
	}
	
	/**
	 * Write EPUB package info
	 * 
	 * @throws IOException 
	 */
	private void writePackage() throws IOException  {
		Map<String,Object> map = new HashMap<>();
		map.put("meta", Collections.singletonMap("meta", book.getMeta()));
		map.put("toc", Collections.singletonMap("entries", book.getTOC().getTOC()));
		writePart("content.opf", "opf.ftl", "package", map);
	}

	@Override
	public void writeCover() throws IOException  {
		Map<String, BookMeta> meta = Collections.singletonMap("meta", book.getMeta());
		writePart("cover.xhtml", "cover.ftl", "cover", meta);
	}

	@Override
	public void writePreface() {
		//
	}

	@Override
	public void writeTOC() throws IOException {
		Map<String,Object> map = new HashMap<>();
		map.put("meta", Collections.singletonMap("meta", book.getMeta()));
		map.put("toc", Collections.singletonMap("entries", book.getTOC().getTOC()));
		writePart("toc.xhtml", "navigation.ftl", "ToC", map);
	}

	@Override
	public void writeContent() {
		Map<String,Object> map = new HashMap<>();

	}

	@Override
	public void endBook() throws IOException {
		try (	FileOutputStream fos = new FileOutputStream(file.toFile());
				ZipOutputStream zip = new ZipOutputStream(fos)) {

			ZipEntry zeMime = new ZipEntry("mime-type");
			zip.putNextEntry(zeMime);
			try (InputStream is = cld.getResourceAsStream(PREFIX + "/mime-type")) {
				LOG.info("Write {}", zeMime.getName());
				copyIO(is, zip);
			}
			zip.closeEntry();
			
			Path[] paths = Files.walk(tempDirOEBPS).filter(Files::isRegularFile).toArray(Path[]::new);
			for (Path p : paths) {
				ZipEntry ze = new ZipEntry("OEBPS/" + p.toFile().getName());
				zip.putNextEntry(ze);
				try (InputStream ois = Files.newInputStream(p, StandardOpenOption.READ)) {
					LOG.info("Write {}", ze.getName());
					copyIO(ois, zip);
				}
				zip.closeEntry();
			}
		} catch (IOException ex) {
			LOG.error("Could not write eBook", ex);
		}
	}
	
	@Override
	public void write(Path file, Book book) throws IOException {
		this.file = file;
		this.book = book;
		LOG.info("Write EPUB to {}", file);
		
		try {
			startBook();
			writePackage();
			writeCover();
			writePreface();
			writeTOC();
			writeContent();
			endBook();
		} finally {
			cleanup();
		}
	}

	/**
	 * Remove temporary files
	 */
	private void cleanup() {
		if (tempDir != null) {
			try {
				for(Path p : Files.walk(tempDir)
							.sorted((a, b) -> b.compareTo(a))
							.toArray(Path[]::new)) {
					Files.delete(p);
				}
			} catch (IOException ioe) {
				LOG.error("Could not delete (part of) temp dir", ioe);
			}
		}
	}

	/**
	 * Copy data from one stream to another
	 * 
	 * @param is input stream
	 * @param os output stream
	 * @throws IOException 
	 */
	private void copyIO(InputStream is, OutputStream os) throws IOException {
		byte[] buf = new byte[16384];

        for (int len; (len= is.read(buf)) != -1; ){
            os.write(buf, 0, len);
        }
	}
}
