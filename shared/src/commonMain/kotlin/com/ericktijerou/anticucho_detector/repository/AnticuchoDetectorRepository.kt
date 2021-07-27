package com.ericktijerou.anticucho_detector.repository

import co.touchlab.kermit.Kermit
import com.ericktijerou.anticucho_detector.UNKNOWN
import com.ericktijerou.anticucho_detector.di.AnticuchoDetectorDatabaseWrapper
import com.ericktijerou.anticucho_detector.remote.AnticuchoDetectorApi
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.CoroutineContext

class AnticuchoDetectorRepository : KoinComponent {
    private val anticuchoDetectorApi: AnticuchoDetectorApi by inject()
    private val logger: Kermit by inject()

    private val database: AnticuchoDetectorDatabaseWrapper by inject()
    private val databaseQueries = database.instance?.anticuchoDetectorDatabaseQueries

    var detectorJob: Job? = null

    fun fetchPeopleAsFlow(): Flow<List<String>> {
        // the main reason we need to do this check is that sqldelight isn't currently
        // setup for javascript client
        return databaseQueries?.selectAll(mapper = { name, lastname ->
            name
        })?.asFlow()?.mapToList() ?: flowOf(emptyList())
    }

    suspend fun compareImage(filename: String, filepath: String): List<String> {
        logger.d { "compareImage" }
        val response = anticuchoDetectorApi.compareImage(filename, filepath)

        // this is very basic implementation for now that removes all existing rows
        // in db and then inserts results from api request
        databaseQueries?.deleteAll()
        response.result.forEach {
            databaseQueries?.insertItem(it, it)
        }
        return response.result.filterNot { it == UNKNOWN }
    }

    fun stopObservingPeopleUpdates() {
        logger.d { "stopObservingPeopleUpdates, peopleJob = $detectorJob" }
        detectorJob?.cancel()
    }

    val iosScope: CoroutineScope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = SupervisorJob() + Dispatchers.Main
    }

    companion object {
        private const val POLL_INTERVAL = 10000L
    }
}


class KotlinNativeFlowWrapper<T>(private val flow: Flow<T>) {
    fun subscribe(
        scope: CoroutineScope,
        onEach: (item: T) -> Unit,
        onComplete: () -> Unit,
        onThrow: (error: Throwable) -> Unit
    ) = flow
        .onEach { onEach(it) }
        .catch { onThrow(it) }
        .onCompletion { onComplete() }
        .launchIn(scope)
}


