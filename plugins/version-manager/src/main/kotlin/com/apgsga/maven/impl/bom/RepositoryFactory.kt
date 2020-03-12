package com.apgsga.maven.impl.bom

import com.apgsga.gradle.repository.Repository
import com.apgsga.gradle.repository.artifactory.RemoteRepositoryBuilder
import com.apgsga.gradle.repository.local.FileRepositoryBuilder
import com.apgsga.gradle.repository.nop.NopRepository

@Deprecated("GradleDependencyLoader to be used instead")
abstract class RepositoryFactory(val baseUrl: String? = null, val repoName: String? = "", val user: String? = "", val password: String? = "") {
    abstract fun makeRepo(): Repository

    companion object {
        @JvmStatic
        @JvmOverloads
        fun createFactory(baseUrl: String?, repoName: String? ="", user: String? = "", password: String? = ""): RepositoryFactory = when {
            baseUrl == null -> NopRepositoryFactory()
            baseUrl.startsWith("http") -> RemoteRepositoryFactory(baseUrl, repoName
                    ?: "", user ?: "", password ?: "")
            else -> FileRepositoryFactory(baseUrl, repoName ?: "")
        }

    }
}

@Deprecated("GradleDependencyLoader to be used instead")
class FileRepositoryFactory(baseUrl: String, repoName: String) : RepositoryFactory(baseUrl, repoName) {

    override fun makeRepo(): Repository {
        require(!repoName.isNullOrEmpty()) { "RepoName should'nt be null or empty" }
        return FileRepositoryBuilder.create(baseUrl).setTargetRepo(repoName).build()
    }

}

@Deprecated("GradleDependencyLoader to be used instead")
class RemoteRepositoryFactory(baseUrl: String, repoName: String, user: String, password: String) : RepositoryFactory(baseUrl, repoName, user, password) {

    override fun makeRepo(): Repository {
        require(!repoName.isNullOrEmpty()) { "RepoName should'nt be null or empty" }
        require(!user.isNullOrEmpty()) { "User should'nt be null or empty" }
        require(!password.isNullOrEmpty()) { "Password should'nt be null or empty" }
        return RemoteRepositoryBuilder.create(baseUrl).setTargetRepo(repoName).setUsername(user).setPassword(password).build()
    }

}

@Deprecated("GradleDependencyLoader to be used instead")
class NopRepositoryFactory : RepositoryFactory() {

    override fun makeRepo(): Repository {
        return NopRepository.NOP
    }

}
