package kz.tastamat.jooq;

import org.jooq.tools.StringUtils;
import org.jooq.util.DefaultGeneratorStrategy;
import org.jooq.util.Definition;

import java.util.Arrays;

/**
 * Created by didar on 8/16/16.
 */
public class CustomStrategy extends DefaultGeneratorStrategy {

	@Override
	public String getJavaPackageName(Definition definition, Mode mode) {
		return super.getJavaPackageName(definition, mode).replace("public_", "main");
	}

	@Override
	public String getJavaIdentifier(Definition definition) {
		String outputName = cleanup(definition.getInputName());

		if (outputName.endsWith("_")) {
			outputName = outputName.substring(0, outputName.length() - 1);
		}
		return outputName.toUpperCase();
	}

	private static String cleanup(String name) {
		for (String prefix : Arrays.asList("tt_", "wx_")) {
			if (name.startsWith(prefix)) {
				return name.substring(prefix.length());
			}
		}
		return name;
	}

	@Override
	public String getJavaSetterName(Definition definition, Mode mode) {
		String outputName = super.getJavaSetterName(definition, mode);
		if (outputName.endsWith("_")) {
			outputName = outputName.substring(0, outputName.length() - 1);
		}
		return outputName;
	}

	@Override
	public String getJavaGetterName(Definition definition, Mode mode) {
		String outputName = super.getJavaGetterName(definition, mode);
		if (outputName.endsWith("_")) {
			outputName = outputName.substring(0, outputName.length() - 1);
		}
		return outputName;
	}

	@Override
	public String getJavaClassName(Definition definition, Mode mode) {
		StringBuilder result = new StringBuilder();
		String outputName = definition.getOutputName();
		int ix = outputName.indexOf("_");

		if (ix != -1) {
			String prefix = outputName.substring(0, ix);
			if (prefix.length() < 4) {
				outputName = outputName.substring(ix + 1);
			}
		}

		if (outputName.endsWith("_")) {
			outputName = outputName.substring(0, outputName.length() - 1);
		}

		result.append("Jq" + StringUtils.toCamelCase(outputName.replace(' ', '_').replace('-', '_').replace('.', '_')));


		if (mode == Mode.RECORD) {
			result.append("Record");
		} else if (mode == Mode.DAO) {
			result.append("Dao");
		} else if (mode == Mode.INTERFACE) {
			result.insert(0, "I");
		}

		return result.toString();
	}
}
