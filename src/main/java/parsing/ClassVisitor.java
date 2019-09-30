package parsing;

import domain.ClassDeclaration;
import domain.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jsf.jsfBaseVisitor;
import jsf.jsfParser;

public class ClassVisitor extends jsfBaseVisitor<ClassDeclaration> {

  private final Set<String> classNames = new HashSet<>();
  private final List<Field> fields = new ArrayList<>();

  @Override
  public ClassDeclaration visitClassdecl(final jsfParser.ClassdeclContext ctx) {
    final String name = ctx.classlbl.getText();

    if (classNames.contains(name)) {
      System.out.println("Oops");
    } else {
      classNames.add(name);
    }

    final FieldVisitor fieldVisitor = new FieldVisitor(name);
    ctx.fielddecl().forEach(f -> fields.add(f.accept(fieldVisitor)));

    System.out.println(fields);
    return new ClassDeclaration(name);
  }

  public List<Field> getFields() {
    return fields;
  }

  public Set<String> getClassNames() {
    return classNames;
  }
}
