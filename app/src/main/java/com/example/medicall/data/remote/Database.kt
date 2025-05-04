package com.example.medicall.data.remote

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object Database {
    private const val URL = "jdbc:mysql://127.0.0.1:3306/medicall"
    private const val USER = "root"
    private const val PASSWORD = "bouchra"

    fun getConnection(): Connection? {
        return try {
            Class.forName("com.mysql.cj.jdbc.Driver")
            DriverManager.getConnection(URL, USER, PASSWORD)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            null
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
    }
}
