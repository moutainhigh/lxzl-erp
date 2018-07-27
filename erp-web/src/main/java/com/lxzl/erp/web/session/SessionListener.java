package com.lxzl.erp.web.session;

import com.lxzl.erp.core.session.SessionManagement;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class SessionListener implements HttpSessionListener {

    private static SessionManagement sessionManagement = SessionManagement.getInstance();

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        sessionManagement.addSessionId(httpSessionEvent.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        sessionManagement.removeSession(httpSessionEvent.getSession().getId());
    }
}
