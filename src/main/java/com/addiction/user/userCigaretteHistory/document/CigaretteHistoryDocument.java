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
@Document("cigarette_history")
public class CigaretteHistoryDocument {
	@Id
	private String id;
	private LocalDateTime smokeDate; // timeField: 해당 날짜 자정 (yyyyMMdd 00:00:00)
	private String month;            // yyyyMM
	private String date;             // yyyyMMdd
	private Long userId;             // metaField
	private Integer smokeCount;
	private Long avgPatienceTime;
	private List<History> history;

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class History {
		private String address;
		private LocalDateTime smokeTime;
		private Long smokePatienceTime;
	}

}
