## How To Add a JWT Token Verification Provider

In order for Vorto to recognize JWT tokens coming from your organization, you must need to add a new JWT token verification provider. The steps for doing this are detailed below.

### Implement the _TokenVerificationProvider_ interface
The _TokenVerificationProvider_ has 3 methods you need to implement.

1. For verifying if a token is valid
```
boolean verify(HttpServletRequest request, JwtToken jwtToken); 
```
    
2. Creating an authentication object out of the token
```
OAuth2Authentication createAuthentication(HttpServletRequest request, JwtToken jwtToken);
```
        
3. To return the identifier for your verification provider
```
String getIssuer();
```

The _AbstractTokenVerificationProvider_ contains several methods that are most common in operations involving verification of JWT tokens. You can take a look at it as an example or if it contains most logic for your verifier, you can subclass it.

### Annotate your implementation as a spring component bean

Your implementation for the **TokenVerificationProvider** interface must become a Spring bean. The easiest way to do this is to annotate it with _@Component_ annotation. 