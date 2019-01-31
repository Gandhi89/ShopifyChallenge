# ShopifyChallenge

As part of Shopify challenge,this application is build to gather important data from large information. This application uses OkHttp Library to get response from API and picasso Library to load image from API 

![Alt Text](https://github.com/Gandhi89/ShopifyChallenge/blob/master/Shopify.gif)

# Libraries 
* `OkHttp Library` - For sending and receive HTTP-based network requests
* `picasso Library` - A powerful image downloading and caching library

# Android Component 
Two anroid components are used:
* Activity
* Broadcast Receiver

The main work around while working with APIs can be internet connectivity. In this application to avoide failure (because of internet connectivity) `Broadcast Receiver` is used. Application listens for broadcast if intenet connectivity is enabled or disabled by user. 

![Alt Text](https://github.com/Gandhi89/ShopifyChallenge/blob/master/shopifyBR.gif)
