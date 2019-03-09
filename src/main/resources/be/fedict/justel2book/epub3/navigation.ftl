<html xmlns="http://www.w3.org/1999/xhtml" xmlns:epub="http://www.idpf.org/2007/ops">
	<head>
		<title>${meta.title!""}</title>
	</head>
	<body>
	<section epub:type="frontmatter toc">
		<header>
			<h1>TOC</h1>
		</header>
		<nav epub:type="toc" id="toc">
        <ol>
			<#if toc.entries??>
			<#list toc.entries as c>
			<li>
				<a href="${c.href!""}">${c.title?html}</a>
			</li>
			</#list>
			</#if>
		</ol>
		</nav>
	</section>
	</body>
</html>
