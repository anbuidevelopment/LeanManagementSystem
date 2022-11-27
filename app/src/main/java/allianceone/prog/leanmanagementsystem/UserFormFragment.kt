package allianceone.prog.leanmanagementsystem

import allianceone.prog.leanmanagementsystem.core.*
import allianceone.prog.leanmanagementsystem.data.model.DriverConfigData
import allianceone.prog.leanmanagementsystem.databinding.FragmentUserFormBinding
import allianceone.prog.leanmanagementsystem.repository.QuerySQLRepository
import allianceone.prog.leanmanagementsystem.utils.SharedPreferenceHelper
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager


class UserFormFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentUserFormBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserFormBinding.inflate(inflater, container, false)
        initViewBinding()
        fillLocalData()
        return binding.root
    }

    private fun fillLocalData() {
        binding.apply {
            val config = SharedPreferenceHelper.readConfigConnection()
            val staffId = SharedPreferenceHelper.readStaffId()

            config?.let {
                edtServerHost.setText(it.serverName)
                edtServerPort.setText(it.port)
                edtUsername.setText("*")
                edtPassword.setText("*")
                edtDatabase.setText(it.database)
                txtRunning.text = "Connect to DB successful. Enter you staff id to run."
            }

            staffId?.let {
                edtStaffId.setText(it)
                txtRunning.text = "Automatic notification mode is running."
            }
        }
    }

    private fun initViewBinding() {
        binding.apply {
            btnVerify.setOnClickListener(this@UserFormFragment)
            textInputLayoutStaffId.markRequiredInRed()
            textInputLayoutServerName.markRequiredInRed()
            textInputLayoutServerPort.markRequiredInRed()
            textInputLayoutUsername.markRequiredInRed()
            textInputLayoutPassword.markRequiredInRed()
            textInputLayoutDatabase.markRequiredInRed()
        }
    }

    private fun setInitValidateForm() {
        binding.apply {
            if (edtStaffId.text.isNullOrEmpty()) {
                textInputLayoutStaffId.error = "Enter your staff id"
                edtStaffId.setOnErrorDisableAfterTextChanged(textInputLayoutStaffId)
                return
            }

            if (edtServerHost.text.isNullOrEmpty()) {
                textInputLayoutServerName.error = "Enter your server/host"
                edtServerHost.setOnErrorDisableAfterTextChanged(textInputLayoutServerName)
                return
            }

            if (edtServerPort.text.isNullOrEmpty()) {
                textInputLayoutServerPort.error = "Enter your port"
                edtServerPort.setOnErrorDisableAfterTextChanged(textInputLayoutServerPort)
                return
            }

            if (edtUsername.text.isNullOrEmpty()) {
                textInputLayoutUsername.error = "Enter your username"
                edtUsername.setOnErrorDisableAfterTextChanged(textInputLayoutUsername)
                return
            }

            if (edtPassword.text.isNullOrEmpty()) {
                textInputLayoutPassword.error = "Enter your password"
                edtPassword.setOnErrorDisableAfterTextChanged(textInputLayoutPassword)
                return
            }

            if (edtDatabase.text.isNullOrEmpty()) {
                textInputLayoutDatabase.error = "Enter your database"
                edtDatabase.setOnErrorDisableAfterTextChanged(textInputLayoutDatabase)
                return
            }
        }
    }

    private fun createWorkRequest() {
        val myWorkRequest = OneTimeWorkRequestBuilder<NotifyWorker>()
            .build()
        WorkManager.getInstance(activity!!)
            .enqueueUniqueWork("send", ExistingWorkPolicy.REPLACE, myWorkRequest)
    }

    override fun onClick(p0: View?) {
        binding.apply {
            when (p0?.id) {
                R.id.btn_verify -> {
                    setInitValidateForm()
                    val config = DriverConfigData(
                        serverName = edtServerHost.getValue(),
                        port = edtServerPort.getValue(),
                        username = edtUsername.getValue(),
                        password = edtPassword.getValue(),
                        database = edtDatabase.getValue()
                    )
                    val querySQLRepository = QuerySQLRepository(config)
                    val isConnectedSQL = querySQLRepository.checkOnConnection()

                    if (isConnectedSQL) {
                        txtRunning.reveal()
                        SharedPreferenceHelper.writeConfigConnection(config)
                        txtRunning.text = "Connect to DB successful. Enter you staff id to run"
                        val isValidStaff =
                            querySQLRepository.checkValidStaffId(edtStaffId.getValue())
                        if (isValidStaff) {
                            SharedPreferenceHelper.writeStaffId(edtStaffId.getValue())
                            txtRunning.text = "Automatic notification mode is running."
                            createWorkRequest()
                            activity?.showToast("Save successful. You can close app now.")
                        } else {
                            edtStaffId.error = "Your staff id invalid"
                        }

                        txtError.gone()
                    } else {
                        txtRunning.gone()
                        txtError.reveal()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}