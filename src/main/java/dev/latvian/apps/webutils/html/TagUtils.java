package dev.latvian.apps.webutils.html;

import dev.latvian.apps.webutils.ansi.Ansi;
import dev.latvian.apps.webutils.ansi.AnsiComponent;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class TagUtils {
	public static final int[] ANSI_COLORS = {
			13, // MAGENTA
			5, // PURPLE
			12, // BLUE
			14, // CYAN
	};

	public static String encode(char c) {
		return switch (c) {
			case '&' -> "&amp;";
			case '<' -> "&lt;";
			case '>' -> "&gt;";
			case '"' -> "&quot;";
			case 'Œ' -> "&OElig;";
			case 'œ' -> "&oelig;";
			case 'Š' -> "&Scaron;";
			case 'š' -> "&scaron;";
			case 'Ÿ' -> "&Yuml;";
			case 'ˆ' -> "&circ;";
			case '˜' -> "&tilde;";
			case '\u2002' -> "&ensp;";
			case '\u2003' -> "&emsp;";
			case '\u2009' -> "&thinsp;";
			case '\u200C' -> "&zwnj;";
			case '\u200D' -> "&zwj;";
			case '\u200E' -> "&lrm;";
			case '\u200F' -> "&rlm;";
			case '–' -> "&ndash;";
			case '—' -> "&mdash;";
			case '‘' -> "&lsquo;";
			case '’' -> "&rsquo;";
			case '‚' -> "&sbquo;";
			case '“' -> "&ldquo;";
			case '”' -> "&rdquo;";
			case '„' -> "&bdquo;";
			case '†' -> "&dagger;";
			case '‡' -> "&Dagger;";
			case '‰' -> "&permil;";
			case '‹' -> "&lsaquo;";
			case '›' -> "&rsaquo;";
			case '€' -> "&euro;";
			case '\u00A0' -> "&nbsp;";
			case '¡' -> "&iexcl;";
			case '¢' -> "&cent;";
			case '£' -> "&pound;";
			case '¤' -> "&curren;";
			case '¥' -> "&yen;";
			case '¦' -> "&brvbar;";
			case '§' -> "&sect;";
			case '¨' -> "&uml;";
			case '©' -> "&copy;";
			case 'ª' -> "&ordf;";
			case '«' -> "&laquo;";
			case '¬' -> "&not;";
			case '\u00AD' -> "&shy;";
			case '®' -> "&reg;";
			case '¯' -> "&macr;";
			case '°' -> "&deg;";
			case '±' -> "&plusmn;";
			case '²' -> "&sup2;";
			case '³' -> "&sup3;";
			case '´' -> "&acute;";
			case 'µ' -> "&micro;";
			case '¶' -> "&para;";
			case '·' -> "&middot;";
			case '¸' -> "&cedil;";
			case '¹' -> "&sup1;";
			case 'º' -> "&ordm;";
			case '»' -> "&raquo;";
			case '¼' -> "&frac14;";
			case '½' -> "&frac12;";
			case '¾' -> "&frac34;";
			case '¿' -> "&iquest;";
			case 'À' -> "&Agrave;";
			case 'Á' -> "&Aacute;";
			case 'Â' -> "&Acirc;";
			case 'Ã' -> "&Atilde;";
			case 'Ä' -> "&Auml;";
			case 'Å' -> "&Aring;";
			case 'Æ' -> "&AElig;";
			case 'Ç' -> "&Ccedil;";
			case 'È' -> "&Egrave;";
			case 'É' -> "&Eacute;";
			case 'Ê' -> "&Ecirc;";
			case 'Ë' -> "&Euml;";
			case 'Ì' -> "&Igrave;";
			case 'Í' -> "&Iacute;";
			case 'Î' -> "&Icirc;";
			case 'Ï' -> "&Iuml;";
			case 'Ð' -> "&ETH;";
			case 'Ñ' -> "&Ntilde;";
			case 'Ò' -> "&Ograve;";
			case 'Ó' -> "&Oacute;";
			case 'Ô' -> "&Ocirc;";
			case 'Õ' -> "&Otilde;";
			case 'Ö' -> "&Ouml;";
			case '×' -> "&times;";
			case 'Ø' -> "&Oslash;";
			case 'Ù' -> "&Ugrave;";
			case 'Ú' -> "&Uacute;";
			case 'Û' -> "&Ucirc;";
			case 'Ü' -> "&Uuml;";
			case 'Ý' -> "&Yacute;";
			case 'Þ' -> "&THORN;";
			case 'ß' -> "&szlig;";
			case 'à' -> "&agrave;";
			case 'á' -> "&aacute;";
			case 'â' -> "&acirc;";
			case 'ã' -> "&atilde;";
			case 'ä' -> "&auml;";
			case 'å' -> "&aring;";
			case 'æ' -> "&aelig;";
			case 'ç' -> "&ccedil;";
			case 'è' -> "&egrave;";
			case 'é' -> "&eacute;";
			case 'ê' -> "&ecirc;";
			case 'ë' -> "&euml;";
			case 'ì' -> "&igrave;";
			case 'í' -> "&iacute;";
			case 'î' -> "&icirc;";
			case 'ï' -> "&iuml;";
			case 'ð' -> "&eth;";
			case 'ñ' -> "&ntilde;";
			case 'ò' -> "&ograve;";
			case 'ó' -> "&oacute;";
			case 'ô' -> "&ocirc;";
			case 'õ' -> "&otilde;";
			case 'ö' -> "&ouml;";
			case '÷' -> "&divide;";
			case 'ø' -> "&oslash;";
			case 'ù' -> "&ugrave;";
			case 'ú' -> "&uacute;";
			case 'û' -> "&ucirc;";
			case 'ü' -> "&uuml;";
			case 'ý' -> "&yacute;";
			case 'þ' -> "&thorn;";
			case 'ÿ' -> "&yuml;";
			case 'ƒ' -> "&fnof;";
			case 'Α' -> "&Alpha;";
			case 'Β' -> "&Beta;";
			case 'Γ' -> "&Gamma;";
			case 'Δ' -> "&Delta;";
			case 'Ε' -> "&Epsilon;";
			case 'Ζ' -> "&Zeta;";
			case 'Η' -> "&Eta;";
			case 'Θ' -> "&Theta;";
			case 'Ι' -> "&Iota;";
			case 'Κ' -> "&Kappa;";
			case 'Λ' -> "&Lambda;";
			case 'Μ' -> "&Mu;";
			case 'Ν' -> "&Nu;";
			case 'Ξ' -> "&Xi;";
			case 'Ο' -> "&Omicron;";
			case 'Π' -> "&Pi;";
			case 'Ρ' -> "&Rho;";
			case 'Σ' -> "&Sigma;";
			case 'Τ' -> "&Tau;";
			case 'Υ' -> "&Upsilon;";
			case 'Φ' -> "&Phi;";
			case 'Χ' -> "&Chi;";
			case 'Ψ' -> "&Psi;";
			case 'Ω' -> "&Omega;";
			case 'α' -> "&alpha;";
			case 'β' -> "&beta;";
			case 'γ' -> "&gamma;";
			case 'δ' -> "&delta;";
			case 'ε' -> "&epsilon;";
			case 'ζ' -> "&zeta;";
			case 'η' -> "&eta;";
			case 'θ' -> "&theta;";
			case 'ι' -> "&iota;";
			case 'κ' -> "&kappa;";
			case 'λ' -> "&lambda;";
			case 'μ' -> "&mu;";
			case 'ν' -> "&nu;";
			case 'ξ' -> "&xi;";
			case 'ο' -> "&omicron;";
			case 'π' -> "&pi;";
			case 'ρ' -> "&rho;";
			case 'ς' -> "&sigmaf;";
			case 'σ' -> "&sigma;";
			case 'τ' -> "&tau;";
			case 'υ' -> "&upsilon;";
			case 'φ' -> "&phi;";
			case 'χ' -> "&chi;";
			case 'ψ' -> "&psi;";
			case 'ω' -> "&omega;";
			case 'ϑ' -> "&thetasym;";
			case 'ϒ' -> "&upsih;";
			case 'ϖ' -> "&piv;";
			case '•' -> "&bull;";
			case '…' -> "&hellip;";
			case '′' -> "&prime;";
			case '″' -> "&Prime;";
			case '‾' -> "&oline;";
			case '⁄' -> "&frasl;";
			case '℘' -> "&weierp;";
			case 'ℑ' -> "&image;";
			case 'ℜ' -> "&real;";
			case '™' -> "&trade;";
			case 'ℵ' -> "&alefsym;";
			case '←' -> "&larr;";
			case '↑' -> "&uarr;";
			case '→' -> "&rarr;";
			case '↓' -> "&darr;";
			case '↔' -> "&harr;";
			case '↵' -> "&crarr;";
			case '⇐' -> "&lArr;";
			case '⇑' -> "&uArr;";
			case '⇒' -> "&rArr;";
			case '⇓' -> "&dArr;";
			case '⇔' -> "&hArr;";
			case '∀' -> "&forall;";
			case '∂' -> "&part;";
			case '∃' -> "&exist;";
			case '∅' -> "&empty;";
			case '∇' -> "&nabla;";
			case '∈' -> "&isin;";
			case '∉' -> "&notin;";
			case '∋' -> "&ni;";
			case '∏' -> "&prod;";
			case '∑' -> "&sum;";
			case '−' -> "&minus;";
			case '∗' -> "&lowast;";
			case '√' -> "&radic;";
			case '∝' -> "&prop;";
			case '∞' -> "&infin;";
			case '∠' -> "&ang;";
			case '∧' -> "&and;";
			case '∨' -> "&or;";
			case '∩' -> "&cap;";
			case '∪' -> "&cup;";
			case '∫' -> "&int;";
			case '∴' -> "&there4;";
			case '∼' -> "&sim;";
			case '≅' -> "&cong;";
			case '≈' -> "&asymp;";
			case '≠' -> "&ne;";
			case '≡' -> "&equiv;";
			case '≤' -> "&le;";
			case '≥' -> "&ge;";
			case '⊂' -> "&sub;";
			case '⊃' -> "&sup;";
			case '⊄' -> "&nsub;";
			case '⊆' -> "&sube;";
			case '⊇' -> "&supe;";
			case '⊕' -> "&oplus;";
			case '⊗' -> "&otimes;";
			case '⊥' -> "&perp;";
			case '⋅' -> "&sdot;";
			case '⌈' -> "&lceil;";
			case '⌉' -> "&rceil;";
			case '⌊' -> "&lfloor;";
			case '⌋' -> "&rfloor;";
			case '〈' -> "&lang;";
			case '〉' -> "&rang;";
			case '◊' -> "&loz;";
			case '♠' -> "&spades;";
			case '♣' -> "&clubs;";
			case '♥' -> "&hearts;";
			case '♦' -> "&diams;";
			default -> "";
		};
	}

	public static String encode(String source) {
		if (source == null || source.isEmpty()) {
			return source;
		}

		StringBuilder builder = null;
		var chars = source.toCharArray();

		for (int i = 0; i < chars.length; i++) {
			var encoded = encode(chars[i]);

			if (!encoded.isEmpty()) {
				if (builder == null) {
					builder = new StringBuilder(source.length());
					builder.append(chars, 0, i);
				}

				builder.append(encoded);
			} else if (builder != null) {
				builder.append(chars[i]);
			}
		}

		return builder == null ? source : builder.toString();
	}

	public static void writeAttributes(Writer writer, @Nullable Map<String, String> attributes) throws IOException {
		if (attributes != null) {
			for (var entry : attributes.entrySet()) {
				writer.write(' ');
				writer.write(entry.getKey());

				if (!entry.getValue().equals("<NO_VALUE>")) {
					writer.write("=\"");
					writer.write(encode(entry.getValue()));
					writer.write('"');
				}
			}
		}
	}

	public static void ansiAttributes(AnsiComponent component, @Nullable Map<String, String> attributes, int depth) {
		int col = ANSI_COLORS[depth % ANSI_COLORS.length];

		if (attributes != null) {
			for (var entry : attributes.entrySet()) {
				component.append(' ');
				component.append(Ansi.lime(entry.getKey()));

				if (!entry.getValue().equals("<NO_VALUE>")) {
					component.append(Ansi.of("=\"").color(col));
					component.append(Ansi.yellow(encode(entry.getValue())));
					component.append(Ansi.of("\"").color(col));
				}
			}
		}
	}
}
