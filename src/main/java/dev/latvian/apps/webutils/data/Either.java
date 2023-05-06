package dev.latvian.apps.webutils.data;

public abstract class Either<L, R> {
	public static <L, R> Either<L, R> left(L l) {
		return new Left<>(l);
	}

	public static <L, R> Either<L, R> right(R r) {
		return new Right<>(r);
	}

	public abstract L left();

	public abstract R right();

	public abstract boolean isLeft();

	public abstract boolean isRight();

	private static class Left<L, R> extends Either<L, R> {
		private final L left;

		public Left(L l) {
			left = l;
		}

		@Override
		public L left() {
			return left;
		}

		@Override
		public R right() {
			throw new NullPointerException("This is Left side!");
		}

		@Override
		public boolean isLeft() {
			return true;
		}

		@Override
		public boolean isRight() {
			return false;
		}

		@Override
		public String toString() {
			return "Left{" + left + '}';
		}
	}

	private static class Right<L, R> extends Either<L, R> {
		private final R right;

		public Right(R r) {
			right = r;
		}

		@Override
		public L left() {
			throw new NullPointerException("This is Right side!");
		}

		@Override
		public R right() {
			return right;
		}

		@Override
		public boolean isLeft() {
			return false;
		}

		@Override
		public boolean isRight() {
			return true;
		}

		@Override
		public String toString() {
			return "Right{" + right + '}';
		}
	}
}
