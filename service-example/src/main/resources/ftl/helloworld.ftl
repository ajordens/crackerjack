<#import "/ftl/crackerjack-freemarker.ftl" as crk>

<html>
<head>
    <title>Hello World!</title>
</head>
<body>
<div>
    <h1>Hello ${crk.getValue('name')}!</h1>
    <h2>${crk.getValue('nested.value')}</h2>
    <h3>${crk.getValue('nested.value.missing', "Default Value for Null Values")}</h3>
</div>
</body>
</html>