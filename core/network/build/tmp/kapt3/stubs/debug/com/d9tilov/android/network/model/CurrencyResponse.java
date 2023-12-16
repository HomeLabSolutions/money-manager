package com.d9tilov.android.network.model;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010%\n\u0002\u0010\u0006\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B-\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0016\u0010\u0006\u001a\u0012\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\b0\u0007j\u0002`\t\u00a2\u0006\u0002\u0010\nJ\t\u0010\u0011\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0012\u001a\u00020\u0005H\u00c6\u0003J\u0019\u0010\u0013\u001a\u0012\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\b0\u0007j\u0002`\tH\u00c6\u0003J7\u0010\u0014\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\u0018\b\u0002\u0010\u0006\u001a\u0012\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\b0\u0007j\u0002`\tH\u00c6\u0001J\u0013\u0010\u0015\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0018\u001a\u00020\u0019H\u00d6\u0001J\t\u0010\u001a\u001a\u00020\u0003H\u00d6\u0001R\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0016\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR&\u0010\u0006\u001a\u0012\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\b0\u0007j\u0002`\t8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010\u00a8\u0006\u001b"}, d2 = {"Lcom/d9tilov/android/network/model/CurrencyResponse;", "", "base", "", "date", "", "rates", "", "", "Lcom/d9tilov/android/network/model/CurrencyRateMap;", "(Ljava/lang/String;JLjava/util/Map;)V", "getBase", "()Ljava/lang/String;", "getDate", "()J", "getRates", "()Ljava/util/Map;", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "network_debug"})
public final class CurrencyResponse {
    @com.squareup.moshi.Json(name = "base_code")
    @org.jetbrains.annotations.NotNull
    private final java.lang.String base = null;
    @com.squareup.moshi.Json(name = "time_last_update_unix")
    private final long date = 0L;
    @com.squareup.moshi.Json(name = "conversion_rates")
    @org.jetbrains.annotations.NotNull
    private final java.util.Map<java.lang.String, java.lang.Double> rates = null;
    
    public CurrencyResponse(@org.jetbrains.annotations.NotNull
    java.lang.String base, long date, @org.jetbrains.annotations.NotNull
    java.util.Map<java.lang.String, java.lang.Double> rates) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getBase() {
        return null;
    }
    
    public final long getDate() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.Map<java.lang.String, java.lang.Double> getRates() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component1() {
        return null;
    }
    
    public final long component2() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.Map<java.lang.String, java.lang.Double> component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.network.model.CurrencyResponse copy(@org.jetbrains.annotations.NotNull
    java.lang.String base, long date, @org.jetbrains.annotations.NotNull
    java.util.Map<java.lang.String, java.lang.Double> rates) {
        return null;
    }
    
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public java.lang.String toString() {
        return null;
    }
}