# Pull Request

## Summary

Provide a concise description of the change. State what was modified and why. Avoid
restating the diff. The reader should understand the intent from this section alone.

## Related Issues

List the issues this PR closes or advances. Use GitHub's linking keywords so the
issues close automatically on merge.

- Closes #
- Relates to #

## Milestone

Select the milestone this change belongs to.

- [ ] Part 1 - Pure Spring Framework (`feature/01-pure-spring-core`)
- [ ] Part 2 - Spring Boot Platform (`feature/02-spring-boot-platform`)
- [ ] Part 3 - Spring Security Perimeter (`feature/03-spring-security-perimeter`)
- [ ] Cross-cutting (tooling, CI, documentation)

## Type of Change

Mark all that apply.

- [ ] Feature (new capability)
- [ ] Bug fix (corrects incorrect behavior)
- [ ] Refactor (no functional change)
- [ ] Performance (measurable improvement)
- [ ] Documentation
- [ ] Build or tooling
- [ ] Test coverage only

## Architectural Impact

Describe how this change affects the container, proxy, transaction, or web layers.
Explicitly note any of the following if applicable:

- New or removed Spring beans
- Changes to bean scope or lifecycle
- Changes to proxy generation (JDK vs CGLIB)
- Changes to transaction demarcation or propagation
- Changes to the Spring Boot ban in `hpg-core`
- Changes to public API of `hpg-core` or `hpg-domain`

If none of the above apply, state "No architectural impact."

## Implementation Notes

Record decisions that a reviewer would otherwise have to reverse-engineer. Examples:

- Why a specific propagation mode was chosen
- Why a lock retry strategy was applied at this layer
- Why a particular bean is stateless and singleton-scoped
- Any deviation from the pattern established by earlier issues

Reference relevant documentation in `docs/part1/` where applicable.

## Performance Evidence

Required for any change that touches concurrency, transactions, proxies, or persistence.
Delete this section only if the change is purely cosmetic or documentation.

| Metric | Before | After | Method |
|--------|--------|-------|--------|
| | | | JMH / `@RepeatedTest` / manual |

Include the raw command used to produce the numbers. If the change has no measurable
performance impact, state "No performance-relevant change" and briefly justify.

## Verification

List every step taken to verify the change. Check all that apply.

- [ ] `mvn -q clean verify` passes locally
- [ ] `mvn -q -pl hpg-core dependency:tree "-Dincludes=org.springframework.boot"` produces empty output
- [ ] Integration tests pass against a real PostgreSQL Testcontainer
- [ ] New unit tests cover the change
- [ ] New integration tests cover the change where relevant
- [ ] Documentation updated in `docs/part1/`
- [ ] No field injection introduced (ArchUnit rule passes)
- [ ] No new Spring Boot dependency added to `hpg-core`

## Concurrency and Correctness Checklist

Complete for any change that touches transactions, locking, or shared state.

- [ ] Transaction boundaries are explicit and documented
- [ ] Propagation mode is justified in the implementation notes
- [ ] Lock retry behavior is covered by a test
- [ ] Shared mutable state is either absent or properly synchronized
- [ ] Self-invocation risk has been checked and does not apply, or has been fixed
- [ ] Exception handling preserves the original cause

## Screenshots or Logs

Optional. Include only when they clarify behavior that is difficult to express in text.
Prefer pasted log excerpts over images.

## Reviewer Notes

Call out anything you want the reviewer to scrutinize. Examples:

- Specific edge case you were unsure about
- Trade-off you made and its rationale
- Follow-up work that is deliberately out of scope for this PR

## Checklist

- [ ] Commit messages follow Conventional Commits
- [ ] Branch is up to date with `main`
- [ ] No debugging artifacts, commented-out code, or stray `System.out` statements remain
- [ ] No secrets, credentials, or environment-specific values are committed
- [ ] Documentation reflects the final state of the change
- [ ] Related issues are linked above