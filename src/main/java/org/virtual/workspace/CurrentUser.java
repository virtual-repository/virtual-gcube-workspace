package org.virtual.workspace;

import javax.inject.Inject;

public class CurrentUser {

	private final String username;
	
	@Inject
	public CurrentUser(String name) {
		this.username=name;
	}
	
	public String name() {
		return username;
	}

	@Override
	public String toString() {
		return username;
	}
	
}
