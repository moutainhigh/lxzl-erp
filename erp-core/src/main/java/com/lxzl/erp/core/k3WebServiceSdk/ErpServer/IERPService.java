/**
 * IERPService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.lxzl.erp.core.k3WebServiceSdk.ErpServer;

import com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.FormICItem;
import com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.FormOrganization;
import com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.FormSupply;
import com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.ServiceResult;

public interface IERPService extends java.rmi.Remote {
    public ServiceResult addSupply(FormSupply supply) throws java.rmi.RemoteException;
    public ServiceResult addICItem(FormICItem item) throws java.rmi.RemoteException;
    public ServiceResult addOrganization(FormOrganization cust) throws java.rmi.RemoteException;
}
