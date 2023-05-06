package dev.latvian.apps.webutils.html;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class TagUtils {
	private static final HashMap<Character, String> htmlEncodeChars = new HashMap<>();

	static {

		// Special characters for HTML
		htmlEncodeChars.put('&', "&amp;");
		htmlEncodeChars.put('<', "&lt;");
		htmlEncodeChars.put('>', "&gt;");
		htmlEncodeChars.put('"', "&quot;");

		htmlEncodeChars.put('Œ', "&OElig;");
		htmlEncodeChars.put('œ', "&oelig;");
		htmlEncodeChars.put('Š', "&Scaron;");
		htmlEncodeChars.put('š', "&scaron;");
		htmlEncodeChars.put('Ÿ', "&Yuml;");
		htmlEncodeChars.put('ˆ', "&circ;");
		htmlEncodeChars.put('˜', "&tilde;");
		htmlEncodeChars.put('\u2002', "&ensp;");
		htmlEncodeChars.put('\u2003', "&emsp;");
		htmlEncodeChars.put('\u2009', "&thinsp;");
		htmlEncodeChars.put('\u200C', "&zwnj;");
		htmlEncodeChars.put('\u200D', "&zwj;");
		htmlEncodeChars.put('\u200E', "&lrm;");
		htmlEncodeChars.put('\u200F', "&rlm;");
		htmlEncodeChars.put('–', "&ndash;");
		htmlEncodeChars.put('—', "&mdash;");
		htmlEncodeChars.put('‘', "&lsquo;");
		htmlEncodeChars.put('’', "&rsquo;");
		htmlEncodeChars.put('‚', "&sbquo;");
		htmlEncodeChars.put('“', "&ldquo;");
		htmlEncodeChars.put('”', "&rdquo;");
		htmlEncodeChars.put('„', "&bdquo;");
		htmlEncodeChars.put('†', "&dagger;");
		htmlEncodeChars.put('‡', "&Dagger;");
		htmlEncodeChars.put('‰', "&permil;");
		htmlEncodeChars.put('‹', "&lsaquo;");
		htmlEncodeChars.put('›', "&rsaquo;");
		htmlEncodeChars.put('€', "&euro;");

		// Character entity references for ISO 8859-1 characters
		htmlEncodeChars.put('\u00A0', "&nbsp;");
		htmlEncodeChars.put('¡', "&iexcl;");
		htmlEncodeChars.put('¢', "&cent;");
		htmlEncodeChars.put('£', "&pound;");
		htmlEncodeChars.put('¤', "&curren;");
		htmlEncodeChars.put('¥', "&yen;");
		htmlEncodeChars.put('¦', "&brvbar;");
		htmlEncodeChars.put('§', "&sect;");
		htmlEncodeChars.put('¨', "&uml;");
		htmlEncodeChars.put('©', "&copy;");
		htmlEncodeChars.put('ª', "&ordf;");
		htmlEncodeChars.put('«', "&laquo;");
		htmlEncodeChars.put('¬', "&not;");
		htmlEncodeChars.put('\u00AD', "&shy;");
		htmlEncodeChars.put('®', "&reg;");
		htmlEncodeChars.put('¯', "&macr;");
		htmlEncodeChars.put('°', "&deg;");
		htmlEncodeChars.put('±', "&plusmn;");
		htmlEncodeChars.put('²', "&sup2;");
		htmlEncodeChars.put('³', "&sup3;");
		htmlEncodeChars.put('´', "&acute;");
		htmlEncodeChars.put('µ', "&micro;");
		htmlEncodeChars.put('¶', "&para;");
		htmlEncodeChars.put('·', "&middot;");
		htmlEncodeChars.put('¸', "&cedil;");
		htmlEncodeChars.put('¹', "&sup1;");
		htmlEncodeChars.put('º', "&ordm;");
		htmlEncodeChars.put('»', "&raquo;");
		htmlEncodeChars.put('¼', "&frac14;");
		htmlEncodeChars.put('½', "&frac12;");
		htmlEncodeChars.put('¾', "&frac34;");
		htmlEncodeChars.put('¿', "&iquest;");
		htmlEncodeChars.put('À', "&Agrave;");
		htmlEncodeChars.put('Á', "&Aacute;");
		htmlEncodeChars.put('Â', "&Acirc;");
		htmlEncodeChars.put('Ã', "&Atilde;");
		htmlEncodeChars.put('Ä', "&Auml;");
		htmlEncodeChars.put('Å', "&Aring;");
		htmlEncodeChars.put('Æ', "&AElig;");
		htmlEncodeChars.put('Ç', "&Ccedil;");
		htmlEncodeChars.put('È', "&Egrave;");
		htmlEncodeChars.put('É', "&Eacute;");
		htmlEncodeChars.put('Ê', "&Ecirc;");
		htmlEncodeChars.put('Ë', "&Euml;");
		htmlEncodeChars.put('Ì', "&Igrave;");
		htmlEncodeChars.put('Í', "&Iacute;");
		htmlEncodeChars.put('Î', "&Icirc;");
		htmlEncodeChars.put('Ï', "&Iuml;");
		htmlEncodeChars.put('Ð', "&ETH;");
		htmlEncodeChars.put('Ñ', "&Ntilde;");
		htmlEncodeChars.put('Ò', "&Ograve;");
		htmlEncodeChars.put('Ó', "&Oacute;");
		htmlEncodeChars.put('Ô', "&Ocirc;");
		htmlEncodeChars.put('Õ', "&Otilde;");
		htmlEncodeChars.put('Ö', "&Ouml;");
		htmlEncodeChars.put('×', "&times;");
		htmlEncodeChars.put('Ø', "&Oslash;");
		htmlEncodeChars.put('Ù', "&Ugrave;");
		htmlEncodeChars.put('Ú', "&Uacute;");
		htmlEncodeChars.put('Û', "&Ucirc;");
		htmlEncodeChars.put('Ü', "&Uuml;");
		htmlEncodeChars.put('Ý', "&Yacute;");
		htmlEncodeChars.put('Þ', "&THORN;");
		htmlEncodeChars.put('ß', "&szlig;");
		htmlEncodeChars.put('à', "&agrave;");
		htmlEncodeChars.put('á', "&aacute;");
		htmlEncodeChars.put('â', "&acirc;");
		htmlEncodeChars.put('ã', "&atilde;");
		htmlEncodeChars.put('ä', "&auml;");
		htmlEncodeChars.put('å', "&aring;");
		htmlEncodeChars.put('æ', "&aelig;");
		htmlEncodeChars.put('ç', "&ccedil;");
		htmlEncodeChars.put('è', "&egrave;");
		htmlEncodeChars.put('é', "&eacute;");
		htmlEncodeChars.put('ê', "&ecirc;");
		htmlEncodeChars.put('ë', "&euml;");
		htmlEncodeChars.put('ì', "&igrave;");
		htmlEncodeChars.put('í', "&iacute;");
		htmlEncodeChars.put('î', "&icirc;");
		htmlEncodeChars.put('ï', "&iuml;");
		htmlEncodeChars.put('ð', "&eth;");
		htmlEncodeChars.put('ñ', "&ntilde;");
		htmlEncodeChars.put('ò', "&ograve;");
		htmlEncodeChars.put('ó', "&oacute;");
		htmlEncodeChars.put('ô', "&ocirc;");
		htmlEncodeChars.put('õ', "&otilde;");
		htmlEncodeChars.put('ö', "&ouml;");
		htmlEncodeChars.put('÷', "&divide;");
		htmlEncodeChars.put('ø', "&oslash;");
		htmlEncodeChars.put('ù', "&ugrave;");
		htmlEncodeChars.put('ú', "&uacute;");
		htmlEncodeChars.put('û', "&ucirc;");
		htmlEncodeChars.put('ü', "&uuml;");
		htmlEncodeChars.put('ý', "&yacute;");
		htmlEncodeChars.put('þ', "&thorn;");
		htmlEncodeChars.put('ÿ', "&yuml;");

		// Mathematical, Greek and Symbolic characters for HTML
		htmlEncodeChars.put('ƒ', "&fnof;");
		htmlEncodeChars.put('Α', "&Alpha;");
		htmlEncodeChars.put('Β', "&Beta;");
		htmlEncodeChars.put('Γ', "&Gamma;");
		htmlEncodeChars.put('Δ', "&Delta;");
		htmlEncodeChars.put('Ε', "&Epsilon;");
		htmlEncodeChars.put('Ζ', "&Zeta;");
		htmlEncodeChars.put('Η', "&Eta;");
		htmlEncodeChars.put('Θ', "&Theta;");
		htmlEncodeChars.put('Ι', "&Iota;");
		htmlEncodeChars.put('Κ', "&Kappa;");
		htmlEncodeChars.put('Λ', "&Lambda;");
		htmlEncodeChars.put('Μ', "&Mu;");
		htmlEncodeChars.put('Ν', "&Nu;");
		htmlEncodeChars.put('Ξ', "&Xi;");
		htmlEncodeChars.put('Ο', "&Omicron;");
		htmlEncodeChars.put('Π', "&Pi;");
		htmlEncodeChars.put('Ρ', "&Rho;");
		htmlEncodeChars.put('Σ', "&Sigma;");
		htmlEncodeChars.put('Τ', "&Tau;");
		htmlEncodeChars.put('Υ', "&Upsilon;");
		htmlEncodeChars.put('Φ', "&Phi;");
		htmlEncodeChars.put('Χ', "&Chi;");
		htmlEncodeChars.put('Ψ', "&Psi;");
		htmlEncodeChars.put('Ω', "&Omega;");
		htmlEncodeChars.put('α', "&alpha;");
		htmlEncodeChars.put('β', "&beta;");
		htmlEncodeChars.put('γ', "&gamma;");
		htmlEncodeChars.put('δ', "&delta;");
		htmlEncodeChars.put('ε', "&epsilon;");
		htmlEncodeChars.put('ζ', "&zeta;");
		htmlEncodeChars.put('η', "&eta;");
		htmlEncodeChars.put('θ', "&theta;");
		htmlEncodeChars.put('ι', "&iota;");
		htmlEncodeChars.put('κ', "&kappa;");
		htmlEncodeChars.put('λ', "&lambda;");
		htmlEncodeChars.put('μ', "&mu;");
		htmlEncodeChars.put('ν', "&nu;");
		htmlEncodeChars.put('ξ', "&xi;");
		htmlEncodeChars.put('ο', "&omicron;");
		htmlEncodeChars.put('π', "&pi;");
		htmlEncodeChars.put('ρ', "&rho;");
		htmlEncodeChars.put('ς', "&sigmaf;");
		htmlEncodeChars.put('σ', "&sigma;");
		htmlEncodeChars.put('τ', "&tau;");
		htmlEncodeChars.put('υ', "&upsilon;");
		htmlEncodeChars.put('φ', "&phi;");
		htmlEncodeChars.put('χ', "&chi;");
		htmlEncodeChars.put('ψ', "&psi;");
		htmlEncodeChars.put('ω', "&omega;");
		htmlEncodeChars.put('ϑ', "&thetasym;");
		htmlEncodeChars.put('ϒ', "&upsih;");
		htmlEncodeChars.put('ϖ', "&piv;");
		htmlEncodeChars.put('•', "&bull;");
		htmlEncodeChars.put('…', "&hellip;");
		htmlEncodeChars.put('′', "&prime;");
		htmlEncodeChars.put('″', "&Prime;");
		htmlEncodeChars.put('‾', "&oline;");
		htmlEncodeChars.put('⁄', "&frasl;");
		htmlEncodeChars.put('℘', "&weierp;");
		htmlEncodeChars.put('ℑ', "&image;");
		htmlEncodeChars.put('ℜ', "&real;");
		htmlEncodeChars.put('™', "&trade;");
		htmlEncodeChars.put('ℵ', "&alefsym;");
		htmlEncodeChars.put('←', "&larr;");
		htmlEncodeChars.put('↑', "&uarr;");
		htmlEncodeChars.put('→', "&rarr;");
		htmlEncodeChars.put('↓', "&darr;");
		htmlEncodeChars.put('↔', "&harr;");
		htmlEncodeChars.put('↵', "&crarr;");
		htmlEncodeChars.put('⇐', "&lArr;");
		htmlEncodeChars.put('⇑', "&uArr;");
		htmlEncodeChars.put('⇒', "&rArr;");
		htmlEncodeChars.put('⇓', "&dArr;");
		htmlEncodeChars.put('⇔', "&hArr;");
		htmlEncodeChars.put('∀', "&forall;");
		htmlEncodeChars.put('∂', "&part;");
		htmlEncodeChars.put('∃', "&exist;");
		htmlEncodeChars.put('∅', "&empty;");
		htmlEncodeChars.put('∇', "&nabla;");
		htmlEncodeChars.put('∈', "&isin;");
		htmlEncodeChars.put('∉', "&notin;");
		htmlEncodeChars.put('∋', "&ni;");
		htmlEncodeChars.put('∏', "&prod;");
		htmlEncodeChars.put('∑', "&sum;");
		htmlEncodeChars.put('−', "&minus;");
		htmlEncodeChars.put('∗', "&lowast;");
		htmlEncodeChars.put('√', "&radic;");
		htmlEncodeChars.put('∝', "&prop;");
		htmlEncodeChars.put('∞', "&infin;");
		htmlEncodeChars.put('∠', "&ang;");
		htmlEncodeChars.put('∧', "&and;");
		htmlEncodeChars.put('∨', "&or;");
		htmlEncodeChars.put('∩', "&cap;");
		htmlEncodeChars.put('∪', "&cup;");
		htmlEncodeChars.put('∫', "&int;");
		htmlEncodeChars.put('∴', "&there4;");
		htmlEncodeChars.put('∼', "&sim;");
		htmlEncodeChars.put('≅', "&cong;");
		htmlEncodeChars.put('≈', "&asymp;");
		htmlEncodeChars.put('≠', "&ne;");
		htmlEncodeChars.put('≡', "&equiv;");
		htmlEncodeChars.put('≤', "&le;");
		htmlEncodeChars.put('≥', "&ge;");
		htmlEncodeChars.put('⊂', "&sub;");
		htmlEncodeChars.put('⊃', "&sup;");
		htmlEncodeChars.put('⊄', "&nsub;");
		htmlEncodeChars.put('⊆', "&sube;");
		htmlEncodeChars.put('⊇', "&supe;");
		htmlEncodeChars.put('⊕', "&oplus;");
		htmlEncodeChars.put('⊗', "&otimes;");
		htmlEncodeChars.put('⊥', "&perp;");
		htmlEncodeChars.put('⋅', "&sdot;");
		htmlEncodeChars.put('⌈', "&lceil;");
		htmlEncodeChars.put('⌉', "&rceil;");
		htmlEncodeChars.put('⌊', "&lfloor;");
		htmlEncodeChars.put('⌋', "&rfloor;");
		htmlEncodeChars.put('〈', "&lang;");
		htmlEncodeChars.put('〉', "&rang;");
		htmlEncodeChars.put('◊', "&loz;");
		htmlEncodeChars.put('♠', "&spades;");
		htmlEncodeChars.put('♣', "&clubs;");
		htmlEncodeChars.put('♥', "&hearts;");
		htmlEncodeChars.put('♦', "&diams;");
	}

	public static void encode(Writer writer, String source) throws IOException {
		writer.write(encode(source));
	}

	public static String encode(String source) {
		if (source == null || source.isEmpty()) {
			return source;
		}

		StringBuilder encoded_string = null;
		char[] string_to_encode_array = source.toCharArray();
		int last_match = -1;
		int difference = 0;

		for (int i = 0; i < string_to_encode_array.length; i++) {
			char char_to_encode = string_to_encode_array[i];

			if (TagUtils.htmlEncodeChars.containsKey(char_to_encode)) {

				if (encoded_string == null) {

					encoded_string = new StringBuilder(source.length());
				}
				difference = i - (last_match + 1);
				if (difference > 0) {
					encoded_string.append(string_to_encode_array, last_match + 1, difference);
				}
				encoded_string.append(TagUtils.htmlEncodeChars.get(char_to_encode));
				last_match = i;
			}
		}

		if (encoded_string == null) {
			return source;
		} else {
			difference = string_to_encode_array.length - (last_match + 1);
			if (difference > 0) {
				encoded_string.append(string_to_encode_array, last_match + 1, difference);
			}
			return encoded_string.toString();
		}
	}

	public static void writeAttributes(Writer writer, @Nullable Map<String, String> attributes) throws IOException {
		if (attributes != null) {
			for (Map.Entry<String, String> entry : attributes.entrySet()) {
				writer.write(' ');
				writer.write(entry.getKey());

				if (!entry.getValue().equals("<NO_VALUE>")) {
					writer.write("=\"");
					encode(writer, entry.getValue());
					writer.write('"');
				}
			}
		}
	}
}
