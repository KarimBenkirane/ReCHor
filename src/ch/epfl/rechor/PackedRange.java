package ch.epfl.rechor;

public class PackedRange {
    private PackedRange() {
    }

    public static int pack(int startInclusive, int endExclusive) {
        Preconditions.checkArgument((startInclusive >> 24) == 0);
        int length = endExclusive - startInclusive;
        Preconditions.checkArgument((length >> 8) == 0);
        return Bits32_24_8.pack(startInclusive, length);
    }

    public static int length(int interval) {
        return Bits32_24_8.unpack8(interval);
    }

    public static int startInclusive(int interval) {
        return Bits32_24_8.unpack24(interval);
    }

    public static int endExclusive(int interval) {
        return startInclusive(interval) + length(interval);
    }
}
