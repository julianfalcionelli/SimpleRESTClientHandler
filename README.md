SimpleRESTClientHandler 
===========
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-SimpleRESTClientHandler-green.svg?style=true)](https://android-arsenal.com/details/1/3209)

SimpleRESTClientHandler is an Open Source Android library that allows developers to easily make request to a REST server usign VOLLEY and GSON. 
Using GSON as dependency SimpleRESTClientHandler parse the responses automatically to the Model of your interest.

Setup
-----
### Add SimpleRESTClientHandler as a Module
Download the project and include in your project as a module.

Make sure that the `build.gradle` of your application has the module depency

```groovy
dependencies {
    compile project(":simplerestclienthandler")
}
```


### Initialize the service

Initialize the RestClientManager in your Application class in the #onCreate() method.

```java
RestClientManager.initialize(getInstance()).enableDebugLog(true);
```

The `enableDebugLog` method allows you too see the request information in the console.

Simple Example
-----

### JSON Request
To make a request that returns a JSON object you need to call the `makeJsonRequest` after obtaining RestClientManager instance.
Required parameters are the request method (POST, GET, PUT, DELETE), the URL of the server endpoint, and RequestHanlder object.

You can also pass new headers to the request in a `Map <String, String>` as an Authorization header.
We also recommend spending a tag request to cancel the specific request for some reasons.

```java
RestClientManager.getInstance().makeJsonRequest(Request.Method.POST, url, new RequestHandler<>(new RequestCallbacks<ResponseModel, ErrorModel>()
		{
			@Override
			public void onRequestSuccess(ResponseModel response)
			{

			}

			@Override
			public void onRequestError(ErrorModel error)
			{

			}

		}, parameters));
```

To create an instance of `Request Handler` you need to spend a instance of `Request Callbacks`. This class has two types, the model answer at the first set and the second type defines the model error. If you do not want to analyze the response or error just pass a 'Object` in each of the types.

Remember that the classes passed to the `Request Callbacks` object must be use GSON, where each of the values assigned to GSON must correspond to the parameters sent by the server.

The `Request Callbacks` object has four methods:

-  `onRequestStart`: This method is always executed before the request is made.
-  `onRequestSuccess`: It is obligatory to overwrite. This method will be execute if the request is successfull (HTTP Status = 200)
-  `onRequestError`: It is obligatory to overwrite. This method will be execute if the request failed (HTTP Status != 200)
-  `onRequestFinish`: This method is always executed after the request is made, independently if the request failed or not.

You can pass to the  `Request Handler` object the parameters. The parameters can be a bundle or any object that use GSON, and automatically the library will parse the object to a valid json.

### JSON Array Request
To make a request that returns a JSON Array you need to call the `makeJsonArrayRequest`.

```java
RestClientManager.getInstance().makeJsonArrayRequest(Request.Method.GET, url, new RequestHandler<>(new RequestCallbacks<List<ResponseItemModel>, ErrorModel>()
		{
			@Override
			public void onRequestSuccess(List<ResponseItemModel> response)
			{

			}

			@Override
			public void onRequestError(ErrorModel error)
			{

			}

		}, parameters));
```

NEWS
-----
- Now Support Multipart Requests! 

License
-----
    Copyright 2016 Julián Falcionelli

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
