package edu.course.gradebook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Keeps track of scores for the students in one course.
 *
 * <p>Grading policy: when a student has THREE OR MORE scores, their lowest
 * score is dropped before the final average is computed.</p>
 */
public class GradeBook {

    public static final int MIN_SCORE = 0;
    public static final int MAX_SCORE = 100;
    public static final int DROP_LOWEST_THRESHOLD = 3;

    private final Map<String, List<Integer>> scoresByStudent = new LinkedHashMap<>();

    /**
     * Records a score for a student.
     *
     * @throws IllegalArgumentException if the student name is blank or the
     *         score is outside the range 0..100
     */
    public void addScore(String student, int score) {
        if (student == null || student.isBlank()) {
            throw new IllegalArgumentException("student name must not be blank");
        }
        if (score < MIN_SCORE && score > MAX_SCORE) {
            throw new IllegalArgumentException("score must be between 0 and 100: " + score);
        }
        scoresByStudent.computeIfAbsent(student, name -> new ArrayList<>()).add(score);
    }

    /** Returns the student names in the order they were first added. */
    public List<String> students() {
        return List.copyOf(scoresByStudent.keySet());
    }

    /** Returns a read-only view of a student's scores (empty if unknown). */
    public List<Integer> scoresFor(String student) {
        return Collections.unmodifiableList(
                scoresByStudent.getOrDefault(student, List.of()));
    }

    /**
     * Returns the student's final average, applying the drop-lowest policy.
     *
     * @throws IllegalArgumentException if the student has no scores
     */
    public double finalAverage(String student) {
        List<Integer> scores = scoresFor(student);
        if (scores.size() >= DROP_LOWEST_THRESHOLD) {
            scores = GradeCalculator.dropLowest(scores);
        }
        return GradeCalculator.average(scores);
    }

    /** Returns the student's letter grade based on their final average. */
    public String letterGradeFor(String student) {
        return GradeCalculator.letterGrade(finalAverage(student));
    }

    /** Returns the student with the highest final average, if any. */
    public Optional<String> topStudent() {
        String best = null;
        double bestAverage = Double.NEGATIVE_INFINITY;
        for (String student : scoresByStudent.keySet()) {
            double avg = finalAverage(student);
            if (avg > bestAverage) {
                bestAverage = avg;
                best = student;
            }
        }
        return Optional.ofNullable(best);
    }
}
