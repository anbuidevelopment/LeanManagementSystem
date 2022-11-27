package allianceone.prog.leanmanagementsystem.data.sql

import allianceone.prog.leanmanagementsystem.data.model.DriverConfigData
import java.sql.Connection

class JdbcController {
    private val serviceConnection = JdbcServiceConnection()

    fun getConnectionToSQL(driverConfigData: DriverConfigData): Connection? {
        return serviceConnection.getConnection(driverConfigData)
    }
}