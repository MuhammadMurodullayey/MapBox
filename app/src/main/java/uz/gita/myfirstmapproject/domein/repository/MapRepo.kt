package uz.gita.myfirstmapproject.domein.repository

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface MapRepo {
  fun setLocation(location: Location): Flow<Unit>
}