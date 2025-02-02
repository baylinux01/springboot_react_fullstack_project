package com.demo.webapideneme1.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;


@Entity
public class Comment {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@ManyToOne(fetch=FetchType.EAGER)
	private User owner;
	private String content;
	@ManyToOne
	private Media quotedMedia;
	@ManyToOne
	private Comment quotedComment;
//	@OneToMany
//	private List<Comment> quotingComments;
	@ManyToOne
	private Group group;
	//@JsonFormat(shape=JsonFormat.Shape.STRING,pattern="dd-MM-yyyy")
	private LocalDateTime commentDate=LocalDateTime.now(ZoneId.of("Turkey"));
	//@JsonFormat(shape=JsonFormat.Shape.STRING,pattern="dd-MM-yyyy")
	private LocalDateTime commentEditDate=LocalDateTime.now(ZoneId.of("Turkey"));
	
	
	
	public Comment() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public User getOwner() {
		return owner;
	}



	public void setOwner(User owner) {
		this.owner = owner;
	}



	public String getContent() {
		return content;
	}



	public void setContent(String content) {
		this.content = content;
	}

	

	public Media getQuotedMedia() {
		return quotedMedia;
	}



	public void setQuotedMedia(Media quotedMedia) {
		this.quotedMedia = quotedMedia;
	}



	public Comment getQuotedComment() {
		return quotedComment;
	}



	public void setQuotedComment(Comment quotedComment) {
		this.quotedComment = quotedComment;
	}



	public Group getGroup() {
		return group;
	}



	public void setGroup(Group group) {
		this.group = group;
	}



	public LocalDateTime getCommentDate() {
		return commentDate;
	}



	public void setCommentDate(LocalDateTime commentDate) {
		this.commentDate = commentDate;
	}



	public LocalDateTime getCommentEditDate() {
		return commentEditDate;
	}



	public void setCommentEditDate(LocalDateTime commentEditDate) {
		this.commentEditDate = commentEditDate;
	}

	




	
	
	
	
	
}
