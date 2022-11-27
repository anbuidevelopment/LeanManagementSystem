package allianceone.prog.leanmanagementsystem.repository

import allianceone.prog.leanmanagementsystem.data.model.DriverConfigData
import allianceone.prog.leanmanagementsystem.data.model.PolicyData
import allianceone.prog.leanmanagementsystem.data.model.PolicyUser
import allianceone.prog.leanmanagementsystem.data.sql.JdbcController
import allianceone.prog.leanmanagementsystem.service.IQuerySQLService
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement

class QuerySQLRepository(private val driverConfigData: DriverConfigData = DriverConfigData()) : IQuerySQLService {
    private val jdbcController = JdbcController()

    override fun checkValidStaffId(staffId: String): Boolean {
        var isValidStaffId = false
        try {
            val conn: Connection? = jdbcController.getConnectionToSQL(driverConfigData)
            val statement = conn?.createStatement()
            val querySql = "SELECT TOP 1 id_staff " +
                    "FROM DtradeProduction.dbo.HT_ESCALATION_POLICY_USER " +
                    "WHERE id_staff = '${staffId}'"
            val resultSet = statement!!.executeQuery(querySql)
            while (resultSet.next()) {
                if (resultSet.getString("id_staff") != null) {
                    isValidStaffId = true
                }
            }
            conn.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return isValidStaffId
    }

    override fun checkOnConnection(): Boolean {
        var isConnected = false
        try {
            val conn: Connection? = jdbcController.getConnectionToSQL(driverConfigData)
            val statement = conn?.createStatement()
            val querySql = "SELECT TOP 1 id_staff " +
                    "FROM DtradeProduction.dbo.HT_ESCALATION_POLICY_USER"
            val resultSet = statement!!.executeQuery(querySql)
            while (resultSet.next()) {
                if (resultSet.getString("id_staff") != null) {
                    isConnected = true
                }
            }
            conn.close()
        } catch (e: Exception) {
            isConnected = false
            e.printStackTrace()
        }
        return isConnected
    }

    override fun findPolicyUserByStaffId(staffId: String): List<PolicyUser> {
        val listUserPolicy = mutableListOf<PolicyUser>()
        try {
            val conn: Connection? = jdbcController.getConnectionToSQL(driverConfigData)
            val statement: Statement =
                conn?.createStatement() ?: throw Exception("Lỗi kết nối với SQL")
            val sql =
                "Select id_staff, Dept, IDLv, Group_Line  " +
                        "from DtradeProduction.dbo.HT_ESCALATION_POLICY_USER WHERE id_staff = '${staffId}'"
            val rs: ResultSet = statement.executeQuery(sql)
            while (rs.next()) {
                listUserPolicy.add(
                    PolicyUser(
                        rs.getString("id_staff"),
                        rs.getString("Dept"),
                        rs.getString("IDLv"),
                        rs.getString("Group_Line")
                    )
                )
            }
            conn.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return listUserPolicy
    }

    override fun findAllPolicyData(): List<PolicyData> {
        val listDataPolicy = mutableListOf<PolicyData>()

        try {
            val conn: Connection? = jdbcController.getConnectionToSQL(driverConfigData)
            val statement: Statement =
                conn?.createStatement() ?: throw Exception("Lỗi kết nối với SQL")
            val sql =
                "Select Facline, Dept, IDLv, DefName  " +
                        "from DtradeProduction.dbo.HT_ESCALATION_POLICY_DATA"
            val rs: ResultSet = statement.executeQuery(sql)
            while (rs.next()) {
                listDataPolicy.add(
                    PolicyData(
                        rs.getString("Facline"),
                        rs.getString("DefName"),
                        rs.getString("Dept"),
                        rs.getString("IDLv")
                    )
                )
            }
            conn.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return listDataPolicy

    }
}