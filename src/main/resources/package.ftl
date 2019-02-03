<package version="3.0" unique-identifier="pub-id" xmlns="http://www.idpf.org/2007/opf">
	<metadata xmlns:dc="http://purl.org/dc/elements/1.1/">
		<dc:title id="dc_title" xml:lang="${lang}>${title}</dc:identifier>
		<dc:language>${lang}</dc:language>
		<dc:date>${now}</dc:date>
	</metadata>
	<manifest>
		<item id="toc" properties="nav" href="toc.html" media-type="application/xhtml+xml"/>
		<#list chapters as c>
			<item id="${c.id}" href="${c.file}" media-type="application/xhtml+xml"/>
		</#list>
	</manifest>
	<spine>
		<itemref idref="toc" />
		<#list chapters as c>
			<itemref idref="${c.id}" />
		</#list>
	</spine>
</package>