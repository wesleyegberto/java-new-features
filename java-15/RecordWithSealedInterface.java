public class RecordWithSealedInterface {
	public static void main(String[] args) {
		Expr expr = new TimesExpr(new ConstantExpr(6), new ConstantExpr(7));
		System.out.println("Answer: " + expr.evaluate());
	}
}

sealed interface Expr permits ConstantExpr, PlusExpr, TimesExpr, NegExpr {
	Integer evaluate();
}

record ConstantExpr(int i) implements Expr {
	public Integer evaluate() {
		return this.i;
	}
}

record PlusExpr(Expr a, Expr b) implements Expr {
	public Integer evaluate() {
		return a.evaluate() + b.evaluate();
	}
}

record TimesExpr(Expr a, Expr b) implements Expr {
	public Integer evaluate() {
		return a.evaluate() * b.evaluate();
	}
}

record NegExpr(Expr e) implements Expr {
	public Integer evaluate() {
		return -1 * this.e.evaluate();
	}
}
