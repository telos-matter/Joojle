package hemmouda.joojle.api.core;

import hemmouda.joojle.api.Searcher;

import java.lang.reflect.Executable;

/**
 * <p>Represents a method.</p>
 * <p>Constructors are also methods. And
 * they are distinguished by the {@link #isConstructor}
 * field.</p>
 */
public record MethodRecord(
		Executable executable,
		boolean isConstructor,
		String signature
) {

	public MethodRecord(Executable executable, boolean isConstructor, String signature) {
		if (executable == null) {
			throw new AssertionError("Executable cannot be null.");
		}

		this.executable = executable;
		this.isConstructor = isConstructor;
		this.signature = SignatureForger.forgeSignature(executable); // TODO is this where we should get this?
	}

	public MethodRecord(Executable executable, boolean isConstructor) {
		this(executable, isConstructor, null);
	}

	@Override
	public String toString() {
		// TODO recheck
		return executable.toString() +((isConstructor)? " [CONSTRUCTOR]" : "");
	}
	
}
