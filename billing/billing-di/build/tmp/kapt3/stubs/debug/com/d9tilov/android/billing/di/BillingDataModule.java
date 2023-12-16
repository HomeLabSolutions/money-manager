package com.d9tilov.android.billing.di;

@dagger.Module
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bg\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\'J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\'\u00a8\u0006\n"}, d2 = {"Lcom/d9tilov/android/billing/di/BillingDataModule;", "", "provideBillingLocalSource", "Lcom/d9tilov/android/billing/data/contract/BillingSource;", "billingDataSource", "Lcom/d9tilov/android/billing/data/impl/BillingDataSource;", "provideBillingRepo", "Lcom/d9tilov/android/billing/domain/contract/BillingRepo;", "billingDataRepo", "Lcom/d9tilov/android/billing/data/impl/BillingDataRepo;", "billing-di_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public abstract interface BillingDataModule {
    
    @dagger.Binds
    @javax.inject.Singleton
    @org.jetbrains.annotations.NotNull
    public abstract com.d9tilov.android.billing.data.contract.BillingSource provideBillingLocalSource(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.billing.data.impl.BillingDataSource billingDataSource);
    
    @dagger.Binds
    @javax.inject.Singleton
    @org.jetbrains.annotations.NotNull
    public abstract com.d9tilov.android.billing.domain.contract.BillingRepo provideBillingRepo(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.billing.data.impl.BillingDataRepo billingDataRepo);
}