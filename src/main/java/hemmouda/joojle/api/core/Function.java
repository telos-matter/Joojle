package hemmouda.joojle.api.core;

import hemmouda.joojle.api.Searcher;

import java.lang.reflect.Executable;

/**
 * <p>Represents a function.</p>
 * <p>Constructors are also functions. And
 * they are distinguished by the {@link #isConstructor}
 * field.</p>
 */
public record Function (Executable executable, boolean isConstructor, String signature) {

	public Function (Executable executable, boolean isConstructor, String signature) {
		if (executable == null) {
			throw new AssertionError("Cannot be null.");
		}

		this.executable = executable;
		this.isConstructor = isConstructor;
		this.signature = Searcher.forgeSignature(executable); // TODO is this where we should get this?
	}

	@Override
	public String toString() {
		// TODO recheck
		return executable.toString() +((isConstructor)? " [CONSTRUCTOR]" : "");
	}
	
}
