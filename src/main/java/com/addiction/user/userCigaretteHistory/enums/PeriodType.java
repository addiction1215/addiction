package com.addiction.user.userCigaretteHistory.enums;

import java.time.LocalDate;

public enum PeriodType {
	WEEKLY {
		@Override
		public LocalDate calculateStartDate(LocalDate endDate) {
			return endDate.minusWeeks(1);
		}
	},
	MONTHLY {
		@Override
		public LocalDate calculateStartDate(LocalDate endDate) {
			return endDate.minusMonths(1);
		}
	},
	SIXMONTHLY {
		@Override
		public LocalDate calculateStartDate(LocalDate endDate) {
			return endDate.minusMonths(6);
		}
	},
	YEARLY {
		@Override
		public LocalDate calculateStartDate(LocalDate endDate) {
			return endDate.minusYears(1);
		}
	};

	public abstract LocalDate calculateStartDate(LocalDate endDate);
}
