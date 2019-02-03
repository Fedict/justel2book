/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.fedict.justel2book.just;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 *
 * @author Bart.Hanssens
 */
public abstract class AbstractJustel {
	private final Map<String,Pattern> matcher = new HashMap<>();
	private int level= 0;

	private String prefix = "";
	private String seq = "";
	private String pretext = "";
	private String text = "";
	private String posttext = "";

	private List<AbstractJustel> parts = new ArrayList<>();
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getPretext() {
		return pretext;
	}

	public void setPretext(String pretext) {
		this.pretext = pretext;
	}

	public String getText() {
		return text;
	}

	public void setText(String pretext) {
		this.text = pretext;
	}

	public String getPosttext() {
		return posttext;
	}

	public void setPosttext(String posttext) {
		this.posttext = posttext;
	}
	
	protected AbstractJustel(int level, String prefix, String seq) {
		this.level = level;
		this.prefix = prefix;
		this.seq = seq;
	}
}
