package io.github.spair.byond.dmi;

import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

final class CheckSupplierUtil {

    static <T, R> R returnIfNonNull(final T toCheck, final Function<T, R> toApply) {
        return Objects.nonNull(toCheck) ? toApply.apply(toCheck) : null;
    }

    static <T, R> R returnIfNonNullAndTrue(
            final T toCheck, final BooleanSupplier boolCheck, final Function<T, R> toApply) {
        return Objects.nonNull(toCheck) && boolCheck.getAsBoolean() ? toApply.apply(toCheck) : null;
    }

    private CheckSupplierUtil() {
    }
}
