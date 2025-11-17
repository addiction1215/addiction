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
@Document  // collection 이름을 동적으로 지정하기 위해 제거
public class CigaretteHistoryDocument {
	@Id
	private String id;
	private String month;
	private String date;
	private Long userId;
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
