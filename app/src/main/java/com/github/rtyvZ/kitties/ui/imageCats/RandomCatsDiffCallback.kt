import androidx.recyclerview.widget.DiffUtil
import com.github.rtyvZ.kitties.network.data.Cat

class RandomCatsDiffCallback : DiffUtil.ItemCallback<Cat>() {

    override fun areItemsTheSame(oldItem: Cat, newItem: Cat): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Cat, newItem: Cat): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: Cat, newItem: Cat): Any? {
        return super.getChangePayload(oldItem, newItem)
    }
}
