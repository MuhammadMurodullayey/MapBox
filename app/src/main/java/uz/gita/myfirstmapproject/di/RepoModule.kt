package uz.gita.myfirstmapproject.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.myfirstmapproject.domein.repository.MapRepo
import uz.gita.myfirstmapproject.domein.repository.impl.MapRepoImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepoModule {

    @[Binds Singleton]
    fun getAppRepository(impl: MapRepoImpl)  : MapRepo
}