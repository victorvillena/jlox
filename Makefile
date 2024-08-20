default: compile

compile:
	mvn compile

generate_ast: compile
	java -cp target/classes eu.willena.tool.GenerateAst src/main/java/eu/willena/lox

run: compile
	java -cp target/classes eu.willena.lox.Lox 1 + 1
