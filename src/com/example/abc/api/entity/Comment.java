package com.example.abc.api.entity;

import java.util.Date;

public class Comment {
	String id;
	Date createDate;
	Date editDate;
	String text;
	String authorName;
	String authorAvatar;
//	Article articele;
	
	public String getId() {
		return id;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getAuthorAvatar() {
		return authorAvatar;
	}
	public void setAuthorAvatar(String authorAvatar) {
		this.authorAvatar = authorAvatar;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getEditDate() {
		return editDate;
	}
	public void setEditDate(Date editDate) {
		this.editDate = editDate;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
//	public Article getArticele() {
//		return articele;
//	}
//	public void setArticele(Article articele) {
//		this.articele = articele;
//	}
	
}
