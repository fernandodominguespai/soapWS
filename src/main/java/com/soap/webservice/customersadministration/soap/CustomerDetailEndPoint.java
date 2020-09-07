package com.soap.webservice.customersadministration.soap;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.soap.webservice.customersadministration.helper.Status;
import com.soap.webservice.customersadministration.model.Customer;
import com.soap.webservice.customersadministration.services.CustomerDetailService;
import com.webservice.soap.CustomerDetail;
import com.webservice.soap.DeleteCustomerRequest;
import com.webservice.soap.DeleteCustomerResponse;
import com.webservice.soap.GetAllCustomerDetailRequest;
import com.webservice.soap.GetAllCustomerDetailResponse;
import com.webservice.soap.GetCustomerDetailRequest;
import com.webservice.soap.GetCustomerDetailResponse;

@Endpoint
public class CustomerDetailEndPoint {

	@Autowired
	CustomerDetailService service;

	@PayloadRoot(namespace = "http://soap.webservice.com", localPart = "GetCustomerDetailRequest")
	@ResponsePayload
	public GetCustomerDetailResponse processCustomerDeteilRequest(@RequestPayload GetCustomerDetailRequest req)
			throws Exception {
		
		Customer response = service.findById(req.getId());
		if (response == null) {
			// throw new Exception("Id Invalido" + req.getId());
			throw new CustomerNotFoundException("Id Invalido" + req.getId());
		}
		return convertToGetCustomerDetailResponse(response);
	}

	private GetCustomerDetailResponse convertToGetCustomerDetailResponse(Customer customer) {
		GetCustomerDetailResponse customerDetailResponse = new GetCustomerDetailResponse();
		customerDetailResponse.setCustomerDetail(convertToCustomerDetail(customer));
		return customerDetailResponse;
	}

	public CustomerDetail convertToCustomerDetail(Customer customer) {
		CustomerDetail customerDetail = new CustomerDetail();
		customerDetail.setId(customer.getId());
		customerDetail.setName(customer.getNome());
		customerDetail.setPhone(customer.getPhone());
		customerDetail.setEmail(customer.getEmail());
		return customerDetail;
	}

	@PayloadRoot(namespace = "http://soap.webservice.com", localPart = "GetAllCustomerDetailRequest")
	@ResponsePayload
	public GetAllCustomerDetailResponse processGetAllCustomerDetailRequest(
			@RequestPayload GetAllCustomerDetailRequest req) {
		List<Customer> customers = service.findAll();
		return ConvetToAllCustomerDetailResponse(customers);
	}

	private GetAllCustomerDetailResponse ConvetToAllCustomerDetailResponse(List<Customer> customers) {
		GetAllCustomerDetailResponse response = new GetAllCustomerDetailResponse();
		customers.stream().forEach(c -> response.getCustomerDetail().add(convertToCustomerDetail(c)));
		return response;
	}

	@PayloadRoot(namespace = "http://soap.webservice.com", localPart = "DeleteCustomerRequest")
	@ResponsePayload
	public DeleteCustomerResponse deleteDeleteCustomerDetailRequest(@RequestPayload DeleteCustomerRequest req) {
		DeleteCustomerResponse resp = new DeleteCustomerResponse();
		resp.setStatus(converteStatusSoap(service.deleteById(req.getId())));
		return resp;
	}
	
	
	private com.webservice.soap.Status converteStatusSoap(Status status) {
		if(status.equals(status.FAILURE)) {
			return com.webservice.soap.Status.FAILURE;
		}
		return com.webservice.soap.Status.SUCCESS;
	} 
	
}
