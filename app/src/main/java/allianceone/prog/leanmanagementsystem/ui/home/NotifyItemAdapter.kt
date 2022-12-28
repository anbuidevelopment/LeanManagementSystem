package allianceone.prog.leanmanagementsystem.ui.home

import allianceone.prog.leanmanagementsystem.data.model.Notification
import allianceone.prog.leanmanagementsystem.databinding.ItemNotificationBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class NotifyItemAdapter(private val notifications: List<Notification>) :
    RecyclerView.Adapter<NotifyItemAdapter.NotifyItemViewHolder>() {

    class NotifyItemViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Notification) {
            binding.apply {
                txtContent.text = item.defectVN
                txtDateCreated.text = item.sysCreatedDate
                txtObjectType.text = String.format("%s - %s", item.factory, item.facLine)

//                when (item.isRead) {
//                    true -> itemLayout.setBackgroundColor(Color.parseColor("#FFFFFF"))
//                    false -> itemLayout.setBackgroundColor(Color.parseColor("#E8F2FE"))
//                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifyItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNotificationBinding.inflate(inflater, parent, false)
        return NotifyItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    override fun onBindViewHolder(holder: NotifyItemViewHolder, position: Int) {
        holder.bind(notifications[position])
    }


}
