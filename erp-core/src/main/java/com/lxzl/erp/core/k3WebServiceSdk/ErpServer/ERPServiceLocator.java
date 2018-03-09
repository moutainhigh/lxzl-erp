/**
 * ERPServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.lxzl.erp.core.k3WebServiceSdk.ErpServer;

import com.lxzl.erp.common.domain.K3Config;

public class ERPServiceLocator extends org.apache.axis.client.Service implements ERPService {

    public ERPServiceLocator() {
    }


    public ERPServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ERPServiceLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for BasicHttpBinding_IERPService
    private String BasicHttpBinding_IERPService_address = K3Config.k3Server;

    public String getBasicHttpBinding_IERPServiceAddress() {
        return BasicHttpBinding_IERPService_address;
    }

    // The WSDD service name defaults to the port name.
    private String BasicHttpBinding_IERPServiceWSDDServiceName = "BasicHttpBinding_IERPService";

    public String getBasicHttpBinding_IERPServiceWSDDServiceName() {
        return BasicHttpBinding_IERPServiceWSDDServiceName;
    }

    public void setBasicHttpBinding_IERPServiceWSDDServiceName(String name) {
        BasicHttpBinding_IERPServiceWSDDServiceName = name;
    }

    public IERPService getBasicHttpBinding_IERPService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(BasicHttpBinding_IERPService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getBasicHttpBinding_IERPService(endpoint);
    }

    public IERPService getBasicHttpBinding_IERPService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            BasicHttpBinding_IERPServiceStub _stub = new BasicHttpBinding_IERPServiceStub(portAddress, this);
            _stub.setPortName(getBasicHttpBinding_IERPServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setBasicHttpBinding_IERPServiceEndpointAddress(String address) {
        BasicHttpBinding_IERPService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (IERPService.class.isAssignableFrom(serviceEndpointInterface)) {
                BasicHttpBinding_IERPServiceStub _stub = new BasicHttpBinding_IERPServiceStub(new java.net.URL(BasicHttpBinding_IERPService_address), this);
                _stub.setPortName(getBasicHttpBinding_IERPServiceWSDDServiceName());
                return _stub;
            }
        }
        catch (Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        String inputPortName = portName.getLocalPart();
        if ("BasicHttpBinding_IERPService".equals(inputPortName)) {
            return getBasicHttpBinding_IERPService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org/", "ERPService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "BasicHttpBinding_IERPService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {
        
if ("BasicHttpBinding_IERPService".equals(portName)) {
            setBasicHttpBinding_IERPServiceEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
