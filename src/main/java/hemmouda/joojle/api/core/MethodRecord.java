package hemmouda.joojle.api.core;

import hemmouda.joojle.api.SignatureForger;
import hemmouda.joojle.api.core.methodinfo.MethodScope;
import hemmouda.joojle.api.core.methodinfo.MethodKind;
import hemmouda.joojle.api.core.methodinfo.MethodVisibility;

import java.lang.reflect.Executable;

/**
 * <p>Holds and represents a method,
 * and the information needed about this
 * method.</p>
 * <p>Constructors are also methods.</p>
 */
// This shit is a class and not a
// record simply because of the
// canonical constructor constraint.
public class MethodRecord {

	/**
	 * The method or constructor itself.
	 */
	private final Executable executable;

	/**
	 * Is it a method or a constructor.
	 */
	private final MethodKind kind;
	/**
	 * The visibility of this method
	 */
	private final MethodVisibility visibility;
	/**
	 * Is this an instance method or a class method
	 */
	private final MethodScope scope;

	/**
	 * This methods' signature. What
	 * the user's query will be tested
	 * against.
	 */
	private final String signature;

	public MethodRecord(Executable executable, boolean isConstructor) {
		this.executable = executable;

		this.kind = (isConstructor)? MethodKind.CONSTRUCTOR : MethodKind.METHOD;
		this.visibility = MethodVisibility.getVisibility(executable);
		this.scope = MethodScope.getScope(executable);

		this.signature = SignatureForger.forgeSignature(executable);
	}

	public MethodKind getKind() {
		return kind;
	}

	public MethodVisibility getVisibility () {
		return visibility;
	}

	public MethodScope getScope () {
		return scope;
	}

	public String getSignature () {
		return signature;
	}

	/**
	 * @return the representation
	 * to show the user.
	 */
	@Override
	public String toString() {
		// TODO recheck
		// This is what we used to print
//		return executable.toString() +((isConstructor)? " [CONSTRUCTOR]" : "");
//		return executable.toString();
		return signature + " -> " + executable.toString() +" -> " + executable.getName(); // to test
	}
}
