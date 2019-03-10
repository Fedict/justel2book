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

import be.fedict.justel2book.dao.BookContent;
import be.fedict.justel2book.dao.BookTOC;
import be.fedict.justel2book.dao.BookMeta;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Get an HTML page and clean it
 * 
 * @author Bart Hanssens
 */
public class JustelReader {
	private final static Logger LOG = LoggerFactory.getLogger(JustelReader.class);
	
	private final static DateTimeFormatter REV_ISO = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	private final static String ELI_PREFIX = "http://www.ejustice.just.fgov.be/eli";
	
	private Document doc;
	
	/**
	 * Get an HTML page (direct or via proxy) and fix incorrect markup. Time out after 30 seconds
	 * 
	 * @param url
	 * @param proxyHost proxy host or empty for no proxy
	 * @param proxyPort proxy port or zero
	 * @throws IOException
	 */
	public void fetch(String url, String proxyHost, int proxyPort) throws IOException {
		Proxy proxy = (proxyHost == null || proxyHost.isEmpty())
							? Proxy.NO_PROXY 
							: new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
		LOG.info("Getting {}", url);
		doc = Jsoup.connect(url).ignoreHttpErrors(true).proxy(proxy).timeout(30_000)
								.userAgent("Justel2eBook").get();
	}
	
	/**
	 * Load an HTML page from a local file and fix incorrect markup.
	 * 
	 * @param f
	 * @throws IOException 
	 */
	public void fetch(File f) throws IOException {
		LOG.info("Loading {}", f);
		doc = Jsoup.parse(f, StandardCharsets.ISO_8859_1.name());
	}

	/**
	 * Save HTML document to a local file
	 * 
	 * @param f file name
	 * @throws IOException 
	 */
	public void saveLocal(File f) throws IOException {
		LOG.info("Saving doc to {}", f);
		Files.write(f.toPath(), 
				doc.outerHtml().getBytes(StandardCharsets.ISO_8859_1), 
				StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
	}
	
	
	/**
	 * Set publication and other dates from HTML document
	 * 
	 * @param meta meta object to populate
	 * @throws IOException 
	 */
	private void setMetaDates(BookMeta meta) throws IOException {
		Element table = doc.body().select("a[name='titre'] ~ table:first-of-type").first();
		if (table == null) {
			throw new IOException("Head table not found");
		}

		Elements rows = table.select("tr");
		if (rows.isEmpty()) {
			throw new IOException("No head rows found");
		}
		if (!rows.get(1).text().trim().equals("Titel")) {
			throw new IOException("Wrong table, expected title table");
		}

		Elements fonts = rows.select("font[color='red']");
		for (Element font: fonts) {
			if (font.hasText()) {
				String txt = font.text().trim();
				if (txt.equals("Publicatie :")) {
					String d = font.nextSibling().toString().trim();
					try {
						meta.setPubDate(LocalDate.parse(d, REV_ISO));
					} catch (DateTimeParseException dtpe) {
						LOG.error("Could not parse publication date {}", d);
					}
				} else if (txt.equals("Inwerkingtreding :")) {
					String d = font.nextSibling().toString().trim();
					try {
						meta.setFromDate(LocalDate.parse(d, REV_ISO));
					} catch (DateTimeParseException dtpe) {
						LOG.error("Could not parse from date {}", d);
					}
				}
			}
		}
		
	}
	
	/**
	 * Set ELI from HTML document
	 * 
	 * @param meta
	 * @throws IOException
	 */
	private void setMetaEli(BookMeta meta) throws IOException {
		URL url = null;
		Elements els = doc.body().select("table:first-of-type tr td[colspan='5']");
		
		for (Element el: els) {
			if (el.hasText()) {
				String eli = el.text().trim();
				if (eli.startsWith(ELI_PREFIX)) {
					try {
						url = new URL(eli);
					} catch (MalformedURLException ex) {
						LOG.error("Invalid ELI {}", eli);
					}
				}
			}
		}
		if (url == null) {
			throw new IOException("No ELI found");
		}
		meta.setEli(url);
	}

	/**
	 * Get meta from HTML page
	 * 
	 * @return meta elements 
	 * @throws IOException 
	 */
	public BookMeta getMeta() throws IOException {
		BookMeta meta = new BookMeta();
		setMetaEli(meta);
		setMetaDates(meta);
	
		return meta;
	}

	/**
	 * Set table of contents from HTML document
	 * 
	 * @param toc ToC object to populate
	 * @throws IOException 
	 */
	private void setTOC(BookTOC toc) throws IOException {
		Element table = doc.body().select("a[name='tablematiere'] ~ table").first();
		if (table == null) {
			throw new IOException("TOC table not found");
		}

		Elements rows = table.select("tr");
		if (rows == null || rows.isEmpty()) {
			throw new IOException("No rows found");
		}

		Element header = rows.get(0).selectFirst("th");
		if (header == null || !header.wholeText().trim().equals("Inhoudstafel")){
			throw new IOException("Wrong table, expected TOC table");
		}

		Elements links = rows.select("tr th[colspan='3'] a[href^='#LNK']");
		for (Element link: links) {
			if (link.hasText()) {
				String href = link.attr("href");
				String prefix = link.text().trim();
				String title = "";
				
				Node sibl = link.nextSibling();
				while (sibl != null && !(sibl instanceof Element && ((Element) sibl).is("a[href^='#LNK']"))) {
					if (sibl instanceof TextNode) {
						title += " " + ((TextNode) sibl).text().trim();
					}
					sibl = sibl.nextSibling();
				}
				if (title.startsWith(" - ")) {
					title = title.substring(3);
				}
				toc.add(href, prefix, title);
			} else {
				LOG.warn("Skipping link without text");
			}
		}
	}

	/**
	 * Get table of contents from HTML page
	 * 
	 * @return TOC object
	 * @throws IOException
	 */
	public BookTOC getTOC() throws IOException {
		BookTOC toc = new BookTOC();
		setTOC(toc);
		return toc;
	}
	

	/**
	 * Set content from HTML document
	 * 
	 * @param content content object to populate
	 * @throws IOException 
	 */
	private void setContent(BookContent content) throws IOException {
		Element table = doc.body().select("a[name='tablematiere'] ~ table").first();
		if (table == null) {
			throw new IOException("Content table not found");
		}

		Elements rows = table.select("tr");
		if (rows == null || rows.isEmpty()) {
			throw new IOException("No rows found");
		}

		Element header = rows.get(0).selectFirst("th");
		if (header == null || !header.wholeText().trim().equals("Tekst")){
			throw new IOException("Wrong table, expected content table");
		}

		Elements parts = rows.select("tr th[colspan='3'] a[href^='#LNK']");
		for (Element part: parts) {
			if (part.hasText()) {
				String href = part.attr("href");
				StringBuilder builder  = new StringBuilder(2048);
				
				Node sibl = part.nextSibling();
				while (sibl != null && !(sibl instanceof Element && ((Element) sibl).is("a[href^='#LNK']"))) {
					if (sibl instanceof TextNode) {
						builder.append(((TextNode) sibl).text().trim());
					}
					if (sibl instanceof Element && ((Element) sibl).tagName().equals("br")) {
						builder.append("\n");
					}
					sibl = sibl.nextSibling();
				}
				
				content.add(href, builder.toString());
			} else {
				LOG.warn("Skipping content without text");
			}
		}
	}


	/**
	* 
	* @return
	* @throws IOException 
	*/
	public BookContent getContent() throws IOException {
		BookContent content = new BookContent();
		setContent(content);
		return content;
	}
}
