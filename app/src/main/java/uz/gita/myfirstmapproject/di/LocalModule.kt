package uz.gita.myfirstmapproject.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.gita.myfirstmapproject.data.local.MapDataBase
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
class LocalModule {
    @[Provides Singleton]
    fun getDatabase(@ApplicationContext context : Context) = Room.databaseBuilder(context, MapDataBase::class.java, "Sample.db")
        .fallbackToDestructiveMigration()
        .build()
}