package com.applications.toms.depormas.data.source

interface PermissionChecker {

    enum class Permission { COARSE_LOCATION, FINE_LOCATION }

    suspend fun check(permission: Permission): Boolean
}