package com.applications.toms.depormas

import android.app.Application
import com.applications.toms.depormas.data.AndroidPermissionChecker
import com.applications.toms.depormas.data.PlayServicesLocationDataSource
import com.applications.toms.depormas.data.database.local.RoomEventDataSource
import com.applications.toms.depormas.data.database.local.RoomFavoriteDataSource
import com.applications.toms.depormas.data.database.local.RoomSportDataSource
import com.applications.toms.depormas.data.database.local.event.EventDatabase
import com.applications.toms.depormas.data.database.local.favorite.FavoriteDatabase
import com.applications.toms.depormas.data.database.local.sport.SportDatabase
import com.applications.toms.depormas.data.database.remote.EventFirestoreServer
import com.applications.toms.depormas.data.database.remote.SportFirestoreServer
import com.applications.toms.depormas.data.repository.EventRepository
import com.applications.toms.depormas.data.repository.FavoriteRepository
import com.applications.toms.depormas.data.repository.LocationRepository
import com.applications.toms.depormas.data.repository.SportRepository
import com.applications.toms.depormas.data.source.LocationDataSource
import com.applications.toms.depormas.data.source.PermissionChecker
import com.applications.toms.depormas.data.source.events.LocalEventDataSource
import com.applications.toms.depormas.data.source.events.RemoteEventDataSource
import com.applications.toms.depormas.data.source.favorite.LocalFavoriteDataSource
import com.applications.toms.depormas.data.source.sports.LocalSportDataSource
import com.applications.toms.depormas.data.source.sports.RemoteSportDataSource
import com.applications.toms.depormas.ui.screens.create.CreateEventFragment
import com.applications.toms.depormas.ui.screens.create.CreateViewModel
import com.applications.toms.depormas.ui.screens.favourite.FavoriteViewModel
import com.applications.toms.depormas.ui.screens.favourite.FavouriteFragment
import com.applications.toms.depormas.ui.screens.home.HomeFragment
import com.applications.toms.depormas.ui.screens.home.HomeViewModel
import com.applications.toms.depormas.usecases.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun Application.initDI(){
    startKoin {
        androidLogger(Level.ERROR)
        androidContext(this@initDI)
        modules(listOf(appModule, dataModule, scopesModule))
    }
}

private val appModule = module {
    single { EventDatabase.getInstance(get()) }
    single { FavoriteDatabase.getInstance(get()) }
    single { SportDatabase.getInstance(get()) }

    factory<LocalEventDataSource> { RoomEventDataSource(get()) }
    factory<LocalFavoriteDataSource> { RoomFavoriteDataSource(get()) }
    factory<LocalSportDataSource> { RoomSportDataSource(get()) }

    factory<RemoteEventDataSource> { EventFirestoreServer() }
    factory<RemoteSportDataSource> { SportFirestoreServer() }

    factory<LocationDataSource> { PlayServicesLocationDataSource(get()) }
    factory<PermissionChecker> { AndroidPermissionChecker(get()) }

    single<CoroutineDispatcher> { Dispatchers.Main }
}

private val dataModule = module {
    factory { EventRepository(get(),get()) }
    factory { FavoriteRepository(get()) }
    factory { SportRepository(get(),get()) }
    factory { LocationRepository(get(),get()) }
}

private val scopesModule = module {
    scope(named<HomeFragment>()) {
        viewModel { HomeViewModel(get(),get(),get(),get(),get()) }
        scoped { GetSports(get()) }
        scoped { GetEvents(get()) }
        scoped { IsFavorite(get()) }
        scoped { SaveFavorite(get(),get()) }
    }

    scope(named<FavouriteFragment>()) {
        viewModel { FavoriteViewModel(get(),get(),get()) }
        scoped { MyFavorite(get(),get()) }
        scoped { GetEvents(get()) }
    }

    scope(named<CreateEventFragment>()){
        viewModel { CreateViewModel(get(),get(),get()) }
        scoped { GetSports(get()) }
        scoped { SaveEvent(get(),get()) }
    }
}