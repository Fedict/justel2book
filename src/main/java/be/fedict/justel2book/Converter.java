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

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Get an HTML page and clean it
 * 
 * @author Bart Hanssens
 */
public class Converter {
	private final static Logger LOG = LoggerFactory.getLogger(Converter.class);
	
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
		doc = Jsoup.connect(url).proxy(proxy).timeout(30_000).userAgent("Justel2eBook").get();
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
	public void save(File f) throws IOException {
		LOG.info("Saving doc to {}", f);
		Files.write(f.toPath(), 
					doc.outerHtml().getBytes(StandardCharsets.ISO_8859_1), 
					StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
	}
	
	/**
	 * Get meta from HTML page
	 * 
	 * @return 
	 */
	public BookMeta getMeta() {
		doc.body().select("table:first-child td[colspan=5]");
		return new BookMeta();
	}
}
