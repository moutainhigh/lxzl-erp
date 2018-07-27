package com.lxzl.erp.core.session;

import java.util.HashSet;

public class SessionManagement {
    private HashSet<String> sessionIdSet;

    private SessionManagement() {
        sessionIdSet = new HashSet<>();
    }

    public static SessionManagement getInstance() {
        return EnmuSingleton.Singleton.getInstance();
    }

    public boolean hasSession(String id) {
        return sessionIdSet.contains(id);
    }

    public void addSessionId(String id) {
        if (id != null && id.length() > 0)
            sessionIdSet.add(id);
    }

    public void removeSession(String id) {
        if (id != null && id.length() > 0)
            sessionIdSet.remove(id);
    }

    public HashSet<String> getSessionIdSet() {
        return sessionIdSet;
    }

    public void setSessionIdSet(HashSet<String> sessionIdSet) {
        this.sessionIdSet = sessionIdSet;
    }

    private enum EnmuSingleton {
        Singleton;

        private SessionManagement singleton;

        EnmuSingleton() {
            singleton = new SessionManagement();
        }

        public SessionManagement getInstance() {
            return singleton;
        }
    }
}
