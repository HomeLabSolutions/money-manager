package com.d9tilov.android.network.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import kotlinx.coroutines.CoroutineDispatcher;

@ScopeMetadata
@QualifierMetadata("com.d9tilov.android.network.dispatchers.Dispatcher")
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
public final class DispatchersModule_ProvidesMainDispatcherFactory implements Factory<CoroutineDispatcher> {
  @Override
  public CoroutineDispatcher get() {
    return providesMainDispatcher();
  }

  public static DispatchersModule_ProvidesMainDispatcherFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CoroutineDispatcher providesMainDispatcher() {
    return Preconditions.checkNotNullFromProvides(DispatchersModule.INSTANCE.providesMainDispatcher());
  }

  private static final class InstanceHolder {
    private static final DispatchersModule_ProvidesMainDispatcherFactory INSTANCE = new DispatchersModule_ProvidesMainDispatcherFactory();
  }
}
