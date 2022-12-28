package allianceone.prog.leanmanagementsystem.ui.home

import allianceone.prog.leanmanagementsystem.core.convertLongToTime
import allianceone.prog.leanmanagementsystem.data.model.Notification
import allianceone.prog.leanmanagementsystem.databinding.FragmentListNotificationBinding
import allianceone.prog.leanmanagementsystem.databinding.FragmentStaffListNotificationBinding
import allianceone.prog.leanmanagementsystem.repository.QuerySQLRepository
import allianceone.prog.leanmanagementsystem.utils.SharedPreferenceHelper
import android.graphics.ColorSpace.Model
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class StaffListNotificationFragment : Fragment() {

    private var _binding: FragmentStaffListNotificationBinding? = null
    private val binding get() = _binding!!
    private val configData = SharedPreferenceHelper.readConfigConnection()


    private val listNotification = mutableListOf<Notification>()

    private lateinit var adapter: NotifyItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStaffListNotificationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = sdf.format(Date())

        configData?.let {
            val querySQLRepository = QuerySQLRepository(configData)
            listNotification.addAll(
                querySQLRepository.findNotificationByUser(
                    configData.staffId,
                    currentDate
                )
            )
            adapter = NotifyItemAdapter(listNotification)

            binding.rcvNotification.adapter = adapter
        }
    }


    private fun attachAdapter(list: List<Notification>) {
        val searchAdapter = NotifyItemAdapter(list)
        binding.rcvNotification.adapter = searchAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}