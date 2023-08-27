package joojle;

import java.lang.reflect.Executable;

public class Function {

	private Executable executable;
	private boolean isConstructor;
	private String signature;
	
	public Function (Executable executable, boolean isConstructor) {
		assert executable != null;
		
		this.executable = executable;
		this.isConstructor = isConstructor;
		this.signature = Searcher.forgeSignature(executable);
	}
	
	public String getSignature () {
		return this.signature;
	}
	
	@Override
	public String toString() {
		return getSignature();
	}
	
}
