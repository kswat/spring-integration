package com.dsl.demo;

import org.springframework.stereotype.Service;

@Service
public class MessagePrinter {
	public String load(org.springframework.util.LinkedCaseInsensitiveMap<String> m) {
		String data = (String)m.get("msg");
		System.out.println("Message from DB - "+ data);
		return data;
	}
}
