package dev.latvian.apps.webutils.misc;

@FunctionalInterface
public interface StringToken {
	static StringToken of(String sequence) {
		return sequence.length() == 1 ? new CharacterToken(sequence.charAt(0)) : new SequenceToken(sequence.toCharArray());
	}

	StringToken SPACE = parser -> parser.isSpace(0) ? 1 : 0;
	StringToken SPACES = StringParser::countSpace;

	record OrToken(StringToken a, StringToken b) implements StringToken {
		@Override
		public int test(StringParser r) {
			int i = a.test(r);
			return i == 0 ? b.test(r) : i;
		}

		@Override
		public String toString() {
			return a + " or " + b;
		}
	}

	record CharacterToken(char c) implements StringToken {
		@Override
		public int test(StringParser r) {
			return r.peek() == c ? 1 : 0;
		}

		@Override
		public String toString() {
			return "'" + c + "'";
		}
	}

	record SequenceToken(char[] match) implements StringToken {
		@Override
		public int test(StringParser r) {
			if (r.cursor + match.length > r.input.length) {
				return 0;
			}

			for (int i = 0; i < match.length; i++) {
				if (r.input[r.cursor + i] != match[i]) {
					return 0;
				}
			}

			return match.length;
		}

		@Override
		public String toString() {
			return "'" + new String(match) + "'";
		}
	}

	int test(StringParser parser);

	default StringToken or(StringToken other) {
		return new OrToken(this, other);
	}
}
