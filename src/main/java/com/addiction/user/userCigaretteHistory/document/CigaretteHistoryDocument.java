package com.addiction.user.userCigaretteHistory.document;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "cigarette_history")
public class CigaretteHistoryDocument {
	@Id
	private String id;
	private String date;
	private int userId;
	private List<History> history;

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class History {
		private String address;
		private LocalDateTime smokeTime;
		private long smokePatienceTime;
	}
}
