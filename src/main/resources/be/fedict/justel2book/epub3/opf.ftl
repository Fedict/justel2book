<?xml version="1.0" encoding="UTF-8" ?>
<package version="3.0" unique-identifier="pub-id" 
		xmlns="http://www.idpf.org/2007/opf"
		xmlns:dc="http://purl.org/dc/elements/1.1/">
	<metadata>
		<dc:title xml:lang="${meta.lang!''}">${meta.title!''}</dc:title>
		<dc:language>${meta.lang!""}</dc:language>
		<dc:date>${meta.now!""}</dc:date>
	</metadata>
	<manifest>
		<item id="toc" properties="nav" href="toc.xhtml" media-type="application/xhtml+xml"/>
		<item id="cover" href="cover.xhtml" media-type="application/xhtml+xml"/>
		<#if toc.entries??>
		<#list toc.entries as c>
		<item id="${c.href!''}" href="content-${c.href!''}.xhtml" media-type="application/xhtml+xml"/>
		</#list>
		</#if>
	</manifest>
	<spine>
		<itemref idref="cover" properties="page-spread-right" />
		<itemref idref="toc" />
		<#if toc.entries??>
		<#list toc.entries as c>
		<itemref idref="${c.href!''}" />
		</#list>
		</#if>
	</spine>
	<guide>
		<reference type="cover" title="Cover" href="cover.xhtml"/>
		<reference type="toc" title="toc.xhtml"/>
		</guide>
</package>