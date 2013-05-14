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
'use strict';

/* Services */
var shareTaskApp = angular.module('shareTaskApp.services', ['ngResource']);

shareTaskApp.service('User', function($resource, $http) {
	
	this.authenticate = function(user, success, error) {
		console.log("Login user (user: %o)", user);
		//return $http.post('/sharetask/api/user/login', {username: 'dev1@shareta.sk', password: 'password'}).success(success).error(error);
		return $http.post('/sharetask/api/user/login', {username: user.username, password: user.password}).success(success).error(error);
	};
});

shareTaskApp.service('Workspace', function($resource, $http) {
	
	this.find = function(input, success, error) {
		console.log("Getting workspaces from server for type (type: %s)", input.type);
		var workspace = $resource("/sharetask/api/workspace", {type: input.type}, {query: {method: "GET", isArray: true}});
		workspace.query(function(response){return success(response);}, function(response){return error(response);});
	};
	
	this.getActiveTasks = function(input, success, error) {
		console.log("Getting active tasks for workspace (id: %s) from server", input.workspaceId);
		var task = $resource("/sharetask/api/workspace/"+input.workspaceId+"/task", {taskQueue: 'ALL'}, {query: {method: "GET", isArray: true}});
		task.query(function(response){return success(response);}, function(response){return error(response);});
	};
	
	this.getCompletedTasks = function(input, callback) {
		console.log("Getting completed tasks for workspace (id: %s) from server", input.workspaceId);
		return $resource("/sharetask/api/workspace/"+input.workspaceId+"/task", {taskQueue: 'FINISHED'}, {query: {method: "GET", isArray: true}}).query(callback);
	};
	
	this.create = function(input, success, error) {
		console.log("Create workspace (workspace: %o)", input.workspace);
		return $http.post("/sharetask/api/workspace/", input.workspace).success(success).error(error);
	};
	
	this.addMember = function(input, success, error) {
		console.log("Add new member (user: %o) to workspace (id: %o)", input.user, input.workspaceId);
		return $http.post("/sharetask/api/workspace/"+input.workspaceId+"/member", {username: input.user.username}).success(success).error(error);
	};
	
	this.removeMember = function(input, success, error) {
		console.log("Delete member (username: %o) from workspace (id: %o)", input.username, input.workspaceId);
		return $http.delete("/sharetask/api/workspace/"+input.workspaceId+"/member/"+input.username, input.user).success(success).error(error);
	};
});

shareTaskApp.service('Task', function($resource, $http) {
	
	this.findById = function(input, callback) {
		console.log("Getting task (id: %s) from server", input.id);
		return $resource("/sharetask/resources-1.0.0/scripts/data/task-"+input.id+".json", {}, {query: {method: "GET", isArray: false}}).query(callback);
	};
	
	this.create = function(input, success, error) {
		console.log("Create task (task: %o) on workspace (id: %s)", input.task, input.workspaceId);
		return $http.post("/sharetask/api/workspace/"+input.workspaceId+"/task", input.task).success(success).error(error);
	};
	
	this.update = function(input, success, error) {
		console.log("Update task (task: %o) on workspace (id: %s)", input.task, input.workspaceId);
		return $http.put("/sharetask/api/workspace/"+input.workspaceId+"/task", input.task).success(success).error(error);
	};
	
	this.complete = function(input, success, error) {
		console.log("Complete task (id: %s) on workspace (id: %s)", input.taskId, input.workspaceId);
		return $http.post("/sharetask/api/workspace/"+input.workspaceId+"/task/"+input.taskId+"/complete", {}).success(success).error(error);
	};
});

shareTaskApp.service('Logger', function($log) {
	
	this.debug = function() {
		
	};
});

shareTaskApp.service('Utils', function($resource) {
	
	this.isCookieEnabled = function() {
		console.log("isCookieEnabled called");
		var cookieEnabled = (navigator.cookieEnabled) ? true : false;
		if (typeof navigator.cookieEnabled == "undefined" && !cookieEnabled) {
			document.cookie="testcookie";
			cookieEnabled = (document.cookie.indexOf("testcookie") != -1) ? true : false;
		}
		return (cookieEnabled);
	};
});

shareTaskApp.service('LocalStorage', function($resource) {
	
	var keyPrefix = 'sharetask-storage-';
	
	this.get = function(key) {
		return JSON.parse(localStorage.getItem(keyPrefix + key) || '[]');
	};
	
	this.store = function(key, data) {
		console.log("Store data (data: %o) to local storage (key: %s)", data, keyPrefix + key);
		localStorage.setItem(keyPrefix + key, JSON.stringify(data));
	};
	
	this.remove = function(key) {
		console.log("Remove from local storage (key: %s)", keyPrefix + key);
		localStorage.removeItem(keyPrefix + key);
	};
});
