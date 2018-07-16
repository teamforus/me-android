package io.forus.me.android.domain.exception;

import java.io.IOException;

import io.forus.me.android.domain.models.records.errors.NewRecordCategoryError;
import io.forus.me.android.domain.models.records.errors.NewRecordError;

public abstract class RetrofitExceptionMapper {

    public abstract NewRecordError mapToNewRecordError(RetrofitException retrofitException) throws IOException;

    public abstract NewRecordCategoryError mapToNewRecordCategoryError(RetrofitException retrofitException) throws IOException ;
}
