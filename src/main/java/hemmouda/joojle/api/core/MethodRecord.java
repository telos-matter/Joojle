package hemmouda.joojle.api.core;

import hemmouda.joojle.api.QueryHandler;
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
	 * Name of this method
	 */
	private final String name;
	/**
	 * This methods' signature. What
	 * the user's query will be tested
	 * against.
	 */
	private final String signature;
	/**
	 * The query that perfectly matches
	 * this method
	 */
	private final String perfectQuery;

	public MethodRecord(Executable executable, boolean isConstructor) {
		this.executable = executable;

		this.kind = (isConstructor)? MethodKind.CONSTRUCTOR : MethodKind.METHOD;
		this.visibility = MethodVisibility.getVisibility(executable);
		this.scope = MethodScope.getScope(executable);

		this.name = executable.getName();
		this.signature = QueryHandler.simplifySignature(SignatureForger.forgeSignature(executable));
		this.perfectQuery = "%s %s %s".formatted(
				signature,
				QueryHandler.SIGNATURE_NAME_SEP,
				name);
	}

	public Executable getExecutable () {
		return executable;
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

	public String getName () {
		return name;
	}

	public String getPerfectQuery () {
		return perfectQuery;
	}

	@Override
	public int hashCode () {
		return signature.hashCode();
	}
}
