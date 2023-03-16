package uz.gita.myfirstmapproject.precentation.viewModels.impl
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.myfirstmapproject.domein.repository.MapRepo
import uz.gita.myfirstmapproject.precentation.viewModels.FindCurrentLocationViewModel
import javax.inject.Inject

@HiltViewModel
class FindCurrentLocationViewModelImpl @Inject constructor(
    private val repo: MapRepo
): FindCurrentLocationViewModel, ViewModel() {

    override fun setLocation(location: Location) {
       repo.setLocation(location).onEach {

       }.launchIn(viewModelScope)
    }

}