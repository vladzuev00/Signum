package by.aurorasoft.signum.protocol.wialon.model;

import lombok.Value;

import static java.lang.Byte.MIN_VALUE;
import static java.util.Arrays.stream;

@Value
public class ResponseCommandPackage implements Package {
    public static final String PACKAGE_PREFIX = "#AM#";

    Status status;

    public enum Status {
        NOT_DEFINED(MIN_VALUE), SUCCESS((byte) 1), ERROR((byte) 0);

        private final byte value;

        Status(byte value) {
            this.value = value;
        }

        public static Status findByValue(byte value) {
            return stream(values())
                    .filter(status -> status.value == value)
                    .findAny()
                    .orElse(NOT_DEFINED);
        }
    }
}
