/**
 * IERPService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.lxzl.erp.core.k3WebServiceSdk.ErpServer;

import com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.*;

public interface IERPService extends java.rmi.Remote {
    public ServiceResult addSupply(FormSupply supply) throws java.rmi.RemoteException;
    public ServiceResult addICItem(FormICItem item) throws java.rmi.RemoteException;
    public ServiceResult addOrganization(FormOrganization cust) throws java.rmi.RemoteException;
    public ServiceResult addSEorder(FormSEOrder order) throws java.rmi.RemoteException;
    public ServiceResult addUser(FormUser user) throws java.rmi.RemoteException;
    public ServiceResult addSEOutstock(FormSEOutStock user) throws java.rmi.RemoteException;
}
