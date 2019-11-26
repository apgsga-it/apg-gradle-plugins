package com.apgsga.gradle.repository.artifactory;

import org.apache.http.HttpRequestInterceptor;
import org.apache.http.ssl.SSLContextBuilder;
import org.jfrog.artifactory.client.Artifactory;
import org.jfrog.artifactory.client.ArtifactoryClientBuilder;
import org.jfrog.artifactory.client.ProxyConfig;

import com.apgsga.gradle.repository.Repository;
import com.apgsga.gradle.repository.RepositoryBuilder;

public class RemoteRepositoryBuilder implements RepositoryBuilder {
	private ArtifactoryClientBuilder artifactortyBuilder = ArtifactoryClientBuilder.create();
	public static RepositoryBuilder create(String baseUrl) {
		RemoteRepositoryBuilder builder = new RemoteRepositoryBuilder();
		builder.setBaseUrl(baseUrl);
		return builder;
	}
	
	private String targetRepo;
	
	
	@Override
	public Repository build() {
		Artifactory artifactory = artifactortyBuilder.build();
		return new RemoteRepository(artifactory.repository(targetRepo));
		
	}


	@Override
	public RepositoryBuilder setTargetRepo(String targetRepo) {
		this.targetRepo = targetRepo;
		return this;
	}


	public ProxyConfig getProxy() {
		return artifactortyBuilder.getProxy();
	}


	public Integer getConnectionTimeout() {
		return artifactortyBuilder.getConnectionTimeout();
	}


	public Integer getSocketTimeout() {
		return artifactortyBuilder.getSocketTimeout();
	}


	public boolean isIgnoreSSLIssues() {
		return artifactortyBuilder.isIgnoreSSLIssues();
	}


	public SSLContextBuilder getSslContextBuilder() {
		return artifactortyBuilder.getSslContextBuilder();
	}


	public String getAccessToken() {
		return artifactortyBuilder.getAccessToken();
	}


	public RepositoryBuilder setBaseUrl(String url) {
		artifactortyBuilder.setUrl(url);
		return this;
	}

	public RepositoryBuilder setUsername(String username) {
		artifactortyBuilder.setUsername(username);
		return this;
	}

	public RepositoryBuilder setPassword(String password) {
		artifactortyBuilder.setPassword(password);
		return this; 
	}

	public ArtifactoryClientBuilder setConnectionTimeout(Integer connectionTimeout) {
		return artifactortyBuilder.setConnectionTimeout(connectionTimeout);
	}

	public ArtifactoryClientBuilder setSocketTimeout(Integer socketTimeout) {
		return artifactortyBuilder.setSocketTimeout(socketTimeout);
	}

	public ArtifactoryClientBuilder setProxy(ProxyConfig proxy) {
		return artifactortyBuilder.setProxy(proxy);
	}

	public ArtifactoryClientBuilder setUserAgent(String userAgent) {
		return artifactortyBuilder.setUserAgent(userAgent);
	}

	public ArtifactoryClientBuilder setIgnoreSSLIssues(boolean ignoreSSLIssues) {
		return artifactortyBuilder.setIgnoreSSLIssues(ignoreSSLIssues);
	}

	public ArtifactoryClientBuilder setSslContextBuilder(SSLContextBuilder sslContextBuilder) {
		return artifactortyBuilder.setSslContextBuilder(sslContextBuilder);
	}

	public ArtifactoryClientBuilder setAccessToken(String accessToken) {
		return artifactortyBuilder.setAccessToken(accessToken);
	}

	public ArtifactoryClientBuilder addInterceptorLast(HttpRequestInterceptor httpRequestInterceptor) {
		return artifactortyBuilder.addInterceptorLast(httpRequestInterceptor);
	}
	
	
	

}
