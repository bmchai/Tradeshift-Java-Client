This is a library to use the Tradeshift API from your own Java service,
in conjunction with an App on Tradeshift.

Installation
============

You can create a Maven dependency on this library:

    <dependency>
      <groupId>com.tradeshift.developer</groupId>
      <artifactId>tradeshift-client-java</artifactId>
      <version>1.0.0</version>
    </dependency>

Accessing your own account
==========================

Take a look at the `OAuth1ClientTest` example. In order to access your
own account, first install the
[OwnAccount](https://go.tradeshift.com/apps/view/Tradeshift.APIAccessToOwnAccount) 
app on Tradeshift. You  can do this in either production or sandbox; 
just make sure you use the same environment when testing.

On tradeshift, note the token and token secret that the app shows you.

You'll need to come up with a `User-Agent` to use for your HTTP calls.
This can be any string that is a valid HTTP user agent, but must identify
you as a user.

From your Java code, create a client like so:

    TradeshiftRestClient client = TradeshiftRestClient.production("/// Put-Your-User-Agent-Here ///");
    OAuth1TokenClient account = client.forOwnAccount("put-your-token-here", "put-your-secret-here");
    System.out.println("My account info: " + AccountOps.on(account).getInfo());

Take a look at the `AccountOps` class for an example on how to write 
Java wrappers for other API calls you might be interested in, for
the rest of the [Tradeshift API](https://api.tradeshift.com/tradeshift/rest/external/doc).

Accessing many accounts
=======================

The full process of integrating your backend server with Tradeshift is
as follows:

1. You register as a [developer](https://sandbox.tradeshift.com/developer) on the Tradeshift sandbox.
-  You write an App (possibly just using the [OAuth demo app](https://github.com/Tradeshift/Tradeshift-Apps/blob/master/src/js/samples/viewOauthToken.json) 
as an example).
-  You create consumer key on Tradeshift.
-  You write your server-side Java component using this library, configuring it with your consumer key.
-  You deploy your Java component on your own URL.
-  You have your Java component expose a callback resource by using `OAuth1CallbackResource`, which Tradeshift can call when people activate/deactivate your app. You can configure this URL on Tradeshift.
-  You send your App to Tradeshift to have it deployed. 

The callback resource that the library implements, is a JAX-RS annotated
class. So it's best if your web application uses JAX-RS as well.
