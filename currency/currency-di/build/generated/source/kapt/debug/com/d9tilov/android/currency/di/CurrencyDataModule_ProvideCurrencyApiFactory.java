package com.d9tilov.android.currency.di;

import com.d9tilov.android.network.CurrencyApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

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
public final class CurrencyDataModule_ProvideCurrencyApiFactory implements Factory<CurrencyApi> {
  private final Provider<Retrofit> retrofitProvider;

  public CurrencyDataModule_ProvideCurrencyApiFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public CurrencyApi get() {
    return provideCurrencyApi(retrofitProvider.get());
  }

  public static CurrencyDataModule_ProvideCurrencyApiFactory create(
      Provider<Retrofit> retrofitProvider) {
    return new CurrencyDataModule_ProvideCurrencyApiFactory(retrofitProvider);
  }

  public static CurrencyApi provideCurrencyApi(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(CurrencyDataModule.INSTANCE.provideCurrencyApi(retrofit));
  }
}
