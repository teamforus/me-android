package io.forus.me.android.domain.exception;

import java.io.IOException;

import io.forus.me.android.domain.models.common.DetailsApiError;
import io.forus.me.android.domain.models.records.errors.BaseApiError;
import io.forus.me.android.domain.models.records.errors.EmailError;
import io.forus.me.android.domain.models.records.errors.NewRecordCategoryError;
import io.forus.me.android.domain.models.records.errors.NewRecordError;

public abstract class RetrofitExceptionMapper {


    public abstract EmailError mapToApiError(RetrofitException retrofitException) throws IOException;

    public abstract NewRecordError mapToNewRecordError(RetrofitException retrofitException) throws IOException;

    public abstract NewRecordCategoryError mapToNewRecordCategoryError(RetrofitException retrofitException) throws IOException ;

    public abstract BaseApiError mapToBaseApiError(RetrofitException retrofitException) throws IOException;

    public abstract DetailsApiError mapToDetailsApiError(RetrofitException retrofitException) throws IOException;
}
