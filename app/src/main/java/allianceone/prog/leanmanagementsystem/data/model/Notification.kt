package allianceone.prog.leanmanagementsystem.data.model

data class Notification(
    val factory: String?,
    val facLine: String?,
    val defectVN: String?,
    val dept: String?,
    val sysCreatedDate: String?,
    var isRead: Boolean = false
)
