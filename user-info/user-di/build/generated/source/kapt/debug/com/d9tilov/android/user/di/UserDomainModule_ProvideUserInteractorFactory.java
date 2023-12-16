package com.d9tilov.android.user.di;

import com.d9tilov.android.user.domain.contract.UserInteractor;
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
public final class UserDomainModule_ProvideUserInteractorFactory implements Factory<UserInteractor> {
  private final Provider<UserRepo> userRepoProvider;

  public UserDomainModule_ProvideUserInteractorFactory(Provider<UserRepo> userRepoProvider) {
    this.userRepoProvider = userRepoProvider;
  }

  @Override
  public UserInteractor get() {
    return provideUserInteractor(userRepoProvider.get());
  }

  public static UserDomainModule_ProvideUserInteractorFactory create(
      Provider<UserRepo> userRepoProvider) {
    return new UserDomainModule_ProvideUserInteractorFactory(userRepoProvider);
  }

  public static UserInteractor provideUserInteractor(UserRepo userRepo) {
    return Preconditions.checkNotNullFromProvides(UserDomainModule.INSTANCE.provideUserInteractor(userRepo));
  }
}
