package com.demo.webapideneme1.models;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;


@Entity
public class Message {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne
	private User messageSender;
	
	@ManyToOne
	private User messageReceiver;
	//@JsonFormat(shape=JsonFormat.Shape.STRING,pattern="dd-MM-yyyy")
	private LocalDateTime messageDate=LocalDateTime.now(ZoneId.of("Turkey"));
	//@JsonFormat(shape=JsonFormat.Shape.STRING,pattern="dd-MM-yyyy")
	private LocalDateTime messageEditDate=LocalDateTime.now(ZoneId.of("Turkey"));
	private String messageContent;
	@ManyToOne
	private Media quotedMedia;
	@ManyToOne
	private Message quotedMessage;
	private boolean read=false;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public User getMessageSender() {
		return messageSender;
	}
	public void setMessageSender(User messageSender) {
		this.messageSender = messageSender;
	}
	public User getMessageReceiver() {
		return messageReceiver;
	}
	public void setMessageReceiver(User messageReceiver) {
		this.messageReceiver = messageReceiver;
	}
	
	public LocalDateTime getMessageDate() {
		return messageDate;
	}
	public void setMessageDate(LocalDateTime messageDate) {
		this.messageDate = messageDate;
	}
	public LocalDateTime getMessageEditDate() {
		return messageEditDate;
	}
	public void setMessageEditDate(LocalDateTime messageEditDate) {
		this.messageEditDate = messageEditDate;
	}
	public String getMessageContent() {
		return messageContent;
	}
	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}
	
	public Media getQuotedMedia() {
		return quotedMedia;
	}
	public void setQuotedMedia(Media quotedMedia) {
		this.quotedMedia = quotedMedia;
	}
	public Message getQuotedMessage() {
		return quotedMessage;
	}
	public void setQuotedMessage(Message quotedMessage) {
		this.quotedMessage = quotedMessage;
	}
	public boolean isRead() {
		return read;
	}
	public void setRead(boolean read) {
		this.read = read;
	}
	
	

	
}
