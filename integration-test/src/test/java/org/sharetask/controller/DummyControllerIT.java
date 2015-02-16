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

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class DummyControllerIT {

	public static final String BASE = System.getProperty("application.base.url") == null ? "http://localhost:8080/sharetask"
			: System.getProperty("application.base.url");
	public static final String BASE_PATH = "/api/info";
	
	public static final String URL = BASE + BASE_PATH;
    
    @Test
    public void testIfAppIsUp() throws IOException {
        //given
        HttpClient client = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(URL);
 
        //when
        HttpResponse response = client.execute(httpget);
 
        //then
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        if (entity != null) {
        	String result = EntityUtils.toString(entity);
        	Assert.assertEquals("OK", result);
        } else {
        	Assert.fail("No content!");
        }
    }
}
