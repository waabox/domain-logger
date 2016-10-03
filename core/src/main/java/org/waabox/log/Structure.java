package org.waabox.log;

import org.apache.commons.lang3.Validate;

/** The structure defines a DomainLog hierarchy given a name and a stack.
 *
 * @author waabox (me[at]waabox[dot]org)
 */
public class Structure {

  /** The name of the structure, it's never null.*/
  private final String name;

  /** The , it's never null.*/
  private String stack;

  /** Creates a new instance of Structure.
   * @param aName the structure name, cannot be null.
   * @param initialStack the structure initial stack, cannot be null.
   */
  public Structure(final String aName, final String initialStack) {
    Validate.notNull(aName, "The name cannot be null");
    Validate.notNull(initialStack, "The stack cannot be null");
    name = aName;
    stack = initialStack;
  }

  /** Retrieves the name.
   * @return the name, never null.
   */
  public String getName() {
    return name;
  }

  /** Retrieves the stack.
   * @return the stack, never null.
   */
  public String getStack() {
    return stack;
  }

  /** Copy this structure into another.
   * @return the copy of this structure.
   */
  private Structure copy() {
    return new Structure(name, stack);
  }

  /** Retrieves an structure by its name.
   * @param name the name, cannot be null.
   * @return the Structure, never null.
   */
  public static Structure named(final String name) {
    return StructureLoader.named(name).copy();
  }

  /** Appends into the structure the variable name.
   * @param varName the variable name, cannot be null.
   * @param varValue the variable value, cannot be null.
   * @return this.
   */
  public Structure with(final String varName, final String varValue) {
    stack = stack.replace("${" + varName + "}", varValue);
    return this;
  }

}
