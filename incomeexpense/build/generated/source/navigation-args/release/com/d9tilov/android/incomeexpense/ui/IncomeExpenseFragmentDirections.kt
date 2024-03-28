package com.d9tilov.android.incomeexpense.ui

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavDirections
import com.d9tilov.android.category.ui.navigation.CategoryDestination
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.incomeexpense.R
import com.d9tilov.android.regular.transaction.domain.model.RegularTransaction
import com.d9tilov.android.transaction.domain.model.Transaction
import java.io.Serializable
import java.lang.UnsupportedOperationException
import kotlin.Int
import kotlin.Suppress

public class IncomeExpenseFragmentDirections private constructor() {
  private data class ToCategoryDest(
    public val destination: CategoryDestination,
    public val transactionType: TransactionType
  ) : NavDirections {
    public override val actionId: Int = R.id.to_category_dest

    public override val arguments: Bundle
      @Suppress("CAST_NEVER_SUCCEEDS")
      get() {
        val result = Bundle()
        if (Parcelable::class.java.isAssignableFrom(CategoryDestination::class.java)) {
          result.putParcelable("destination", this.destination as Parcelable)
        } else if (Serializable::class.java.isAssignableFrom(CategoryDestination::class.java)) {
          result.putSerializable("destination", this.destination as Serializable)
        } else {
          throw UnsupportedOperationException(CategoryDestination::class.java.name +
              " must implement Parcelable or Serializable or must be an Enum.")
        }
        if (Parcelable::class.java.isAssignableFrom(TransactionType::class.java)) {
          result.putParcelable("transactionType", this.transactionType as Parcelable)
        } else if (Serializable::class.java.isAssignableFrom(TransactionType::class.java)) {
          result.putSerializable("transactionType", this.transactionType as Serializable)
        } else {
          throw UnsupportedOperationException(TransactionType::class.java.name +
              " must implement Parcelable or Serializable or must be an Enum.")
        }
        return result
      }
  }

  private data class ToEditTransactionDest(
    public val transactionType: TransactionType,
    public val editedTransaction: Transaction
  ) : NavDirections {
    public override val actionId: Int = R.id.to_edit_transaction_dest

    public override val arguments: Bundle
      @Suppress("CAST_NEVER_SUCCEEDS")
      get() {
        val result = Bundle()
        if (Parcelable::class.java.isAssignableFrom(TransactionType::class.java)) {
          result.putParcelable("transactionType", this.transactionType as Parcelable)
        } else if (Serializable::class.java.isAssignableFrom(TransactionType::class.java)) {
          result.putSerializable("transactionType", this.transactionType as Serializable)
        } else {
          throw UnsupportedOperationException(TransactionType::class.java.name +
              " must implement Parcelable or Serializable or must be an Enum.")
        }
        if (Parcelable::class.java.isAssignableFrom(Transaction::class.java)) {
          result.putParcelable("edited_transaction", this.editedTransaction as Parcelable)
        } else if (Serializable::class.java.isAssignableFrom(Transaction::class.java)) {
          result.putSerializable("edited_transaction", this.editedTransaction as Serializable)
        } else {
          throw UnsupportedOperationException(Transaction::class.java.name +
              " must implement Parcelable or Serializable or must be an Enum.")
        }
        return result
      }
  }

  private data class ToRemoveTransactionDialog(
    public val transaction: Transaction? = null,
    public val regularTransaction: RegularTransaction? = null
  ) : NavDirections {
    public override val actionId: Int = R.id.to_remove_transaction_dialog

    public override val arguments: Bundle
      @Suppress("CAST_NEVER_SUCCEEDS")
      get() {
        val result = Bundle()
        if (Parcelable::class.java.isAssignableFrom(Transaction::class.java)) {
          result.putParcelable("transaction", this.transaction as Parcelable?)
        } else if (Serializable::class.java.isAssignableFrom(Transaction::class.java)) {
          result.putSerializable("transaction", this.transaction as Serializable?)
        }
        if (Parcelable::class.java.isAssignableFrom(RegularTransaction::class.java)) {
          result.putParcelable("regular_transaction", this.regularTransaction as Parcelable?)
        } else if (Serializable::class.java.isAssignableFrom(RegularTransaction::class.java)) {
          result.putSerializable("regular_transaction", this.regularTransaction as Serializable?)
        }
        return result
      }
  }

  public companion object {
    public fun toCategoryDest(destination: CategoryDestination, transactionType: TransactionType):
        NavDirections = ToCategoryDest(destination, transactionType)

    public fun toEditTransactionDest(transactionType: TransactionType,
        editedTransaction: Transaction): NavDirections = ToEditTransactionDest(transactionType,
        editedTransaction)

    public fun toRemoveTransactionDialog(transaction: Transaction? = null,
        regularTransaction: RegularTransaction? = null): NavDirections =
        ToRemoveTransactionDialog(transaction, regularTransaction)
  }
}
