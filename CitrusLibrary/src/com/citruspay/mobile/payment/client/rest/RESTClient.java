/*
   Copyright 2014 Citrus Payment Solutions Pvt. Ltd.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/


package com.citruspay.mobile.payment.client.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RESTClient {

	private final URI uri;
	private final HttpClient http;

	public RESTClient(URI uri, HttpClient http) {
		this.uri = uri;
		this.http = http;
	}

	public URI getUri() {
		return uri;
	}

	public JSONObject post(
			URI path, 
			Collection<Header> headers,
			Map<String, String> params) throws ProtocolException, RESTException {
		// convert params
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> param : params.entrySet()) {
			pairs.add(new BasicNameValuePair(param.getKey(), param.getValue()));
		}

		// create request + entity
		HttpPost post = new HttpPost(uri.resolve(path));
		try {
			post.setEntity(new UrlEncodedFormEntity(pairs));
		} catch (UnsupportedEncodingException uex) {
			throw new ProtocolException(uex);
		}

		// execute request
		return execute(post, headers);
	}
	
	public JSONArray postStatement(
			URI path, 
			Collection<Header> headers,
			Map<String, String> params) throws ProtocolException, RESTException {
		// convert params
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> param : params.entrySet()) {
			pairs.add(new BasicNameValuePair(param.getKey(), param.getValue()));
		}

		// create request + entity
		HttpPost post = new HttpPost(uri.resolve(path));
		try {
			post.setEntity(new UrlEncodedFormEntity(pairs));
		} catch (UnsupportedEncodingException uex) {
			throw new ProtocolException(uex);
		}

		// execute request
		return executeforStatement(post, headers);
	}
	
	public JSONObject postRequest(
			URI path, 
			Collection<Header> headers,
			Map<String, List<String>> params) throws ProtocolException, RESTException {
		// convert params
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		for (Map.Entry<String, List<String>> param : params.entrySet()) {
			for(String value: param.getValue()){
				pairs.add(new BasicNameValuePair(param.getKey(),value));
			}		
			
		}

		// create request + entity
		HttpPost post = new HttpPost(uri.resolve(path));
		try {
			post.setEntity(new UrlEncodedFormEntity(pairs));
		} catch (UnsupportedEncodingException uex) {
			throw new ProtocolException(uex);
		}

		// execute request
		return execute(post, headers);
	}
	

	public JSONObject put(
			URI path, 
			Collection<Header> headers, 
			JSONObject json) throws ProtocolException, RESTException {
		// create request + entity
		HttpPut put = new HttpPut(uri.resolve(path));
		put.addHeader("Content-Type", "application/json");
		try {
			put.setEntity(new StringEntity(json.toString()));
		} catch (UnsupportedEncodingException uex) {
			throw new RuntimeException(uex);
		}

		// execute request
		return execute(put, headers);
	}
	
	
	
	public JSONObject put(
			URI path, 
			Collection<Header> headers, 
			Map<String, String> params) throws ProtocolException, RESTException {
		
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> param : params.entrySet()) {
			pairs.add(new BasicNameValuePair(param.getKey(), param.getValue()));
		}
		// create request + entity
		HttpPut put = new HttpPut(uri.resolve(path));
		
		try {
			put.setEntity(new UrlEncodedFormEntity(pairs));
		} catch (UnsupportedEncodingException uex) {
			throw new RuntimeException(uex);
		}

		// execute request
		return execute(put, headers);
	}


	public JSONObject get(
			URI path, 
			Collection<Header> headers) throws ProtocolException, RESTException {
		// execute request
		return execute(new HttpGet(uri.resolve(path)), headers);
	}

	protected JSONObject execute(
			HttpUriRequest request,
			Collection<Header> headers) throws ProtocolException, RESTException {
		// set headers
		for (Header h : headers) {
			request.addHeader(h);
		}

		// execute request
		HttpResponse resp = null;
		try {
			resp = http.execute(request);
		} catch (IOException iox) {
			throw new ProtocolException(iox);
		}

		// parse response
		try {
			switch (resp.getStatusLine().getStatusCode()) {
			case HttpStatus.SC_OK:
				return new JSONObject(EntityUtils.toString(resp.getEntity()));
			case HttpStatus.SC_NO_CONTENT:
				return new JSONObject();
			case HttpStatus.SC_BAD_REQUEST:
				throw new RESTException(
						resp.getStatusLine().getStatusCode(),
						new JSONObject(EntityUtils.toString(resp.getEntity())));
			default:
				throw new RESTException(
						resp.getStatusLine().getStatusCode(),
						new JSONObject().put("entity", EntityUtils.toString(resp.getEntity())));
			}
		} catch (JSONException jx) {
			throw new ProtocolException(jx);
		} catch (IOException iox) {
			throw new ProtocolException(iox);
		}
	}
	
	protected JSONArray executeforStatement(HttpUriRequest request,	Collection<Header> headers) throws ProtocolException, RESTException {
		for (Header h : headers) {
			request.addHeader(h);
		}

		// execute request
		HttpResponse resp = null;
		try {
			resp = http.execute(request);
		} catch (IOException iox) {
			throw new ProtocolException(iox);
		}

		// parse response
		try {
			switch (resp.getStatusLine().getStatusCode()) {
			case HttpStatus.SC_OK:
				return new JSONArray(EntityUtils.toString(resp.getEntity()));
			case HttpStatus.SC_NO_CONTENT:
				return new JSONArray();
			case HttpStatus.SC_BAD_REQUEST:
				throw new RESTException(
						resp.getStatusLine().getStatusCode(),
						new JSONObject(EntityUtils.toString(resp.getEntity())));
			default:
				throw new RESTException(
						resp.getStatusLine().getStatusCode(),
						new JSONObject().put("entity", EntityUtils.toString(resp.getEntity())));
			}
		} catch (JSONException jx) {
			throw new ProtocolException(jx);
		} catch (IOException iox) {
			throw new ProtocolException(iox);
		}
	}
}
