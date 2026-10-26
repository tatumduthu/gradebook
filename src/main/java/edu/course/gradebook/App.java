package edu.course.gradebook;

/**
 * Command-line entry point. Prints a grade report for a sample class.
 * The deployment pipeline runs this as a "smoke test" after installing the jar.
 *
 * Run it with:  java -jar gradebook.jar
 */
public class App {

    public static void main(String[] args) {
        GradeBook book = sampleGradeBook();
        System.out.println(report(book));
    }

    static GradeBook sampleGradeBook() {
        GradeBook book = new GradeBook();
        book.addScore("Ada", 95);
        book.addScore("Ada", 88);
        book.addScore("Ada", 92);

        book.addScore("Grace", 78);
        book.addScore("Grace", 85);
        book.addScore("Grace", 90);
        book.addScore("Grace", 60);

        book.addScore("Linus", 70);
        book.addScore("Linus", 65);
        return book;
    }

    static String report(GradeBook book) {
        String version = App.class.getPackage().getImplementationVersion();
        StringBuilder sb = new StringBuilder();
        sb.append("Gradebook report (version ")
          .append(version == null ? "dev" : version)
          .append(")\n");
        sb.append("--------------------------------------\n");
        for (String student : book.students()) {
            sb.append(String.format("%-10s %-20s avg=%6.2f  grade=%s%n",
                    student,
                    book.scoresFor(student),
                    book.finalAverage(student),
                    book.letterGradeFor(student)));
        }
        sb.append("--------------------------------------\n");
        sb.append("Top student: ").append(book.topStudent().orElse("(none)"));
        return sb.toString();
    }
}
