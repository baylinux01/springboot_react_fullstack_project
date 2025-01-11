package com.demo.webapideneme1.services;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.webapideneme1.models.ConnectionRequest;
import com.demo.webapideneme1.models.User;
import com.demo.webapideneme1.repositories.ConnectionRequestRepository;
import com.demo.webapideneme1.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ConnectionRequestService {
 
	ConnectionRequestRepository connectionRequestRepository;
	UserRepository userRepository;
	
	@Autowired
	public ConnectionRequestService(ConnectionRequestRepository connectionRequestRepository, UserRepository userRepository) {
		super();
		this.connectionRequestRepository = connectionRequestRepository;
		this.userRepository = userRepository;
	}
	
	public List<ConnectionRequest> getAllConnectionRequests() {
		// TODO Auto-generated method stub
		return connectionRequestRepository.findAll();
	}
	
	public String createConnectionRequest(HttpServletRequest request, Long connectionRequestReceiverId) {
		
		List<ConnectionRequest> allConReqs=connectionRequestRepository.findAll();
		/*jwt olmadan requestten kullanıcı adını alma kodları başlangıcı*/		
		Principal pl=request.getUserPrincipal();
		String username=pl.getName();
		/*jwt olmadan requestten kullanıcı adını alma kodları sonu*/
		User connectionRequestSender=userRepository.findByUsername(username);
		User connectionRequestReceiver=userRepository.findById(connectionRequestReceiverId).orElse(null);
		if(connectionRequestReceiver.getBlockedUsers().contains(connectionRequestSender)
				||connectionRequestSender.getBlockedUsers().contains(connectionRequestReceiver))
			return "conreq cannot be created because of a ban";
		if(connectionRequestReceiver.getConnections().contains(connectionRequestSender)
				||connectionRequestSender.getConnections().contains(connectionRequestReceiver))
			return "there is already a connection between these two users";
		if(connectionRequestSender==connectionRequestReceiver) return "sender and receiver is the same";
		if(allConReqs!=null&&allConReqs.size()>0)
		{
			for(ConnectionRequest conReq:allConReqs)
			{
				if((conReq.getConnectionRequestSender()==connectionRequestSender
						&&conReq.getConnectionRequestReceiver()==connectionRequestReceiver)
						||(conReq.getConnectionRequestSender()==connectionRequestReceiver
								&&conReq.getConnectionRequestReceiver()==connectionRequestSender))
				{
					return "conreq already exists";
				}
			}
		}
			ConnectionRequest cRequest=new ConnectionRequest();
			cRequest.setConnectionRequestSender(connectionRequestSender);
			cRequest.setConnectionRequestReceiver(connectionRequestReceiver);
			try {
			connectionRequestRepository.save(cRequest);
			return "conreq succesffully saved";
			}catch(Exception ex){
				return ex.getMessage();
			}
	}

	public void saveAll(List<ConnectionRequest> conreqs) {
		//connectionRequestRepository.deleteAll();
		connectionRequestRepository.saveAll(conreqs);
		
	}

	public void removeAll(List<ConnectionRequest> conreqstoberemoved) {
		connectionRequestRepository.deleteAll(conreqstoberemoved);
		
	}

	public void removeConnectionRequest(ConnectionRequest connectionRequest) {
		connectionRequestRepository.delete(connectionRequest);
		
	}

	

	public ConnectionRequest getAConnectionRequestWhoseSenderIsCurrentUser(HttpServletRequest request,
			Long connectionRequestReceiverId) {
		// TODO Auto-generated method stub
				/*jwt olmadan requestten kullanıcı adını alma kodları başlangıcı*/		
				Principal pl=request.getUserPrincipal();
				String username=pl.getName();
				/*jwt olmadan requestten kullanıcı adını alma kodları sonu*/
				User user=userRepository.findByUsername(username);
				User user2=userRepository.findById(connectionRequestReceiverId).orElse(null);
				ConnectionRequest conreq=null;
				if(user!=null&& user2!=null)
				{
					conreq=connectionRequestRepository.findByConnectionRequestSenderAndConnectionRequestReceiver(user,user2);
				}
				
				return conreq;
	}

	public ConnectionRequest getAConnectionRequestWhoseReceiverIsCurrentUser(HttpServletRequest request,
			Long connectionRequestSenderId) {
		// TODO Auto-generated method stub
				/*jwt olmadan requestten kullanıcı adını alma kodları başlangıcı*/		
				Principal pl=request.getUserPrincipal();
				String username=pl.getName();
				/*jwt olmadan requestten kullanıcı adını alma kodları sonu*/
				User user=userRepository.findByUsername(username);
				User user2=userRepository.findById(connectionRequestSenderId).orElse(null);
				ConnectionRequest conreq=null;
				if(user!=null&& user2!=null)
				{
					conreq=connectionRequestRepository.findByConnectionRequestSenderAndConnectionRequestReceiver(user2,user);
				}
				
				return conreq;
	}

	public String refuseConnectionRequest(HttpServletRequest request, Long connectionRequestSenderId) {
		// TODO Auto-generated method stub
		/*jwt olmadan requestten kullanıcı adını alma kodları başlangıcı*/		
		Principal pl=request.getUserPrincipal();
		String username=pl.getName();
		/*jwt olmadan requestten kullanıcı adını alma kodları sonu*/
		User user=userRepository.findByUsername(username);
		User user2=userRepository.findById(connectionRequestSenderId).orElse(null);
		ConnectionRequest conreq=null;
		if(user!=null&& user2!=null)
		{
			conreq=connectionRequestRepository.findByConnectionRequestSenderAndConnectionRequestReceiver(user2,user);
			connectionRequestRepository.delete(conreq);
			return "successfully refused";
		}
		
		return "not found the request to refuse";
	}

	public String cancelConnectionRequest(HttpServletRequest request, Long connectionRequestReceiverId) {
		// TODO Auto-generated method stub
		/*jwt olmadan requestten kullanıcı adını alma kodları başlangıcı*/		
		Principal pl=request.getUserPrincipal();
		String username=pl.getName();
		/*jwt olmadan requestten kullanıcı adını alma kodları sonu*/
		User user=userRepository.findByUsername(username);
		User user2=userRepository.findById(connectionRequestReceiverId).orElse(null);
		ConnectionRequest conreq=null;
		if(user!=null&& user2!=null)
		{
			conreq=connectionRequestRepository.findByConnectionRequestSenderAndConnectionRequestReceiver(user,user2);
			connectionRequestRepository.delete(conreq);
			return "successfully canceled";
		}
		
		return "not found the request to refuse";
	}
	
	
 
}
