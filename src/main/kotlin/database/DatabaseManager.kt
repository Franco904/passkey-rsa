package database

import database.entities.User
import java.sql.Connection
import java.sql.DriverManager

class DatabaseManager {
    private var connection: Connection? = null

    fun connect() {
        connection = DriverManager.getConnection("jdbc:sqlite:$DATABASE_FILE_PATH")

        createTables()
    }

    fun disconnect() {
        connection?.close()
    }

    private fun createTables() {
        connection?.createStatement()?.executeUpdate(
            "DROP TABLE ${User.TABLE};"
        )
        connection?.createStatement()?.executeUpdate(
            """
            CREATE TABLE IF NOT EXISTS ${User.TABLE}(
                ${User.ID} TEXT PRIMARY KEY,
                ${User.DISPLAY_NAME} TEXT NOT NULL,
                ${User.EMAIL} TEXT NOT NULL,
                ${User.CHALLENGE_BUFFER} TEXT NOT NULL,
                ${User.CHALLENGE_VERIFICATION} TEXT NOT NULL
            );
            """
        )
    }

    fun insert(sql: String) = connection?.createStatement()?.execute(sql)

    fun findSingle(
        query: String,
        args: List<Any> = emptyList(),
    ): User? {
        return connection?.prepareStatement(query)?.use { statement ->
            args.forEachIndexed { index, param ->
                statement.setObject(index + 1, param)
            }

            val result = statement.executeQuery()

            val userId = result.getString(1) ?: return null
            User(
                id = userId,
                displayName = result.getString(2),
                email = result.getString(3),
                challengeBuffer = result.getString(4),
                challengeVerification = result.getString(5),
            )
        }
    }

    companion object {
        private const val DATABASE_FILE_PATH = "src/main/resources/database/passkey-rsa.db"
    }
}
