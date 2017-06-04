package com.rxqp.model;

public class JsApiTicket {

	// 公众号用于调用微信JS接口的临时票据
	private String tickte;
	// 有效时间
	private int expiresIn;

	public String getTickte() {
		return tickte;
	}

	public void setTickte(String tickte) {
		this.tickte = tickte;
	}

	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

}
