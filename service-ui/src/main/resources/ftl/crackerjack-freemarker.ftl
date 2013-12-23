<#function getValue key defaultValue="">
    <#if data[key]??>
        <#return data[key]>
    </#if>
    <#return defaultValue>
</#function>