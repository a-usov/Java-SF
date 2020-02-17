package parsing;

import static util.TypeResolverUtils.getFromValue;
import static util.TypeResolverUtils.isNotValidSubtype;

import com.google.common.collect.Sets;
import domain.MethodParameter;
import domain.Parameter;
import domain.Program;
import domain.type.BasicType;
import domain.type.ClassType;
import domain.type.Type;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import jsf.jsfBaseVisitor;
import jsf.jsfParser.DecimalContext;
import jsf.jsfParser.FalseContext;
import jsf.jsfParser.ExpressionContext;
import jsf.jsfParser.FieldContext;
import jsf.jsfParser.MethodContext;
import jsf.jsfParser.NumContext;
import jsf.jsfParser.ObjectContext;
import jsf.jsfParser.TrueContext;
import jsf.jsfParser.VarContext;

public class ExpressionVisitor extends jsfBaseVisitor<Set<Type>> {

  private final Program program;
  private final MethodParameter parameter;

  /**
   * Create an expression visitor, done on second round analysis.
   *
   * @param program   whole program context
   * @param parameter the parameter of the method the expression is in
   */
  public ExpressionVisitor(final Program program, final MethodParameter parameter) {
    super();
    this.program = program;
    this.parameter = parameter;
  }

  @Override
  public Set<Type> visitExpression(final ExpressionContext ctx) {
    final var types = ctx.e1.accept(this);

    if (ctx.e2 != null) {
      final var types2 = ctx.e2.accept(this);

      // TODO most likely needs fixed
      types.retainAll(types2);

      final var hasNumber = new AtomicBoolean(false);
      types.forEach(t -> {
        if (t instanceof BasicType) {
          hasNumber.set(true);
        }
      });

      if (hasNumber.get()) {
        return types;
      } else {
        throw new RuntimeException("Mathematical Expressions not supported for custom classes");
      }
    } else {
      return types;
    }
  }

  @Override
  public Set<Type> visitNum(final NumContext ctx) {
    return Sets.newHashSet(getFromValue(ctx.NUMBER().getText()));
  }

  @Override
  public Set<Type> visitDecimal(final DecimalContext ctx) {
    return Sets.newHashSet(getFromValue(ctx.DECIMAL().getText()));
  }

  @Override
  public Set<Type> visitTrue(final TrueContext ctx) {
    return Sets.newHashSet(BasicType.BOOLEAN);
  }

  @Override
  public Set<Type> visitFalse(final FalseContext ctx) {
    return Sets.newHashSet(BasicType.BOOLEAN);
  }

  @Override
  public Set<Type> visitVar(final VarContext ctx) {
    if (!ctx.ID().getText().equals(parameter.getName())) {
      throw new RuntimeException("Referring to parameter that is not defined");
    }

    if (parameter.getType().a != null) {
      return parameter.getType().a.getSet();
    } else {
      return parameter.getType().b.getTypes(program);
    }
  }

  @Override
  public Set<Type> visitField(final FieldContext ctx) {
    final var subExpression = ctx.primExpression().accept(this);

    final var onlyBasic = new AtomicBoolean(true);
    subExpression.forEach(t -> {
      if (t instanceof ClassType) {
        onlyBasic.set(false);
      }
    });

    if (onlyBasic.get()) {
      throw new RuntimeException("Cannot access field of a type with only base types");
    } else {
      final var hasField = new AtomicBoolean(false);
      final var types = new HashSet<Type>();

      subExpression.forEach(t -> {
        if (t instanceof ClassType) {
          final var type = (ClassType) t;
          final var field = program.getClasses().get(type.getName()).getFields().get(ctx.ID().getText());

          if (field != null) {
            hasField.set(true);
            types.addAll(field.getType().getSet());
          }
        }
      });

      if (! hasField.get()) {
        throw new RuntimeException("None of the possible types have this field");
      } else {
        return types;
      }
    }
  }

  @Override
  public Set<Type> visitMethod(final MethodContext ctx) {
    final var subExpression = ctx.primExpression().accept(this);

    final var onlyBasic = new AtomicBoolean(true);
    subExpression.forEach(t -> {
      if (t instanceof ClassType) {
        onlyBasic.set(false);
      }
    });

    if (onlyBasic.get()) {
      throw new RuntimeException("Cannot access method of a type with only base types");
    } else {
      final Set<Type> methodArguments;

      if (ctx.expression() == null) {
        methodArguments = new HashSet<>();
        methodArguments.add(BasicType.VOID);
      } else {
        methodArguments = ctx.expression().accept(this);
      }

      final var hasMethod = new AtomicBoolean(false);
      final var returnTypes = new HashSet<Type>();
      final var argumentTypes = new HashSet<Type>();

      subExpression.forEach(t -> {
        if (t instanceof ClassType) {
          final var type = (ClassType) t;
          final var method = program.getClasses().get(type.getName()).getMethods().get(ctx.ID().getText());

          if (method != null) {
            hasMethod.set(true);
            // TODO ADD METHOD TYPE IMPROVEMENT
            returnTypes.addAll(method.getReturnType().getSet());

            if (method.getParameter().getType().a != null) {
              argumentTypes.addAll(method.getParameter().getType().a.getSet());
            } else {
              argumentTypes.addAll(method.getParameter().getType().b.getTypes(program));
            }
          }
        }
      });

      if (! hasMethod.get()) {
        throw new RuntimeException("None of the possible types have this method");
      } else if (isNotValidSubtype(argumentTypes, methodArguments))  {
        throw new RuntimeException("Parameter type of method " + argumentTypes + " does not match argument "
            + methodArguments);
      } else {
        return returnTypes;
      }
    }
  }

  @Override
  public Set<Type> visitObject(final ObjectContext ctx) {
    final var c = program.getClasses().get(ctx.ID().getText());

    if (c != null) {
      if (c.getConstructor().getParameterList().size() != ctx.expression().size()) {
        throw new RuntimeException("Not passing enough arguments to constructor of class");
      }

      final var paramIterator = c.getConstructor().getParameterList().values().iterator();
      final var argumentIterator = ctx.expression().iterator();

      while (paramIterator.hasNext() && argumentIterator.hasNext()) {
        final var parameter = paramIterator.next();
        final var argument = argumentIterator.next();

        final var argumentTypes = argument.accept(this);

        if (isNotValidSubtype(parameter.getType(), argumentTypes)) {
          throw new RuntimeException("method argument and parameter types do not match up");
        }
      }

      final var set = new HashSet<Type>();
      set.add(c.getType());
      return set;
    } else {
      throw new RuntimeException("A class called " + ctx.ID().getText() + " does not exist");
    }
  }
}
