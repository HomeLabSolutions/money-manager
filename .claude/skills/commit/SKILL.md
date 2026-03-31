---
name: commit
description: Run the full CI build then commit. Invoked manually before committing code.
disable-model-invocation: true
allowed-tools: Bash(./gradlew *), Bash(git *)
argument-hint: "[commit message or files]"
---

Before committing, run the full CI build to verify all checks pass:

1. Run from the project root:
   `./gradlew assembleDebug projectHealth ktlintCheck detektCheck check tomlCheck lint test`

2. If any task fails — stop. Report which task failed and what errors were shown. Do NOT commit.

3. If all tasks pass — proceed with the git commit following the standard commit workflow.

$ARGUMENTS
