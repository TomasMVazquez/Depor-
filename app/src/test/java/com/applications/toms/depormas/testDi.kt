package com.applications.toms.depormas

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import com.applications.toms.depormas.data.database.local.favorite.Favorite
import com.applications.toms.depormas.data.source.LocationDataSource
import com.applications.toms.depormas.data.source.PermissionChecker
import com.applications.toms.depormas.data.source.events.LocalEventDataSource
import com.applications.toms.depormas.data.source.events.RemoteEventDataSource
import com.applications.toms.depormas.data.source.favorite.LocalFavoriteDataSource
import com.applications.toms.depormas.data.source.sports.LocalSportDataSource
import com.applications.toms.depormas.data.source.sports.RemoteSportDataSource
import com.applications.toms.depormas.domain.Event
import com.applications.toms.depormas.domain.Sport
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

fun initMockDi(vararg modules: Module){
    startKoin {
        modules(listOf(mockAppModule,dataModule) + modules)
    }
}

private val mockAppModule = module {

    single<LocalEventDataSource> { FakeRoomEventDataSource() }
    single<LocalFavoriteDataSource> { FakeRoomFavoriteDataSource() }
    single<LocalSportDataSource> { FakeRoomSportDataSource() }

    single<RemoteEventDataSource> { FakeEventFirestoreServer() }
    single<RemoteSportDataSource> { FakeSportFirestoreServer() }

    single<LocationDataSource> { FakePlayServicesLocationDataSource(get()) }
    single<PermissionChecker> { FakeAndroidPermissionChecker() }

    single<CoroutineDispatcher> { Dispatchers.Unconfined }
}

class FakeAndroidPermissionChecker: PermissionChecker {
    var permissionGranted = true

    override fun check(permission: PermissionChecker.Permission): Boolean = permissionGranted
}

class FakePlayServicesLocationDataSource(context: Context): LocationDataSource {
    private val geoCoder = Geocoder(context)
    var region = "ES"
    var location = Location("location").apply {
        latitude = 17.372102
        longitude = 78.484196
    }

    override suspend fun findLastLocation(): Location = location

    override suspend fun findMyCurrentLocation(): Address = location.toAddress()

    override suspend fun findLastRegion(): String = region

    private fun Location?.toAddress(): Address {
        val addresses = this?.let {
            geoCoder.getFromLocation(latitude, longitude, 1)
        }
        return addresses!!.first()
    }
}

class FakeSportFirestoreServer: RemoteSportDataSource {
    var sports = defaultFakeSports

    override fun getSportList(): List<Sport> = sports
}

class FakeEventFirestoreServer: RemoteEventDataSource {

    var id = 4
    var events = defaultFakeEvents

    override fun updateEvent(event: Event) {
        events = events.filterNot { it.id == event.id } + event
    }

    override fun getEventDocumentId(): String {
        id++
        return "0$id"
    }

    override fun getEventList(): List<Event> = events

    override fun saveEvent(event: Event) {
        this.events = events + event
    }

}

class FakeRoomSportDataSource: LocalSportDataSource {

    var sports: List<Sport> = emptyList()

    override suspend fun isEmpty(): Boolean = sports.isEmpty()

    override suspend fun saveSports(sports: List<Sport>) {
        this.sports = sports
    }

    override fun getAllSports(): Flow<List<Sport>> = flowOf(sports)

}

class FakeRoomFavoriteDataSource: LocalFavoriteDataSource {

    var favorites: List<Favorite> = emptyList()

    override suspend fun isEmpty(): Boolean = favorites.isEmpty()

    override fun saveFavorite(favorite: Favorite) {
        this.favorites = favorites + favorite
    }

    override fun findFavorite(eventId: String): Int = favorites.find { it.eventId == eventId }?.id ?: -1

    override fun getAllFavorite(): Flow<List<Favorite>> = flowOf(favorites)

    override fun deleteFavorite(eventId: String) {
        val favoriteToDelete = favorites.find { it.eventId == eventId }
        this.favorites = favorites - favoriteToDelete!!
    }

}

class FakeRoomEventDataSource: LocalEventDataSource {

    var events: List<Event> = emptyList()

    override suspend fun isEmpty(): Boolean = events.isEmpty()

    override suspend fun saveEvents(events: List<Event>) {
        this.events = events
    }

    override fun getAllEvent(): Flow<List<Event>> = flowOf(events)

}

val defaultFakeFavorite = listOf(
    mockFavorite.copy(id = 0,eventId = "01"),
    mockFavorite.copy(id = 1,eventId = "02")
)

val defaultFakeSports = listOf(
    mockSport.copy(id = 0),
    mockSport.copy(id = 1),
    mockSport.copy(id = 2),
    mockSport.copy(id = 3)
)

val defaultFakeEvents = listOf(
    mockEvent.copy(id = "01"),
    mockEvent.copy(id = "02"),
    mockEvent.copy(id = "03"),
    mockEvent.copy(id = "04")
)