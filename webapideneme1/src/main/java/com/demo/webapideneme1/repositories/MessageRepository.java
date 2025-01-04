package com.demo.webapideneme1.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.demo.webapideneme1.models.Message;
import com.demo.webapideneme1.models.User;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long>{

	@Query(value="select * from message m where (m.message_sender_id=:user1Id and m.message_receiver_id=:user2Id)"
			+ "or (m.message_sender_id=:user2Id and m.message_receiver_id=:user1Id) order by message_date",nativeQuery=true)
	List<Message> findByMessageSenderandMessageReceiver(Long user1Id, Long user2Id);

	List<Message> findByQuotedMessage(Message message);

	
	

}
