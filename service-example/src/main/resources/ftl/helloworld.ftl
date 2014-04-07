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
<hr/>
<div>
    <h2>Asynchronous View Model Demonstration</h2>
    <h3>Future #1: ${crk.getValue('future1')} (expected 'future1')</h3>
    <h3>Future #2: ${crk.getValue('future2')} (expected 'future2')</h3>
</div>
</body>
</html>