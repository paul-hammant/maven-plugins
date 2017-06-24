# This branch was made like so

```
git branch maven-shade-plugin-only
git checkout maven-shade-plugin-only
find . -mindepth 1 -maxdepth 1 -type d | sed '/maven-shade-plugin/d' | sed '/.git/d' | xargs git rm -r
git commit -am "shade plugin only"
git push --all
```

In the root pom.xml, change the <scm> section to implcate this branch, and commit/push

TODO: how/what precisely?

# Where should developers do regular dev work?

On the master branch, which is guarded by CI

```
git checkout master
# edit, build, commit, push as normal
```

# Release workflow

```
git checkout maven-shade-plugin-only
git merge master
git status --porcelain | grep '^DU' | cut -d' ' -f2 | xargs git rm -rf
git commit -m "merge in advance of release"
```

Now, from here do the release plugin operations.

Note that changes to the versions in the POM that the release plugin makes are
not pushed back to the master branch by any process. TODO: decide workflow for versions
