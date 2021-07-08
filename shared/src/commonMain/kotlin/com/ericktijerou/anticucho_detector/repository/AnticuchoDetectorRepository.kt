package com.ericktijerou.anticucho_detector.repository

import co.touchlab.kermit.Kermit
import com.ericktijerou.anticucho_detector.di.AnticuchoDetectorDatabaseWrapper
import com.ericktijerou.anticucho_detector.remote.AnticuchoDetectorApi
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.CoroutineContext

class AnticuchoDetectorRepository : KoinComponent  {
    private val anticuchoDetectorApi: AnticuchoDetectorApi by inject()
    private val logger: Kermit by inject()

    private val coroutineScope: CoroutineScope = MainScope()
    private val database : AnticuchoDetectorDatabaseWrapper by inject()
    private val databaseQueries = database.instance?.anticuchoDetectorDatabaseQueries

    var peopleJob: Job? = null

    fun fetchPeopleAsFlow(): Flow<List<String>> {
        // the main reason we need to do this check is that sqldelight isn't currently
        // setup for javascript client
        return databaseQueries?.selectAll(mapper = { name, lastname ->
            name
        })?.asFlow()?.mapToList() ?: flowOf(emptyList())
    }

    suspend fun compareImage(filename: String, filepath: String): String  {
        logger.d { "fetchAndStorePeople" }
        val response = anticuchoDetectorApi.compareImage(filename, filepath)

        // this is very basic implementation for now that removes all existing rows
        // in db and then inserts results from api request
        databaseQueries?.deleteAll()
        response.result.forEach {
            databaseQueries?.insertItem(it, it)
        }
        return response.result.first()
    }

    // Used by web client atm
    suspend fun fetchPeople(filename: String, filepath: String) = anticuchoDetectorApi.compareImage(filename, filepath).result

    //fun getPersonBio(personName: String) = personBios[personName] ?: ""
    //fun getPersonImage(personName: String) = personImages[personName] ?: ""


    // called from Kotlin/Native clients
    fun startObservingPeopleUpdates(success: (List<String>) -> Unit) {
        logger.d { "startObservingPeopleUpdates" }
        peopleJob = coroutineScope.launch {
            fetchPeopleAsFlow().collect {
                success(it)
            }
        }
    }

    fun stopObservingPeopleUpdates() {
        logger.d { "stopObservingPeopleUpdates, peopleJob = $peopleJob" }
        peopleJob?.cancel()
    }


/*    fun pollISSPosition(): Flow<IssPosition> = flow {
        while (true) {
            val position = api.fetchISSPosition().iss_position
            emit(position)
            logger.d("Repository") { position.toString() }
            delay(POLL_INTERVAL)
        }
    }*/



    val iosScope: CoroutineScope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = SupervisorJob() + Dispatchers.Main
    }

    // fun iosPollISSPosition() = KotlinNativeFlowWrapper<IssPosition>(pollISSPosition())


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


