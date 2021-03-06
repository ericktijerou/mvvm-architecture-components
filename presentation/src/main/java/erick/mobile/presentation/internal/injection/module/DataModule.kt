package erick.mobile.presentation.internal.injection.module

import android.content.Context
import dagger.Module
import dagger.Provides
import erick.mobile.data.BuildConfig
import erick.mobile.data.gateway.InventoryGatewayImpl
import erick.mobile.data.local.DogLocalDataSource
import erick.mobile.data.local.DogTypeLocalDataSource
import erick.mobile.data.local.dao.DogDao
import erick.mobile.data.local.dao.DogTypeDao
import erick.mobile.data.local.inventory.InventoryDatabase
import erick.mobile.data.local.system.SystemDatabase
import erick.mobile.data.remote.DogRemoteDataSource
import erick.mobile.data.remote.api.TheDogApi
import erick.mobile.data.remote.api.TheDogService
import erick.mobile.data.repository.DogRepository
import erick.mobile.data.repository.DogTypeRepository
import erick.mobile.data.repository.mapper.DogMapper
import erick.mobile.domain.gateway.InventoryGateway
import javax.inject.Singleton

@Module
internal class DataModule {

    @Provides
    @Singleton
    internal fun provideTheDogService(): TheDogService = TheDogApi(BuildConfig.API_URL)

    @Provides
    @Singleton
    internal fun provideDogRemoteDataSource(dogService: TheDogService): DogRemoteDataSource {
        return DogRemoteDataSource(dogService)
    }

    @Provides
    @Singleton
    internal fun provideInventoryDatabase(context: Context): InventoryDatabase {
        return InventoryDatabase.newInstance(context)
    }

    @Provides
    @Singleton
    internal fun provideDogDiskDataSource(dogDao: DogDao): DogLocalDataSource {
        return DogLocalDataSource(dogDao)
    }

    @Provides
    @Singleton
    internal fun provideDogDao(inventoryDatabase: InventoryDatabase): DogDao = inventoryDatabase.dogDao()

    @Provides
    @Singleton
    internal fun provideSystemDatabase(context: Context): SystemDatabase {
        return SystemDatabase.newInstance(context)
    }

    @Provides
    @Singleton
    internal fun provideEventTypeDao(systemDatabase: SystemDatabase): DogTypeDao = systemDatabase.dogTypeDao()

    @Provides
    @Singleton
    internal fun provideDogRepository(dogLocalDataSource: DogLocalDataSource,
                                        dogRemoteDataSource: DogRemoteDataSource): DogRepository {
        return DogRepository(dogLocalDataSource, dogRemoteDataSource, DogMapper())
    }

    @Provides
    @Singleton
    internal fun provideDogTypeRepository(eventTypeLocalDataSource: DogTypeLocalDataSource): DogTypeRepository {
        return DogTypeRepository(eventTypeLocalDataSource)
    }


    @Provides
    @Singleton
    internal fun provideInventoryRepository(dogRepository: DogRepository, dogTypeRepository: DogTypeRepository): InventoryGateway {
        return InventoryGatewayImpl(dogRepository, dogTypeRepository)
    }

    @Provides
    @Singleton
    internal fun provideDogTypeDiskDataSource(dogTypeDao: DogTypeDao): DogTypeLocalDataSource {
        return DogTypeLocalDataSource(dogTypeDao)
    }

}
