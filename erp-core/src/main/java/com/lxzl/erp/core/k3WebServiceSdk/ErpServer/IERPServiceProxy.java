package com.lxzl.erp.core.k3WebServiceSdk.ErpServer;

import com.lxzl.erp.core.k3WebServiceSdk.ERPServer_Models.*;

public class IERPServiceProxy implements IERPService {
  private String _endpoint = null;
  private IERPService iERPService = null;
  
  public IERPServiceProxy() {
    _initIERPServiceProxy();
  }
  
  public IERPServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initIERPServiceProxy();
  }
  
  private void _initIERPServiceProxy() {
    try {
      iERPService = (new ERPServiceLocator()).getBasicHttpBinding_IERPService();
      if (iERPService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)iERPService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)iERPService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (iERPService != null)
      ((javax.xml.rpc.Stub)iERPService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public IERPService getIERPService() {
    if (iERPService == null)
      _initIERPServiceProxy();
    return iERPService;
  }
  
  public ServiceResult addSupply(FormSupply supply) throws java.rmi.RemoteException{
    if (iERPService == null)
      _initIERPServiceProxy();
    return iERPService.addSupply(supply);
  }
  
  public ServiceResult addICItem(FormICItem item) throws java.rmi.RemoteException{
    if (iERPService == null)
      _initIERPServiceProxy();
    return iERPService.addICItem(item);
  }
  
  public ServiceResult addOrganization(FormOrganization cust) throws java.rmi.RemoteException{
    if (iERPService == null)
      _initIERPServiceProxy();
    return iERPService.addOrganization(cust);
  }
  
  public ServiceResult addSEorder(FormSEOrder order) throws java.rmi.RemoteException{
    if (iERPService == null)
      _initIERPServiceProxy();
    return iERPService.addSEorder(order);
  }
  
  
}