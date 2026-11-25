package com.cognia.backend.util;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public final class TimeUtil {

    private TimeUtil() {
    }

    public static OffsetDateTime utc(OffsetDateTime value) {
        return value == null ? null : value.withOffsetSameInstant(ZoneOffset.UTC);
    }
}
