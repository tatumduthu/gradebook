package edu.course.gradebook;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("GradeBook")
class GradeBookTest {

    private static final double DELTA = 0.0001;

    private GradeBook book;

    @BeforeEach
    void setUp() {
        book = new GradeBook();
    }

    @Test
    @DisplayName("records scores per student in the order they were added")
    void recordsScores() {
        book.addScore("Ada", 90);
        book.addScore("Ada", 80);
        book.addScore("Grace", 70);

        assertEquals(List.of("Ada", "Grace"), book.students());
        assertEquals(List.of(90, 80), book.scoresFor("Ada"));
    }

    @Test
    @DisplayName("rejects a blank student name")
    void rejectsBlankName() {
        assertThrows(IllegalArgumentException.class, () -> book.addScore("  ", 90));
    }

    @ParameterizedTest(name = "rejects score {0}")
    @DisplayName("rejects scores outside 0..100")
    @ValueSource(ints = { -1, 101, 250 })
    void rejectsOutOfRangeScores(int score) {
        assertThrows(IllegalArgumentException.class, () -> book.addScore("Ada", score));
    }

    @ParameterizedTest(name = "accepts score {0}")
    @DisplayName("accepts scores at the edges of the valid range")
    @ValueSource(ints = { 0, 100 })
    void acceptsEdgeScores(int score) {
        book.addScore("Ada", score);
        assertEquals(List.of(score), book.scoresFor("Ada"));
    }

    @Test
    @DisplayName("does not drop a score when a student has fewer than 3 scores")
    void noDropWithTwoScores() {
        book.addScore("Linus", 70);
        book.addScore("Linus", 80);
        assertEquals(75.0, book.finalAverage("Linus"), DELTA);
    }

    @Test
    @DisplayName("drops the lowest score when a student has 3 or more scores")
    void dropsLowestWithThreeScores() {
        book.addScore("Ada", 95);
        book.addScore("Ada", 60);
        book.addScore("Ada", 88);
        // 60 is dropped -> (95 + 88) / 2 = 91.5
        assertEquals(91.5, book.finalAverage("Ada"), DELTA);
    }

    @Test
    @DisplayName("assigns a letter grade from the final average")
    void letterGradeFromFinalAverage() {
        book.addScore("Grace", 90);
        book.addScore("Grace", 90);
        assertEquals("A", book.letterGradeFor("Grace"));
    }

    @Test
    @DisplayName("finds the top student")
    void topStudent() {
        book.addScore("Ada", 92);
        book.addScore("Ada", 96);
        book.addScore("Linus", 70);
        book.addScore("Linus", 75);
        assertEquals(Optional.of("Ada"), book.topStudent());
    }

    @Test
    @DisplayName("has no top student when empty")
    void noTopStudentWhenEmpty() {
        assertTrue(book.topStudent().isEmpty());
    }
}
