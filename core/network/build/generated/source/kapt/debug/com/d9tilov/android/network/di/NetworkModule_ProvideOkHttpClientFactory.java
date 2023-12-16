package com.d9tilov.android.network.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class NetworkModule_ProvideOkHttpClientFactory implements Factory<OkHttpClient> {
  private final Provider<HttpLoggingInterceptor> interceptorProvider;

  public NetworkModule_ProvideOkHttpClientFactory(
      Provider<HttpLoggingInterceptor> interceptorProvider) {
    this.interceptorProvider = interceptorProvider;
  }

  @Override
  public OkHttpClient get() {
    return provideOkHttpClient(interceptorProvider.get());
  }

  public static NetworkModule_ProvideOkHttpClientFactory create(
      Provider<HttpLoggingInterceptor> interceptorProvider) {
    return new NetworkModule_ProvideOkHttpClientFactory(interceptorProvider);
  }

  public static OkHttpClient provideOkHttpClient(HttpLoggingInterceptor interceptor) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideOkHttpClient(interceptor));
  }
}
