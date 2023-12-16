package com.d9tilov.android.network.di;

@dagger.Module
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010\b\u001a\u00020\tH\u0007J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\tH\u0007J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000bH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/d9tilov/android/network/di/NetworkModule;", "", "()V", "BASE_URL", "", "READ_TIMEOUT", "", "WRITE_TIMEOUT", "provideLoggingInterceptor", "Lokhttp3/logging/HttpLoggingInterceptor;", "provideOkHttpClient", "Lokhttp3/OkHttpClient;", "interceptor", "provideRetrofit", "Lretrofit2/Retrofit;", "okHttpClient", "network_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class NetworkModule {
    private static final long READ_TIMEOUT = 15L;
    private static final long WRITE_TIMEOUT = 15L;
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String BASE_URL = "https://v6.exchangerate-api.com/v6/";
    @org.jetbrains.annotations.NotNull
    public static final com.d9tilov.android.network.di.NetworkModule INSTANCE = null;
    
    private NetworkModule() {
        super();
    }
    
    @dagger.Provides
    @org.jetbrains.annotations.NotNull
    public final okhttp3.OkHttpClient provideOkHttpClient(@org.jetbrains.annotations.NotNull
    okhttp3.logging.HttpLoggingInterceptor interceptor) {
        return null;
    }
    
    @dagger.Provides
    @org.jetbrains.annotations.NotNull
    public final okhttp3.logging.HttpLoggingInterceptor provideLoggingInterceptor() {
        return null;
    }
    
    @dagger.Provides
    @org.jetbrains.annotations.NotNull
    public final retrofit2.Retrofit provideRetrofit(@org.jetbrains.annotations.NotNull
    okhttp3.OkHttpClient okHttpClient) {
        return null;
    }
}