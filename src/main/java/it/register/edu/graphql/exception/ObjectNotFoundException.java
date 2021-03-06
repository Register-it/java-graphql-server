package it.register.edu.graphql.exception;

public class ObjectNotFoundException extends RuntimeException {

  public ObjectNotFoundException(String message, int objectId) {
    super(message + " " + objectId);
  }

  public ObjectNotFoundException(String message, String objectId) {
    super(message + " " + objectId);
  }
}
