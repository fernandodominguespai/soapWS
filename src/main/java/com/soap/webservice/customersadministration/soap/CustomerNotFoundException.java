package com.soap.webservice.customersadministration.soap;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@SoapFault(faultCode = FaultCode.CUSTOM, customFaultCode = "{http://soap.webservice.com}001_Cliente_nao_encontrado")
public class CustomerNotFoundException extends RuntimeException{

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public CustomerNotFoundException(String message){
        super(message);
    }
}
