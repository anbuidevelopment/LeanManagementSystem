package allianceone.prog.leanmanagementsystem.ui.home

import allianceone.prog.leanmanagementsystem.core.convertLongToTime
import allianceone.prog.leanmanagementsystem.data.model.Notification
import allianceone.prog.leanmanagementsystem.databinding.FragmentListNotificationBinding
import allianceone.prog.leanmanagementsystem.repository.QuerySQLRepository
import allianceone.prog.leanmanagementsystem.utils.SharedPreferenceHelper
import android.graphics.ColorSpace.Model
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ListNotificationFragment : Fragment() {

    private var _binding: FragmentListNotificationBinding? = null
    private val binding get() = _binding!!
    private val configData = SharedPreferenceHelper.readConfigConnection()


    private val listNotification = mutableListOf<Notification>()

    private lateinit var adapter: NotifyItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListNotificationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = sdf.format(Date())
        binding.txtDate.text = currentDate
        configData?.let {
            val querySQLRepository = QuerySQLRepository(configData)
            listNotification.addAll(querySQLRepository.findAllNotification(currentDate))
            adapter = NotifyItemAdapter(listNotification)

            binding.rcvNotification.adapter = adapter

            binding.edtSearch.doOnTextChanged { text, _, _, _ ->
                val query = text.toString().toLowerCase(Locale.getDefault())
                filterWithQuery(query)
            }

            binding.dateRangePicker.setOnClickListener {
                openDatePicker()
            }
        }
    }

    private fun openDatePicker() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        datePicker.addOnPositiveButtonClickListener {
            binding.txtDate.text = convertLongToTime(it, "yyyy-MM-dd")
            configData?.let {
                val querySQLRepository = QuerySQLRepository(configData)
                listNotification.clear()
                listNotification.addAll((querySQLRepository.findAllNotification(binding.txtDate.text.toString())))
                adapter.notifyDataSetChanged()
            }
        }

        datePicker.show(parentFragmentManager, null)
    }

    private fun onQueryChanged(filterQuery: String): List<Notification> {
        val filteredList = ArrayList<Notification>()
        for (currentNotification in listNotification) {
            if (currentNotification.facLine?.lowercase(Locale.getDefault())!!
                    .contains(filterQuery)
            ) {
                filteredList.add(currentNotification)
            }
        }
        return filteredList
    }

    private fun filterWithQuery(query: String) {
        if (query.isNotEmpty()) {
            val filteredList: List<Notification> = onQueryChanged(query)
            attachAdapter(filteredList)
        } else if (query.isEmpty()) {
            attachAdapter(listNotification)
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