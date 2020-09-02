import androidx.recyclerview.widget.DiffUtil
import com.github.rtyvZ.kitties.common.models.Cat

class RandomCatsDiffCallback : DiffUtil.ItemCallback<Cat>() {

    override fun areItemsTheSame(oldItem: Cat, newItem: Cat): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Cat, newItem: Cat): Boolean {
        return oldItem.id == newItem.id
                && (oldItem.choice != newItem.choice || oldItem.voteId != newItem.voteId)
    }
}
