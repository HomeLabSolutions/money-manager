package com.d9tilov.android.backup.data.model

data class BackupData(
    val lastBackupTimestamp: Long
) {

    companion object {
        val EMPTY = BackupData(lastBackupTimestamp = -1L)
    }
}
