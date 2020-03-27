import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.myself.todo.view.fragments.EventsFragment
import com.myself.todo.view.fragments.PicturesFragment
import com.myself.todo.view.fragments.ProfileFragment

class MainPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> EventsFragment()
            1 -> PicturesFragment()
            else -> ProfileFragment()
        }


    }

    override fun getCount(): Int {
        return 3//three fragments
    }
}