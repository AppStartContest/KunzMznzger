package com.ltei.kunzmznzger.models.dao

import com.ltei.kunzmznzger.libs.api.ApiQueryBuilder
import com.ltei.kunzmznzger.libs.api.Http
import com.ltei.kunzmznzger.libs.models.Model
import com.ltei.kunzmznzger.libs.models.ModelManager
import com.ltei.kunzmznzger.libs.models.exceptions.ModelException
import com.ltei.kunzmznzger.models.User
import org.json.simple.JSONObject
import java.util.concurrent.CompletableFuture

class UserDAO : ModelManager<User>() {
    override fun getNamespace(): String {
        return "users"
    }

    override fun getModelInstanceClass(): Class<User> {
        return User::class.java
    }

    /**
     * @throws ModelException Cannot create a user this way. See UserDao#createAccount()
     */
    override fun create(model: Model<User>?): CompletableFuture<User> {
        throw ModelException("You can't create a user this way. See UserDao#createAccount()")
    }

    /**
     * Create an account
     * @param username the user's username
     * @param password not hashed password
     * @param name If null, it will be equal to username
     * @return the created user or null
     */
    fun register(username: String, password: String, name: String? = null): CompletableFuture<User?> {
        val url = "${ModelManager.API_URL}register"
        val json = JSONObject()
        json["password"] = password
        json["username"] = username
        json["name"] = name ?: username

        val query = ApiQueryBuilder.create(url, Http.POST).data(json).buildQuery()
        return query.execute().thenCompose { apiResponse -> CompletableFuture.supplyAsync { this.handleResponseObject(apiResponse) } }
    }

    /**
     * Authenticate a user
     * @param username the user's username
     * @param password not hashed password
     * @return the authenticated user or null
     */
    fun auth(username: String, password: String): CompletableFuture<User?> {
        val url = "${ModelManager.API_URL}auth"
        val json = JSONObject()
        json["password"] = password
        json["username"] = username

        val query = ApiQueryBuilder.create(url, Http.POST).data(json).buildQuery()
        return query.execute().thenCompose { apiResponse -> CompletableFuture.supplyAsync { this.handleResponseObject(apiResponse) } }
    }
}