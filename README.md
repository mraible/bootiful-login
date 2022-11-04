# Bootiful Login

By default, this app starts with an Auth0 app configured. 

Start the app:

```
./gradlew bootRun
```

And open http://localhost:8080 in your favorite browser. You'll be redirected to login to a "Bootiful Login" app. 

You should be able to change the Auth0 app using the following command:

```
echo '{"issuer": "https://dev-06bzs1cu.us.auth0.com/", "clientId": "BgFUlOC2NZhEXunM5feV9YkG2KJv2bTL", 
  "clientSecret": "sndlETfH0V8YJfawZJfB_z1Y4rj3T10rq5YVuNLgtb9WVgfp60pQA-ibjS0r7NGG"}' | http POST :8080/api/configure
```

The client ID and client secret in the above command are different from the defaults. Ideally, they'd be used after you run this command and going to `http://localhost:8080` does not redirect you to the "Bootiful Login" app. Instead, it redirects you to an app called "Spring Boot".
