SHELL := /bin/bash

JC = javac
JRE = java
FLAGS = -g




default: 
	@$(JC) $(FLAGS) EvoLib/*.java
	@$(JC) $(FLAGS) CharTest.java

CharTest:
	@$(JC) $(FLAGS) CharTest.java

check:
	@$(JRE) CharTest

clean:
	@rm -f EvoLib/*.class
	@rm -f *.class

.PHONY: default clean
