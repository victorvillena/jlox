package org.willena.lox;

class Return extends RuntimeException {
    final Object value;

    Return(Object value) {

        // Disable suppressed exceptions and stack traces. This exception is used for control flow,
        // not for error handling, so we don't need the extra overhead.
        super(null, null, false, false);

        this.value = value;
    }
}
