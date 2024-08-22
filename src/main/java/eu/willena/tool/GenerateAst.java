package eu.willena.tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast <output directory>");
            System.err.println("Current dir: " + System.getProperty("user.dir"));
            System.exit(64);
        }
        var outputDir = args[0];
        defineAst(outputDir, "Expr", Arrays.asList(
                "Binary   : Expr left, Token operator, Expr right",
                "Grouping : Expr expression",
                "Literal  : Object value",
                "Unary    : Token operator, Expr right"
        ));

        defineAst(outputDir, "Stmt", Arrays.asList(
                "Expression : Expr expression",
                "Print      : Expr expression"
        ));
    }

    private static void defineAst(
            String outputDir,
            String baseName,
            List<String> types
    ) throws IOException {
        var path = outputDir + "/" + baseName + ".java";
        var writer = new PrintWriter(path, "UTF-8");

        writer.println("package eu.willena.lox;");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class " + baseName + " {");

        // Visitor-pattern interface
        defineVisitor(writer, baseName, types);
        writer.println();

        // AST classes
        for (var type : types) {
            var className = type.split(":")[0].trim();
            var fields = type.split(":")[1].trim();
            defineType(writer, baseName, className, fields);
            writer.println();
        }

        // Visitor-pattern method
        writer.println("    abstract <R> R accept(Visitor<R> visitor);");

        writer.println("}");
        writer.close();
    }

    private static void defineVisitor(PrintWriter writer, String baseName, List<String> types) {
        writer.println("    interface Visitor<R> {");

        for (var type : types) {
            var typeName = type.split(":")[0].trim();
            writer.println(
                    "        R visit" + typeName + baseName + "(" + typeName + " " + baseName.toLowerCase() + ");"
            );
        }

        writer.println("    }");
    }

    private static void defineType(
            PrintWriter writer,
            String baseName,
            String className,
            String fieldList
    ) {
        // Class declaration
        writer.println("    static class " + className + " extends " + baseName + " {");

        // Constructor
        writer.println("        " + className + "(" + fieldList + ") { ");
        var fields = fieldList.split(", ");
        for (var field : fields) {
            var name = field.split(" ")[1];
            writer.println("            this." + name + " = " + name + ";");
        }
        writer.println("        }");

        // Visitor-pattern implementation
        writer.println();
        writer.println("        @Override");
        writer.println("        <R> R accept(Visitor<R> visitor) {");
        writer.println("            return visitor.visit" + className + baseName + "(this);");
        writer.println("        }");


        // Fields
        writer.println();
        for (var field : fields) {
            writer.println("        final " + field + ";");
        }

        writer.println("    }");
    }
}
