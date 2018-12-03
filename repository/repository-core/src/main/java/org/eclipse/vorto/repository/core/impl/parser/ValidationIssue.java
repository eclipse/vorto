package org.eclipse.vorto.repository.core.impl.parser;

public class ValidationIssue {
  private int lineNumber;
  private String msg;

  public ValidationIssue(int lineNumber, String msg) {
    this.lineNumber = lineNumber;
    this.msg = msg;
  }

  public int getLineNumber() {
    return lineNumber;
  }

  public String getMsg() {
    return msg;
  }

  public String toString() {
    return "On line number " + lineNumber + " : " + msg;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + lineNumber;
    result = prime * result + ((msg == null) ? 0 : msg.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ValidationIssue other = (ValidationIssue) obj;
    if (lineNumber != other.lineNumber)
      return false;
    if (msg == null) {
      if (other.msg != null)
        return false;
    } else if (!msg.equals(other.msg))
      return false;
    return true;
  }
}
