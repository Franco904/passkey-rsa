package database

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
        connection?.createStatement()?.executeUpdate("DROP TABLE users;")
        connection?.createStatement()?.executeUpdate(
            """
            CREATE TABLE IF NOT EXISTS users(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                displayName TEXT NOT NULL,
                email TEXT NOT NULL
            );
            """
        )
    }

    fun insert(sql: String) = connection?.createStatement()?.execute(sql)

    companion object {
        private const val DATABASE_FILE_PATH = "src/main/resources/database/passkey-rsa.db"
    }
}
