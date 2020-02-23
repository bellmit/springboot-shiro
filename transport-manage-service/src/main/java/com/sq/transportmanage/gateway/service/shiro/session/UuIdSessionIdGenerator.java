package com.sq.transportmanage.gateway.service.shiro.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;

import java.io.Serializable;
import java.util.UUID;

public class UuIdSessionIdGenerator implements SessionIdGenerator{

	@Override
	public Serializable generateId(Session session) {
		
		return UUID.randomUUID().toString().replace("-", "").toUpperCase();
	}

}
