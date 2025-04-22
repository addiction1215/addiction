package com.addiction.global.config.p6spy

import com.p6spy.engine.logging.Category
import com.p6spy.engine.spy.appender.MessageFormattingStrategy
import org.hibernate.engine.jdbc.internal.FormatStyle
import java.util.Locale

class P6SpyFormatter : MessageFormattingStrategy {

    companion object {
        private const val NEW_LINE = "\n"
        private const val TAP = "\t"
        private const val CREATE = "create"
        private const val ALTER = "alter"
        private const val DROP = "drop"
        private const val COMMENT = "comment"
    }

    override fun formatMessage(
        connectionId: Int,
        now: String,
        elapsed: Long,
        category: String,
        prepared: String?,
        sql: String,
        url: String
    ): String {
        if (sql.trim().isEmpty()) {
            return formatByCommand(category)
        }
        return formatBySql(sql, category) + getAdditionalMessages(elapsed)
    }

    private fun formatByCommand(category: String): String {
        return NEW_LINE +
                "Execute Command : $NEW_LINE$NEW_LINE" +
                "$TAP$category$NEW_LINE$NEW_LINE" +
                "----------------------------------------------------------------------------------------------------"
    }

    private fun formatBySql(sql: String, category: String): String {
        return if (isStatementDDL(sql, category)) {
            NEW_LINE +
                    "Execute DDL : $NEW_LINE" +
                    FormatStyle.DDL.formatter.format(sql)
        } else {
            NEW_LINE +
                    "Execute DML : $NEW_LINE" +
                    FormatStyle.BASIC.formatter.format(sql)
        }
    }

    private fun getAdditionalMessages(elapsed: Long): String {
        return "$NEW_LINE$NEW_LINE" +
                String.format("Execution Time: %s ms", elapsed) +
                NEW_LINE +
                "----------------------------------------------------------------------------------------------------"
    }

    private fun isStatementDDL(sql: String, category: String): Boolean {
        return isStatement(category) && isDDL(sql.trim().lowercase(Locale.ROOT))
    }

    private fun isStatement(category: String): Boolean {
        return Category.STATEMENT.name == category
    }

    private fun isDDL(lowerSql: String): Boolean {
        return lowerSql.startsWith(CREATE) ||
                lowerSql.startsWith(ALTER) ||
                lowerSql.startsWith(DROP) ||
                lowerSql.startsWith(COMMENT)
    }
}
