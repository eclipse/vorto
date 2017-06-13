# Generator Gateway

This is a single microservice that bundles all the current generators of Vorto, in order
to run more efficiently in constrained environments.

# Running locally

If you are running this locally, you need to include both "local" and either "proxied" or "nonproxied" spring boot profiles to your instance.

If you are running in an environment with a proxy, you need to set the properties "http.proxyHost", "http.proxyPort", "http.proxyUser" and "http.proxyPassword".