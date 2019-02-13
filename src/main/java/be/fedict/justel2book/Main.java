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
import java.nio.file.Files;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Convert
 * 
 * @author Bart Hanssens
 */
public class Main {
	private final static Logger LOG = LoggerFactory.getLogger(Main.class);
	
	private final static Options OPTS = new Options()
			.addOption("f", "file", true, "use local file")
			.addRequiredOption("c", "config", true, "config file")
			.addRequiredOption("o", "outdir", true, "output dir");

	/**
	 * Print help info
	 */
	private static void printHelp() {
		HelpFormatter fmt = new HelpFormatter();
		fmt.printHelp("Justel 2 eBook", OPTS);
	}
	
	/**
	 * Parse command line arguments
	 * 
	 * @param args arguments
	 * @return parsed command line
	 */
	private static CommandLine parse(String[] args) {
		CommandLineParser cli = new DefaultParser();
		try {
			return cli.parse(OPTS, args);
		} catch (ParseException ex) {
			printHelp();
		}
		return null;
	}
	
	/**
	 * Main
	 * 
	 * @param args 
	 */
	public static void main(String args[]) {
		CommandLine cli  = parse(args);
		if (cli == null) {
			LOG.error("Cannot parse command line");
			System.exit(-1);
		}

		// get parameters from command line
		File cfg = new File(cli.getOptionValue("c"));
		if (! (cfg.exists() && cfg.isFile() && cfg.canRead())) {
			LOG.error("Cannot read config file {}", cfg);
			System.exit(-2);
		}
		
		File dir = new File(cli.getOptionValue("o"));
		if (! (dir.exists() && dir.isDirectory() && dir.canWrite()) || dir.mkdirs()) {
			LOG.error("Cannot write to output directory {}", dir);
			System.exit(-3);
		} 
		
		Properties props = new Properties();
		try {
			props.load(Files.newInputStream(cfg.toPath()));
		} catch (IOException ex) {
			LOG.error("Loading config file failed", ex.getMessage());
			System.exit(-4);
		}

		File f = cli.hasOption("f") ? new File(cli.getOptionValue("f")) : null;
		if (f != null) {
			if (! (f.exists() && f.isFile() && f.canRead())) {
				LOG.error("Cannot read input file {}", f);
				System.exit(-5);
			}
		}

		// conversion starts here
		Converter conv = new Converter();		
		try {
			if (f == null) {
				conv.fetch(props.getProperty("justel2book.url"), 
							props.getProperty("justel2book.proxy.host"), 
							Integer.getInteger(props.getProperty("justel2book.proxy.port"), 0));
				conv.saveLocal(new File(dir, "out.html"));
			} else {
				conv.fetch(f);
			}
		} catch (IOException ioe) {
			LOG.error("Could not download or import HTML page", ioe.getMessage());
		}
		conv.getMeta();
	}
}
