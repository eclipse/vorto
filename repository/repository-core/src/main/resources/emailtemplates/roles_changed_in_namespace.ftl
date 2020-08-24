<html>
<body>
Dear ${recipient},
<br/><br>
your roles in the <b>${namespace}</b> namespace have been changed by one of its administrators.
<br/><br/>
Your new roles on <b>${namespace}</b> are:
<ul>
    <#list roles as r>
      <li>${r}</li>
    </#list>
</ul>
<br/>
<br/>
Thanks for using Vorto!
<br/><br/>
Kind regards,
<br/><br/><br/>
<b>Team Vorto</b>
</body>
</html>