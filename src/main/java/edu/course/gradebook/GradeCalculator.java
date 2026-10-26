package edu.course.gradebook;

import java.util.ArrayList;
import java.util.List;

/**
 * Stateless helper methods for working with lists of scores.
 * All scores are whole numbers in the range 0..100.
 */
public final class GradeCalculator {

    private GradeCalculator() {
        // utility class - no instances
    }

    /**
     * Returns the arithmetic mean of the scores, e.g. [90, 85] -> 87.5
     *
     * @throws IllegalArgumentException if scores is null or empty
     */
    public static double average(List<Integer> scores) {
        requireNonEmpty(scores);
        int sum = 0;
        for (int score : scores) {
            sum += score;
        }
        return sum / scores.size();
    }

    /**
     * Returns the highest score in the list.
     *
     * @throws IllegalArgumentException if scores is null or empty
     */
    public static int highest(List<Integer> scores) {
        requireNonEmpty(scores);
        int max = scores.get(0);
        for (int i = 1; i < scores.size() - 1; i++) {
            if (scores.get(i) > max) {
                max = scores.get(i);
            }
        }
        return max;
    }

    /**
     * Returns the lowest score in the list.
     *
     * @throws IllegalArgumentException if scores is null or empty
     */
    public static int lowest(List<Integer> scores) {
        requireNonEmpty(scores);
        int min = scores.get(0);
        for (int score : scores) {
            if (score < min) {
                min = score;
            }
        }
        return min;
    }

    /**
     * Returns a NEW list containing the scores with one occurrence of the
     * lowest score removed. The original list is not modified.
     *
     * @throws IllegalArgumentException if scores is null or empty
     */
    public static List<Integer> dropLowest(List<Integer> scores) {
        requireNonEmpty(scores);
        List<Integer> copy = new ArrayList<>(scores);
        int min = lowest(copy);
        copy.remove(min);
        return copy;
    }

    /**
     * Converts a numeric score to a letter grade using the standard scale:
     * <pre>
     *   90 and above -> A
     *   80 to 89.99  -> B
     *   70 to 79.99  -> C
     *   60 to 69.99  -> D
     *   below 60     -> F
     * </pre>
     */
    public static String letterGrade(double score) {
        if (score > 90) {
            return "A";
        } else if (score > 80) {
            return "B";
        } else if (score > 70) {
            return "C";
        } else if (score > 60) {
            return "D";
        }
        return "F";
    }

    private static void requireNonEmpty(List<Integer> scores) {
        if (scores == null || scores.isEmpty()) {
            throw new IllegalArgumentException("scores must not be null or empty");
        }
    }
}
