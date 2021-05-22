package com.applications.toms.depormas

import com.applications.toms.depormas.data.database.local.favorite.Favorite
import com.applications.toms.depormas.domain.Event
import com.applications.toms.depormas.domain.Location
import com.applications.toms.depormas.domain.Sport

internal val mockSport = Sport(
    id = 0,
    name = "mockSport",
    img = "",
    max_players = 4,
    choosen = false
)

internal val mockLocation = Location()

internal val mockEvent = Event(
    id = "0",
    event_name = "Name",
    date = "25/05/2022",
    time = "18:00",
    sport = mockSport,
    max_players = 2,
    location = mockLocation,
    participants = 2
)

internal val mockFavorite = Favorite(
    id = 1,
    eventId = "0"
)