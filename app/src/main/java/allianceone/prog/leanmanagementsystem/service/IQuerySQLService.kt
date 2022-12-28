package allianceone.prog.leanmanagementsystem.service

import allianceone.prog.leanmanagementsystem.data.model.Notification
import allianceone.prog.leanmanagementsystem.data.model.PolicyData
import allianceone.prog.leanmanagementsystem.data.model.PolicyUser

interface IQuerySQLService {
    fun checkValidStaffId(staffId: String): Boolean

    fun checkOnConnection(): Boolean

    fun findPolicyUserByStaffId(staffId: String): List<PolicyUser>

    fun findAllPolicyData(): List<PolicyData>

    fun findAllNotification(date: String): List<Notification>

    fun findNotificationByUser(staffId: String, date: String): List<Notification>
}