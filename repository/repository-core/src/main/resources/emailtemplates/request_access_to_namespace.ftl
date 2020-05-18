<html>
<head></head>
<body>
Dear ${recipient},
<br/><br>
a Vorto user has requested access to a namespace you manage:
<table>
  <caption>Details</caption>
  <tr>
    <th>Namespace</th>
    <th>User to add</th>
    <th>Suggested roles (if any)</th>
    <th>User who initiated the request</th>
  </tr>
  <tr>
    <td>${namespace}</td>
    <td>${targetUser}</td>
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
<hr/>
Your username and e-mail address have <b>not</b> been shared with requesting user ${requestingUser}.<br/>
You can safely disregard this request if you do not know the users involved. <br/>
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
Thanks for using Vorto!
<br/><br/>
Kind regards,
<br/><br/><br/>
<b>Team Vorto</b>
</body>
</html>