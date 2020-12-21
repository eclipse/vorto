<html>
<head>
  <style>
    th,td {
      border: 1px solid black;
    }
  </style>
</head>
<body>
Dear ${recipient},
<br/><br>
a Vorto user has requested access to a namespace you manage, for user <b>${targetUser}</b>:<br/><br/>
<table>
  <caption>Details</caption>
  <tr>
    <th>Namespace</th>
    <th>User to add</th>
    <th>Subject</th>
    <th>Suggested roles (if any)</th>
    <th>User who initiated the request</th>
  </tr>
  <tr>
    <td>${namespace}</td>
    <td><b>${targetUser}</b></td>
    <td>${subject}</td>
    <td>
      <ul>
          <#list suggestedRoles as r>
            <li>${r}</li>
          </#list>
      </ul>
    </td>
    <td>${requestingUser}</td>
  </tr>
</table>
<br/>
Click <a href="${host}/#/provideAccessToNamespace?namespace=${namespace}&targetUsername=${targetUsername}&targetUserOAuthProvider=${targetUserOAuthProvider}&desiredRoles=<#list suggestedRoles as role>${role}<#sep>,</#list>">here</a> to approve, reject or modify the request.
<br/>
<hr/>
<br/>
Your username and e-mail address have <b>not</b> been shared with requesting user ${requestingUser}.<br/><br/>
You can safely disregard this request if you do not know the users involved. <br/><br/>
If you believe this request constitutes a form of abuse, or wish to convey any issue with it, you
can:
<ul>
  <li>
    Notify the Vorto team at
    <a href="mailto:vorto-dev@eclipse.org?subject=Namespace%20access%20request%20abuse">vorto-dev@eclipse.org</a>
    for moderation.
  </li>
  <li>
    Remove your e-mail address from your Vorto account, in order to stop receiving automatic
    notifications from Vorto.
  </li>
</ul>
<br/>
<hr/>
<br/>
Thanks for using Vorto!
<br/><br/>
Kind regards,
<br/><br/><br/>
<b>Team Vorto</b>
</body>
</html>
