package com.demo.webapideneme1.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.demo.webapideneme1.models.Media;
import com.demo.webapideneme1.models.Message;
import com.demo.webapideneme1.models.MessagePost;

@Repository
public interface MessagePostRepository extends JpaRepository<MessagePost, Long>{

	MessagePost findByMessage(Message message);

	MessagePost findByMedia(Media media);
	@Query(value="Select * from message_post where (sender_id=:id and receiver_id=:id2) or (sender_id=:id2 and receiver_id=:id) order by date asc",nativeQuery = true)
	List<MessagePost> findByTwoUsers(long id, long id2);

	

}
