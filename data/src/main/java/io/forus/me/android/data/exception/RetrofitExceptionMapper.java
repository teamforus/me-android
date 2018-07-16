package io.forus.me.android.data.exception;

import java.io.IOException;

import io.forus.me.android.data.entity.common.ApiError;
import io.forus.me.android.domain.exception.RetrofitException;
import io.forus.me.android.domain.models.records.errors.NewRecordCategoryError;
import io.forus.me.android.domain.models.records.errors.NewRecordError;

public class RetrofitExceptionMapper extends io.forus.me.android.domain.exception.RetrofitExceptionMapper {

    public NewRecordError mapToNewRecordError(RetrofitException retrofitException) throws IOException {
        ApiError apiError = retrofitException.getErrorBodyAs(ApiError.class);
        return new NewRecordError(apiError.getMessage(), apiError.getErrors().getKey(), apiError.getErrors().getValue());
    }

    public NewRecordCategoryError mapToNewRecordCategoryError(RetrofitException retrofitException) throws IOException {
        ApiError apiError = retrofitException.getErrorBodyAs(ApiError.class);
        return new NewRecordCategoryError(apiError.getMessage(), apiError.getErrors().getName());
    }
}
