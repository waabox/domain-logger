package org.waabox.log;

import java.util.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/** The log entry.
 * @author waabox (me[at]waabox[dot]org)
 */
public class DomainLog {

  /** The entry stack, it's never null.*/
  private final String stack;

  /** The entry value, it's never null.*/
  private String value;

  /** The list of nodes, it's never null.*/
  private final List<DomainLog> nodes = new LinkedList<>();

  /** Creates a new instance of DomainLog.
   * @param theStack the stack.
   * @param message the message.
   */
  DomainLog(final String theStack, final String message) {
    stack = theStack;
    value = message;
  }

  /** Creates a new instance of DomainLog as stack.
   * @param theStack the stack path.
   */
  DomainLog(final String theStack) {
    stack = theStack;
  }

  /** Search within this log entry the entry that matches the given stack.
   * @param path the path to search.
   * @return the log entry or null.
   */
  DomainLog search(final String path) {
    for (DomainLog entry : nodes) {
      if (entry.stack.equals(path)) {
        return entry;
      } else {
        DomainLog candidate = entry.search(path);
        if (candidate != null) {
          return candidate;
        }
      }
    }
    return null;
  }

  /** {@inheritDoc}. */
  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(stack).append(value).toHashCode();
  }

  /** {@inheritDoc}. */
  @Override
  public boolean equals(final Object obj) {
    if (!(obj instanceof DomainLog)) {
      return false;
    }
    DomainLog another = (DomainLog) obj;
    return new EqualsBuilder()
        .append(stack, another.stack)
        .append(value, another.value)
        .isEquals();
  }

  /** Retrieves the stack.
   * @return the stack, never null.
   */
  public String getStack() {
    return stack;
  }

  /** Retrieves the value.
   * @return the value, never null.
   */
  public String getValue() {
    return value;
  }

  /** Retrieves the nodes.
   * @return the nodes, never null.
   */
  public List<DomainLog> getNodes() {
    return nodes;
  }

  /** Adds a new node.
   * @param entry the node to add.
   */
  public void add(final DomainLog entry) {
    nodes.add(entry);
  }

  /** {@inheritDoc}. */
  @Override
  public String toString() {
    return string(0);
  }

  /** Represents this as String.
   * @param size the indentation size.
   * @return the string representing this instance.
   */
  public String string(final int size) {
    int aSize = size + 1;
    String indentation = "";
    for (int i = 0; i < aSize; i++) {
      indentation += "  ";
    }
    StringBuffer sb = new StringBuffer();
    sb.append(stack);
    if (value != null) {
      sb.append(":");
      sb.append(value);
    }
    if (!nodes.isEmpty()) {
      sb.append("[\n");
    }
    Iterator<DomainLog> it = nodes.iterator();
    while (it.hasNext()) {
      DomainLog entry = it.next();
      sb.append(indentation + entry.string(aSize));
      if (it.hasNext()) {
        sb.append(",\n");
      }
    }
    if (!nodes.isEmpty()) {
      sb.append("\n" + indentation + "]\n");
    }
    return sb.toString();
  }

}
