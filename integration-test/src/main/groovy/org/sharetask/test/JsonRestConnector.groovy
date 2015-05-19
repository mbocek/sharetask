/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.sharetask.test

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import groovyx.net.http.Method
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.HttpResponseDecorator

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
class JsonRestConnector {
	private static final Logger logger = LoggerFactory.getLogger(JsonRestConnector.class);
	
	private String baseUrl
	private HTTPBuilder httpBuilder
	private List<String> cookies
	private HttpResponseDecorator response

	JsonRestConnector(String url) {
		this.baseUrl = url
		this.httpBuilder = initializeHttpBuilder()
		this.cookies = []
	}

	def get(String url) {
		get(url, null)
	}

	def get(String url, Map<String, Serializable> params) {
		request(Method.GET, ContentType.JSON, url, params, null)
	}

	def post(String url, Map<String, Serializable> requestBody) {
		post(url, null, requestBody)
	}

	def post(String url, Map<String, Serializable> params, Map<String, Serializable> requestBody) {
		request(Method.POST, ContentType.JSON, url, params, requestBody)
	}
	
	def request(Method method, ContentType contentType, String url, Map<String, Serializable> params, Map<String, Serializable> requestBody) {
		logger.debug("Send $method request to ${this.baseUrl}$url: $params")
		httpBuilder.request(method, contentType) { request ->
			uri.path = url
			uri.query = params
			headers['Cookie'] = cookies.join(';')
			body = requestBody
		}
	}

	private HTTPBuilder initializeHttpBuilder() {
		def httpBuilder = new HTTPBuilder(baseUrl)

		httpBuilder.handler.success = { HttpResponseDecorator resp, reader ->
			resp.getHeaders('Set-Cookie').each {
				String cookie = it.value.split(';')[0]
				logger.debug("Adding cookie to collection: $cookie")
				cookies.add(cookie)
				response = resp
			}
			logger.debug("Response: ${reader}")
			return reader
		}
		return httpBuilder
	}
}
