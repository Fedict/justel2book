<?xml version="1.0" encoding="UTF-8" ?>
<package version="3.0" unique-identifier="pub-id" 
		xmlns="http://www.idpf.org/2007/opf"
		xmlns:dc="http://purl.org/dc/elements/1.1/"
	<metadata>
		<dc:title xml:lang="${meta.lang}>${meta.title}</dc:title>
		<dc:language>${meta.lang}</dc:language>
		<dc:date>${meta.now}</dc:date>
	</metadata>
	<manifest>
		<item id="toc" properties="nav" href="toc.xhtml" media-type="application/xhtml+xml"/>
		<item id="cover" href="cover.xhtml" media-type="application/xhtml+xml"/>
		<#list chapters as c>
		<item id="${c.id}" href="${c.file}" media-type="application/xhtml+xml"/>
		</#list>
	</manifest>
	<spine>
		<itemref idref="cover" properties="page-spread-right" />
		<itemref idref="toc" />
		<#list chapters as c>
		<itemref idref="${c.id}" />
		</#list>
	</spine>
</package>