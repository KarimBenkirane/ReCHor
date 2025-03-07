package ch.epfl.rechor.journey;

import ch.epfl.rechor.Preconditions;

public class PackedCriteria {
    private PackedCriteria() {
    }

    public static long pack(int arrMins, int changes, int payload) {
        Preconditions.checkArgument(arrMins >= -240 && arrMins < 2880);
        Preconditions.checkArgument((changes >> 7) == 0);
        long result = 0L;
        long translatedMinutes = arrMins + 240L;
        result |= translatedMinutes << 39;
        result |= (long) changes << 32;
        result |= Integer.toUnsignedLong(payload);

        return result;
    }

    public static boolean hasDepMins(long criteria) {
        return (criteria >> 51) != 0;
    }

    public static int depMins(long criteria) {
        Preconditions.checkArgument(hasDepMins(criteria));
        int depMins = ((int) (criteria >> 51));
        return 4095 - depMins - 240;
    }

    public static int arrMins(long criteria) {
        return ((int) ((criteria >> 39) & 0xFFF)) - 240;
    }

    public static int changes(long criteria) {
        return (int) ((criteria >> 32) & 0x7F);
    }

    public static int payload(long criteria) {
        return (int) (criteria);
    }

    public static boolean dominatesOrIsEqual(long criteria1, long criteria2) {
        Preconditions.checkArgument(
                (hasDepMins(criteria1) && hasDepMins(criteria2)) ||
                        (!hasDepMins(criteria1) && !hasDepMins(criteria2))
        );
        if (!hasDepMins(criteria1)) {
            return (changes(criteria1) <= changes(criteria2)) &&
                    (arrMins(criteria1) <= arrMins(criteria2));
        }
        return (changes(criteria1) <= changes(criteria2)) &&
                (arrMins(criteria1) <= arrMins(criteria2)) &&
                (depMins(criteria1) >= depMins(criteria2));


    }

    public static long withoutDepMins(long criteria) {
        long mask = 0x7FFFFFFFFFFFFL;
        return criteria & mask;
    }

    public static long withDepMins(long criteria, int depMins1) {
        Preconditions.checkArgument(depMins1 >= -240 && depMins1 < 2880);
        criteria = withoutDepMins(criteria);
        depMins1 += 240;
        depMins1 = 4095 - depMins1;
        long depMins = (long) depMins1 << 51;
        return criteria | depMins;
    }

    public static long withAdditionalChange(long criteria) {
        long changes = changes(criteria) + 1;
        long mask = 0xFFFFFF80FFFFFFFFL;
        criteria = criteria & mask;
        changes = changes << 32;
        return criteria | changes;
    }

    public static long withPayload(long criteria, int payload1) {
        long mask = 0xFFFFFFFF00000000L;
        criteria = criteria & mask;
        return criteria | Integer.toUnsignedLong(payload1);
    }
}
