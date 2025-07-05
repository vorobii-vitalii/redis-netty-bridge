package io.vitaliivorobii.redis.netty.bridge.command.select;

import io.netty.util.AttributeKey;

public final class SelectedDatabase  {

    public static final AttributeKey<Integer> INSTANCE =
            AttributeKey.valueOf("selectedDatabase");


    private SelectedDatabase() {
        // utility classes should not be instantiated
    }

}
