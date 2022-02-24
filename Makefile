all: kotl

kotl:
	./gradlew assemble

# synTests:
# 	./gradlew test --rerun-tasks --tests 'tsql.synTests*' -i
#
# semTests:
# 	./gradlew test --rerun-tasks 'tsql.synTests*' -i
#
# fIntTests:
# 	./gradlew test --rerun-tasks 'tsql.fIntTests*' -i

clean:
	./gradlew clean
	rm *.s

.PHONY: all rules clean


