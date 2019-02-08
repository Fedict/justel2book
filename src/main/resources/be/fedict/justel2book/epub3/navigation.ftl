<html xmlns="http://www.w3.org/1999/xhtml" xmlns:epub="http://www.idpf.org/2007/ops">
	<head>
		<title>${title}</title>
	</head>
	<body>
	<section epub:type="frontmatter toc">
		<header>
			<h1>TOC</h1>
		</header>
		<nav epub:type="toc" id="toc">
        <ol>
			<#list chapters as c>
			<li id="${c.id}">
				<a href="${c.file}">${c.title}</a>
			</li>
			</#list>
		</ol>
		</nav>
	</section>
	</body>
</html>
