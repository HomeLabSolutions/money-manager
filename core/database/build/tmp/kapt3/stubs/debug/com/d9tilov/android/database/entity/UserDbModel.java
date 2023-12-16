package com.d9tilov.android.database.entity;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0018\b\u0087\b\u0018\u00002\u00020\u0001B9\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u00a2\u0006\u0002\u0010\u000bJ\t\u0010\u0017\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\u0018\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\u0019\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001b\u001a\u00020\bH\u00c6\u0003J\t\u0010\u001c\u001a\u00020\nH\u00c6\u0003JI\u0010\u001d\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\nH\u00c6\u0001J\u0013\u0010\u001e\u001a\u00020\b2\b\u0010\u001f\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010 \u001a\u00020\nH\u00d6\u0001J\t\u0010!\u001a\u00020\u0003H\u00d6\u0001R\u0016\u0010\u0006\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0018\u0010\u0004\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\rR\u0016\u0010\t\u001a\u00020\n8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0018\u0010\u0005\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\rR\u0016\u0010\u0007\u001a\u00020\b8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u001e\u0010\u0002\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\r\"\u0004\b\u0015\u0010\u0016\u00a8\u0006\""}, d2 = {"Lcom/d9tilov/android/database/entity/UserDbModel;", "", "uid", "", "firstName", "lastName", "currentCurrencyCode", "showPrepopulate", "", "fiscalDay", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZI)V", "getCurrentCurrencyCode", "()Ljava/lang/String;", "getFirstName", "getFiscalDay", "()I", "getLastName", "getShowPrepopulate", "()Z", "getUid", "setUid", "(Ljava/lang/String;)V", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "other", "hashCode", "toString", "database_debug"})
@androidx.room.Entity(tableName = "users")
public final class UserDbModel {
    @androidx.room.PrimaryKey
    @androidx.room.ColumnInfo(name = "uid")
    @org.jetbrains.annotations.NotNull
    private java.lang.String uid;
    @androidx.room.ColumnInfo(name = "firstName")
    @org.jetbrains.annotations.Nullable
    private final java.lang.String firstName = null;
    @androidx.room.ColumnInfo(name = "lastName")
    @org.jetbrains.annotations.Nullable
    private final java.lang.String lastName = null;
    @androidx.room.ColumnInfo(name = "currentCurrencyCode")
    @org.jetbrains.annotations.NotNull
    private final java.lang.String currentCurrencyCode = null;
    @androidx.room.ColumnInfo(name = "showPrepopulate")
    private final boolean showPrepopulate = false;
    @androidx.room.ColumnInfo(name = "fiscalDay")
    private final int fiscalDay = 0;
    
    public UserDbModel(@org.jetbrains.annotations.NotNull
    java.lang.String uid, @org.jetbrains.annotations.Nullable
    java.lang.String firstName, @org.jetbrains.annotations.Nullable
    java.lang.String lastName, @org.jetbrains.annotations.NotNull
    java.lang.String currentCurrencyCode, boolean showPrepopulate, int fiscalDay) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getUid() {
        return null;
    }
    
    public final void setUid(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getFirstName() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getLastName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getCurrentCurrencyCode() {
        return null;
    }
    
    public final boolean getShowPrepopulate() {
        return false;
    }
    
    public final int getFiscalDay() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component4() {
        return null;
    }
    
    public final boolean component5() {
        return false;
    }
    
    public final int component6() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.database.entity.UserDbModel copy(@org.jetbrains.annotations.NotNull
    java.lang.String uid, @org.jetbrains.annotations.Nullable
    java.lang.String firstName, @org.jetbrains.annotations.Nullable
    java.lang.String lastName, @org.jetbrains.annotations.NotNull
    java.lang.String currentCurrencyCode, boolean showPrepopulate, int fiscalDay) {
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