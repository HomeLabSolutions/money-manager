package com.d9tilov.android.user.di;

import com.d9tilov.android.user.data.contract.UserSource;
import com.d9tilov.android.user.domain.contract.UserRepo;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class UserDataModule_ProvideUserRepoFactory implements Factory<UserRepo> {
  private final Provider<UserSource> userSourceProvider;

  public UserDataModule_ProvideUserRepoFactory(Provider<UserSource> userSourceProvider) {
    this.userSourceProvider = userSourceProvider;
  }

  @Override
  public UserRepo get() {
    return provideUserRepo(userSourceProvider.get());
  }

  public static UserDataModule_ProvideUserRepoFactory create(
      Provider<UserSource> userSourceProvider) {
    return new UserDataModule_ProvideUserRepoFactory(userSourceProvider);
  }

  public static UserRepo provideUserRepo(UserSource userSource) {
    return Preconditions.checkNotNullFromProvides(UserDataModule.INSTANCE.provideUserRepo(userSource));
  }
}
