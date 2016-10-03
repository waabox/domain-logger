package org.waabox.log;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.*;
import java.util.regex.*;

import org.apache.commons.lang3.Validate;

/** DomainLog domain operations.
 *
 * @author waabox (me[at]waabox[dot]org)
 */
public class DomainLogger {

  /** The holds the instance., it's never null.*/
  private static ThreadLocal<Map<String, DomainLogger>> instance;

  static {
    instance = ThreadLocal
        .withInitial(new Supplier<Map<String, DomainLogger>>() {
          @Override
          public Map<String, DomainLogger> get() {
            return new ConcurrentHashMap<>();
          }
        });
  }

  /** The list of log entries, it's never null.*/
  private List<DomainLog> entries;

  /** The log context variables, it's never null.*/
  private Map<String, String> contextVariables;

  /** The root log entry.*/
  private DomainLog root;

  /** The set of declared stacks, it's never null.*/
  private Set<String> stacks;

  /** Checks if this log is enabled or not, it's never null.*/
  private boolean enabled;

  /** Creates a new instance of the logger.*/
  private DomainLogger() {
    entries = Collections.synchronizedList(new LinkedList<DomainLog>());
    contextVariables = new ConcurrentHashMap<>();
    root = new DomainLog(".");
    stacks = Collections.synchronizedSet(new HashSet<String>());
    enabled = true;
  }

  /** Retrieves the Domain log, never null.
   * @param appender the appender to retrieve, cannot be null.
   * @return the Domain DomainLog, can be null if the appender
   *  has not been registered.
   */
  public static DomainLogger get(final String appender) {
    if (instance.get().containsKey(appender)) {
      instance.get().get(appender);
    } else {
      instance.get().put(appender, new DomainLogger());
    }
    return instance.get().get(appender);
  }

  /** Cleans up the Thread Local storage values.*/
  public static void cleanup() {
    for(DomainLogger log : instance.get().values()) {
      log.entries.clear();
      log.stacks.clear();
      log.root = new DomainLog(".");
      log.contextVariables.clear();
    }
    instance.remove();
  }

  /** Register a global variable.
   * @param name the name of the variable.
   * @param value the value of the variable.
   */
  public void registerGlobal(final String name, final String value) {
    contextVariables.put(name, value);
  }

  /** Performs a log operation, only if this logger is enabled.
   * @param stack the stack where the message belongs to, cannot be null.
   * @param message the message to log, cannot be null.
   */
  public void log(final String stack, final String message) {
    if (!isEnabled()) {
      return;
    }
    Validate.notNull(stack, "The stack cannot be null");
    Validate.notNull(message, "The message cannot be null");
    String theStack = replaceWithGlobals(stack);
    if (!stacks.contains(theStack)) {
      String[] paths = theStack.split(Pattern.quote("."));
      for (int i = 0; i < paths.length; i++) {
        String nStack = createStack(paths, i);
        if (!stacks.contains(nStack)) {
          stacks.add(nStack);
          entries.add(new DomainLog(nStack));
        }
      }
    }
    DomainLog entry = new DomainLog("." + theStack, message);
    if (!entries.contains(entry)) {
      entries.add(new DomainLog("." + theStack, message));
    }
  }

  /** Creates the stack for the given paths and position.
   * @param paths the array of paths.
   * @param position the current path position.
   * @return the stack.
   */
  private String createStack(final String[] paths, final int position) {
    String path = String.join(".", Arrays.copyOfRange(paths, 0, position + 1));
    return "." + path;
  }

  /** Replaces the given stack with the global variables.
   * @param path the stack.
   * @return the new stack.
   */
  private String replaceWithGlobals(final String path) {
    String stack = path;
    Set<Entry<String, String>> values = contextVariables.entrySet();
    for (Entry<String, String> entry : values) {
      String key = "${" + entry.getKey() + "}";
      if (stack.contains(key)) {
        stack = stack.replace(key, entry.getValue());
      }
    }
    return stack;
  }

  /** Performs a log operation based on the given structure.
   * @param structure the structure, cannot be null.
   * @param message the message to log.
   */
  public void log(final Structure structure, final String message) {
    log(structure.getStack(), message);
  }

  /** Generates the log entry.
   * @return the log entry, never null.
   */
  public DomainLog generate() {
    for (DomainLog theEntry : entries) {
      String stack = theEntry.getStack();
      DomainLog parent = root.search(stack);
      if (parent == null) {
        String parentStack = stack.substring(0, stack.lastIndexOf('.'));
        if (parentStack.equals("")) {
          root.add(theEntry);
        } else {
          DomainLog newParent = root.search(parentStack);
          if (newParent == null) {
            throw StackNotFoundException.undeclaredStack(parentStack);
          }
          newParent.add(theEntry);
        }
      } else {
        parent.add(theEntry);
      }
    }
    return root;
  }

  /** Enable this logger.*/
  public void enable() {
    enabled = true;
  }

  /** Disable this logger.*/
  public void disable() {
    enabled = false;
  }

  /** Retrieves if this logger is enabled or not.
   * @return true if this logger is enabled.
   */
  public boolean isEnabled() {
    return enabled;
  }

  /** Exception for stacks, not found scenario.
   * @author waabox (me[at]waabox[dot]org)
   */
  public static class StackNotFoundException extends IllegalStateException {

    /** The serialVersionUID, it's never null.*/
    private static final long serialVersionUID = 1L;

    /** Creates a new instance of the exception.
     * @param message the message.
     */
    private StackNotFoundException(final String message) {
      super(message);
    }

    /** Creates a new exception describing that the stack has not been found.
     * @param stack the stack, cannot be null.
     * @return the exception, never null.
     */
    public static StackNotFoundException undeclaredStack(final String stack) {
      return new StackNotFoundException("Undeclared stack: " + stack);
    }

  }

}
