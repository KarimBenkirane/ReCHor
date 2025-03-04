package ch.epfl.rechor.jounrey;

import ch.epfl.rechor.Preconditions;

public class PackedCriteria {
    private PackedCriteria() {
    }

    public static long pack(int arrMins, int changes, int payload) {
        Preconditions.checkArgument(arrMins >= -240 && arrMins < 2880);
        Preconditions.checkArgument((changes >> 7) == 0);
        long result = 0;
        long translatedMinutes = arrMins + 240;
        result |= translatedMinutes << 39;
        result |= (long) changes << 32;
        result |= payload;

        return result;
    }

    public static boolean hasDepMins(long criteria) {
        return criteria >> 51 != 0;
    }

    public static int depMins(long criteria) {
        Preconditions.checkArgument(hasDepMins(criteria));
        return (int) (criteria >> 51);
    }
}
