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
package org.sharetask.controller;

import groovyx.net.http.ContentType;
import groovyx.net.http.Method;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.sharetask.test.JsonRestConnector;

import spock.lang.Shared;
import spock.lang.Specification;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
abstract class LoginSpec extends Specification {
	
    @Shared
    JsonRestConnector connector
	
	@Shared
	String context

    def setupSpec() {
		def defaultUrl = System.getProperties().getProperty("application.base.url")
        connector = new JsonRestConnector(defaultUrl == null ? 'http://localhost:8080/sharetask/' : defaultUrl)

		def request = ['username':'dev1@shareta.sk','password':'password']
		connector.post('api/user/login', request)
    }

	def cleanupSpec() {
		connector.get('api/user/logout')
	}
}
