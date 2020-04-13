import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.myself.todo.view.fragments.EventsFragment
import com.myself.todo.view.fragments.PicturesFragment
import com.myself.todo.view.fragments.ProfileFragment

class MainPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {


    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> EventsFragment()
            1 -> PicturesFragment()
            else -> ProfileFragment()
        }
    }
}