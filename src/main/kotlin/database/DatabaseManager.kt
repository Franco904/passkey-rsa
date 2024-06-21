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
//        connection?.createStatement()?.executeUpdate(
//            "DROP TABLE ${User.TABLE};"
//        )
        connection?.createStatement()?.executeUpdate(
            """
            CREATE TABLE IF NOT EXISTS ${User.TABLE}(
                ${User.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${User.DISPLAY_NAME} TEXT NOT NULL,
                ${User.EMAIL} TEXT NOT NULL
            );
            """
        )
    }

    fun insert(sql: String) = connection?.createStatement()?.execute(sql)

    fun findById(query: String): User? {
        return connection?.prepareStatement(query)?.use {
            val result = it.executeQuery()

            User(
                id = result.getString(1).toLongOrNull(),
                displayName = result.getString(2),
                email = result.getString(3)
            )
        }
    }

    companion object {
        private const val DATABASE_FILE_PATH = "src/main/resources/database/passkey-rsa.db"
    }
}
