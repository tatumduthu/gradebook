# Gradebook — Testing & CI/CD Lab

A tiny Java application that computes student averages and letter grades.
**It has bugs.** Your job is to:

1. Run the JUnit 5 tests in Eclipse and watch them fail.
2. Read the failures, find the bugs, and fix the code (do **not** change the tests).
3. Push your fixes to your own GitHub repository.
4. Watch GitHub Actions build, test, package — and deploy the app to your own computer.

```
 push to main ─► 1. Build ─► 2. Test ─► 3. Package ─► 4. Deploy (your laptop)
                 (GitHub cloud machines)               (self-hosted runner)
```

If any stage fails, every later stage is skipped. With the bugs still in the code,
the pipeline stops at **2. Test** — nothing broken ever gets deployed.

---

## What you need

| Tool | Notes |
|------|-------|
| **JDK 17 or newer** | e.g. [Eclipse Temurin](https://adoptium.net). Check with `java -version`. |
| **Eclipse IDE for Java Developers** | This package already includes Maven (m2e), Git (EGit) and JUnit 5 support. |
| **A GitHub account** | Free is fine. |

---

## Part 1 — Get your own copy of the repository

1. On the course repository page, click **Use this template → Create a new repository**.
   Name it `gradebook` under your own account. (A **private** repository is recommended —
   see the note in Part 4.)
2. In Eclipse: **File → Import → Git → Projects from Git (with smart import) → Clone URI**.
   Paste the HTTPS URL of *your* new repository (`https://github.com/<you>/gradebook.git`).
3. For the password, GitHub requires a **Personal Access Token**, not your account password.
   Create one at **GitHub → Settings → Developer settings → Personal access tokens**
   with permission to read and write repository contents. Check *Store in Secure Store*.
4. Finish the wizard. Eclipse detects the `pom.xml` and imports it as a Maven project.
   The first import downloads JUnit, so give it a minute.

> Prefer the command line? `git clone https://github.com/<you>/gradebook.git`, then in
> Eclipse use **File → Import → Maven → Existing Maven Projects**.

## Part 2 — Run the tests

* In the **Project Explorer**, right-click `src/test/java` → **Run As → JUnit Test**.
* The **JUnit** view shows a red bar. Expand the failures and read each message —
  `expected <A> but was <B>` tells you exactly what the code got wrong.
* Double-click a failing test to jump to it, then follow the method it calls into
  `src/main/java`.

You can also run the app itself: right-click `App.java` → **Run As → Java Application**.
(Spoiler: with the bugs in place, it crashes.)

From a terminal, `mvn test` runs the same tests (requires Maven installed separately).

### Rules

* Fix the code in `src/main/java` only. **Do not edit or delete the tests.**
* Several failing tests may share a single root cause. Fix the low-level class
  (`GradeCalculator`) first and re-run — some `GradeBook` failures may disappear on their own.
* Re-run until the bar is green: **29 tests, 0 failures**.

## Part 3 — Commit and push

1. **Window → Show View → Other → Git → Git Staging**.
2. Drag your changed files to **Staged Changes**, write a meaningful commit message
   (e.g. `Fix integer division in average()`), and click **Commit and Push**.
3. On GitHub, open the **Actions** tab of your repository and click the newest run.

**Try this:** commit and push *one* fix at a time. Each push triggers a new run, and you can
watch the number of failing tests drop in the run's **Summary** page (and in the
`test-reports` artifact).

When all tests pass, the run goes green through **3. Package**, and the built
`gradebook.jar` appears under **Artifacts** on the run's summary page.

## Part 4 — Deploy to your own computer

GitHub's cloud machines can't reach your laptop, so we install a **self-hosted runner**:
a small agent that runs on your computer, polls GitHub for work, and executes the
`deploy` job locally.

### 4a. Install the runner (one time)

1. In your repository: **Settings → Actions → Runners → New self-hosted runner**.
2. Choose your OS (macOS / Linux / Windows). GitHub shows a short list of commands —
   copy and run them **exactly as shown** in a terminal (PowerShell on Windows).
   They download the runner, and `config` registers it with your repository.
   Accept the defaults when prompted.
3. Start the runner:
   * macOS / Linux: `./run.sh`
   * Windows: `.\run.cmd`

   Leave that terminal open. It should print **Listening for Jobs**.
   Back on GitHub, the runner appears as **Idle**.

### 4b. Turn deployment on

**Settings → Secrets and variables → Actions → Variables tab → New repository variable**

* Name: `DEPLOY_LOCAL`
* Value: `true`

### 4c. Deploy

Push a commit (or use **Actions → CI/CD Pipeline → Run workflow**). After the first three
stages pass, the runner terminal on your laptop prints `Running job: 4. Deploy`.
The job:

1. downloads the `gradebook.jar` built in stage 3 (the exact artifact that was tested),
2. installs it to `~/gradebook-deploy/` (Windows: `C:\Users\<you>\gradebook-deploy\`),
   keeping the previous version as `gradebook.previous.jar`,
3. runs it as a smoke test.

Run it yourself any time:

```
java -jar ~/gradebook-deploy/gradebook.jar
```

You should see a report ending with `Top student: Ada`.

> **Security note:** a self-hosted runner executes workflow code on your computer.
> This workflow only runs on pushes to *your* `main` branch (never on pull requests
> from strangers), but GitHub still recommends self-hosted runners only for **private**
> repositories. Stop the runner (Ctrl+C) when you're not using it.

---

## Troubleshooting

| Symptom | Fix |
|---------|-----|
| Eclipse shows red errors on `org.junit...` imports | Right-click project → **Maven → Update Project** (tick *Force Update*). |
| "Run As → JUnit Test" missing | Make sure you imported as a Maven project (there's a small **M** on the project icon). |
| Push rejected: authentication failed | Use a Personal Access Token as the password, not your GitHub password. |
| Push rejected: "refusing to allow ... workflow" | Your token needs the **workflow** permission (only matters if you edit files under `.github/`). |
| Actions tab says workflows are disabled | Click **I understand my workflows, go ahead and enable them**. |
| Deploy job says *Waiting for a runner* | Your runner isn't running — start `run.sh` / `run.cmd`. |
| Deploy job is *skipped* | `DEPLOY_LOCAL` variable isn't set to `true`, or an earlier stage failed. |

## Project layout

```
gradebook/
├── .github/workflows/ci-cd.yml   ← the pipeline
├── pom.xml                       ← Maven build (Java 17, JUnit 5)
└── src/
    ├── main/java/edu/course/gradebook/
    │   ├── App.java              ← command-line entry point
    │   ├── GradeBook.java        ← per-student scores and grading policy
    │   └── GradeCalculator.java  ← math on lists of scores
    └── test/java/edu/course/gradebook/
        ├── GradeBookTest.java
        └── GradeCalculatorTest.java
```
