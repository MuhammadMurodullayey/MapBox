package uz.gita.myfirstmapproject.precentation.viewModels

import android.location.Location

interface FindCurrentLocationViewModel {
    fun setLocation(location: Location)
}