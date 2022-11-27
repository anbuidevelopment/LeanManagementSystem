package allianceone.prog.leanmanagementsystem.core

import allianceone.prog.leanmanagementsystem.repository.QuerySQLRepository
import allianceone.prog.leanmanagementsystem.utils.SharedPreferenceHelper
import android.content.Context
import androidx.work.*
import java.util.UUID
import java.util.concurrent.TimeUnit


class NotifyWorker(private val context: Context, params: WorkerParameters) :
    Worker(context, params) {
    override fun doWork(): Result {
        setupDataToSendNotification()
        val myWorkRequest = OneTimeWorkRequestBuilder<NotifyWorker>()
            .setInitialDelay(5, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(applicationContext)
            .enqueueUniqueWork("send", ExistingWorkPolicy.REPLACE, myWorkRequest)
        return Result.success()
    }

    private fun setupDataToSendNotification() {
        val idStaff = SharedPreferenceHelper.readStaffId().toString()
        val configData = SharedPreferenceHelper.readConfigConnection()
        val querySQLRepository = QuerySQLRepository(configData!!)
        val listPolicyData = querySQLRepository.findAllPolicyData()
        val listPolicyUser = querySQLRepository.findPolicyUserByStaffId(idStaff)
        listPolicyUser.forEach { user ->
            listPolicyData.forEach { data ->
                if (data.dept == user.dept
                    && data.idLv == user.idLv
                    && user.groupLine!!.contains(data.facLine.toString())
                ) {
                    NotificationHelper(context)
                        .sendNotification(
                            tag = UUID.randomUUID().toString(),
                            content = data.defName.toString(),
                            title = "${data.facLine} - ${user.idStaff}"
                        )
                }
            }
        }
    }
}