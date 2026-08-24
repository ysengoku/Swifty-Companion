package dev.ysengoku.swiftycompanion.data.repository

import kotlinx.coroutines.CancellationException
import dev.ysengoku.swiftycompanion.data.IntraService
import dev.ysengoku.swiftycompanion.data.model.User

class UserRepository (val intraService: IntraService) {
    suspend fun fetchUser(login: String): Result<User> {
        return try {
            Result.success(intraService.getUser(login))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }
}