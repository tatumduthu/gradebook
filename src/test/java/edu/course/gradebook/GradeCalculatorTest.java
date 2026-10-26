package edu.course.gradebook;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("GradeCalculator")
class GradeCalculatorTest {

    private static final double DELTA = 0.0001;

    @Nested
    @DisplayName("average()")
    class Average {

        @Test
        @DisplayName("averages scores that divide evenly")
        void evenAverage() {
            assertEquals(85.0, GradeCalculator.average(List.of(80, 90)), DELTA);
        }

        @Test
        @DisplayName("keeps the fractional part of the average")
        void fractionalAverage() {
            assertEquals(87.5, GradeCalculator.average(List.of(90, 85)), DELTA);
        }

        @Test
        @DisplayName("rejects an empty list")
        void emptyListRejected() {
            assertThrows(IllegalArgumentException.class,
                    () -> GradeCalculator.average(List.of()));
        }
    }

    @Nested
    @DisplayName("highest() and lowest()")
    class HighestLowest {

        @Test
        @DisplayName("finds the highest score when it is in the middle")
        void highestInMiddle() {
            assertEquals(99, GradeCalculator.highest(List.of(70, 99, 85)));
        }

        @Test
        @DisplayName("finds the highest score when it is the last element")
        void highestAtEnd() {
            assertEquals(99, GradeCalculator.highest(List.of(70, 85, 99)));
        }

        @Test
        @DisplayName("finds the lowest score anywhere in the list")
        void lowest() {
            assertEquals(55, GradeCalculator.lowest(List.of(70, 85, 55)));
        }
    }

    @Nested
    @DisplayName("dropLowest()")
    class DropLowest {

        @Test
        @DisplayName("removes the lowest score")
        void removesLowest() {
            assertEquals(List.of(90, 85), GradeCalculator.dropLowest(List.of(90, 70, 85)));
        }

        @Test
        @DisplayName("does not modify the original list")
        void doesNotModifyOriginal() {
            List<Integer> original = new ArrayList<>(List.of(3, 1, 2));
            GradeCalculator.dropLowest(original);
            assertEquals(List.of(3, 1, 2), original);
        }
    }

    @Nested
    @DisplayName("letterGrade()")
    class LetterGrade {

        @ParameterizedTest(name = "{0} -> {1}")
        @DisplayName("maps scores well inside each band")
        @CsvSource({ "95, A", "85, B", "75, C", "65, D", "50, F" })
        void insideBands(double score, String expected) {
            assertEquals(expected, GradeCalculator.letterGrade(score));
        }

        @ParameterizedTest(name = "{0} -> {1}")
        @DisplayName("maps scores exactly on a boundary to the higher grade")
        @CsvSource({ "90, A", "80, B", "70, C", "60, D" })
        void onBoundaries(double score, String expected) {
            assertEquals(expected, GradeCalculator.letterGrade(score));
        }
    }
}
