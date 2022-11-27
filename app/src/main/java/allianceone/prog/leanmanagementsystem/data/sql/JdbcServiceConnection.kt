package allianceone.prog.leanmanagementsystem.data.sql

import allianceone.prog.leanmanagementsystem.data.model.DriverConfigData
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException


class JdbcServiceConnection {
    fun getConnection(config: DriverConfigData): Connection? {
        val policy = ThreadPolicy.Builder()
            .permitAll().build()
        StrictMode.setThreadPolicy(policy)
        var conn: Connection? = null

//        val config = ConnectionData("192.168.1.245", "prog2", "iop", "DtradeProduction", "1433")
        try {
            val sConnURL = (
                    "jdbc:jtds:sqlserver://${config.serverName}:${config.port};" +
                            "databaseName=${config.database};" +
                            "user=${config.username};" +
                            "password=${config.password};")
            conn = DriverManager.getConnection(sConnURL)
        } catch (se: SQLException) {
            se.printStackTrace()
            Log.e("ERROR", se.message.toString())
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            Log.e("ERROR", e.message.toString())
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("ERROR", e.message.toString())
        }
        return conn
    }
}