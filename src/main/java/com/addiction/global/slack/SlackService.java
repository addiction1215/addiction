package com.addiction.global.slack;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SlackService {

	private final RestClient restClient;
	private final String webhookUrl;

	public SlackService(
		@Value("${slack.webhook.url:}") String webhookUrl
	) {
		this.restClient = RestClient.create();
		this.webhookUrl = webhookUrl;
	}

	public void sendErrorNotification(Exception e, HttpServletRequest request) {
		if (webhookUrl.isBlank()) {
			return;
		}
		try {
			String message = buildErrorMessage(e, request);
			restClient.post()
				.uri(webhookUrl)
				.contentType(MediaType.APPLICATION_JSON)
				.body(Map.of("text", message))
				.retrieve()
				.toBodilessEntity();
		} catch (Exception ex) {
			log.warn("Slack 알림 전송 실패: {}", ex.getMessage());
		}
	}

	private String buildErrorMessage(Exception e, HttpServletRequest request) {
		String stackTrace = getStackTrace(e);

		return String.format(
			"*[서버 오류 발생]*\n"
				+ "• *요청*: %s %s\n"
				+ "• *에러*: %s\n"
				+ "• *메시지*: %s\n"
				+ "• *시간*: %s\n"
				+ "```%s```",
			request.getMethod(),
			request.getRequestURI(),
			e.getClass().getSimpleName(),
			e.getMessage(),
			LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
			stackTrace
		);
	}

	private String getStackTrace(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String fullTrace = sw.toString();
		String[] lines = fullTrace.split("\n");
		int maxLines = Math.min(lines.length, 15);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < maxLines; i++) {
			sb.append(lines[i]).append("\n");
		}
		return sb.toString();
	}
}
