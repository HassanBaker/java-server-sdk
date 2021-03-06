package com.launchdarkly.client.interfaces;

import com.launchdarkly.client.integrations.EventProcessorBuilder;

import java.io.Closeable;
import java.net.URI;
import java.util.Date;

/**
 * Interface for a component that can deliver preformatted event data.
 * 
 * @see EventProcessorBuilder#eventSender(EventSenderFactory)
 * @since 4.14.0
 */
public interface EventSender extends Closeable {
  /**
   * Attempt to deliver an event data payload.
   * <p>
   * This method will be called synchronously from an event delivery worker thread. 
   * 
   * @param kind specifies which type of event data is being sent
   * @param data the preformatted JSON data, as a string
   * @param eventCount the number of individual events in the data
   * @param eventsBaseUri the configured events endpoint base URI
   * @return a {@link Result}
   */
  Result sendEventData(EventDataKind kind, String data, int eventCount, URI eventsBaseUri);
  
  /**
   * Enumerated values corresponding to different kinds of event data.
   */
  public enum EventDataKind {
    /**
     * Regular analytics events.
     */
    ANALYTICS,
    
    /**
     * Diagnostic data generated by the SDK.
     */
    DIAGNOSTICS
  };
  
  /**
   * Encapsulates the results of a call to {@link EventSender#sendEventData(EventDataKind, String, int, URI)}.
   */
  public static final class Result {
    private boolean success;
    private boolean mustShutDown;
    private Date timeFromServer;
    
    /**
     * Constructs an instance.
     * 
     * @param success true if the events were delivered
     * @param mustShutDown true if an unrecoverable error (such as an HTTP 401 error, implying that the
     *   SDK key is invalid) means the SDK should permanently stop trying to send events
     * @param timeFromServer the parsed value of an HTTP Date header received from the remote server,
     *   if any; this is used to compensate for differences between the application's time and server time
     */
    public Result(boolean success, boolean mustShutDown, Date timeFromServer) {
      this.success = success;
      this.mustShutDown = mustShutDown;
      this.timeFromServer = timeFromServer;
    }

    /**
     * Returns true if the events were delivered.
     * 
     * @return true if the events were delivered
     */
    public boolean isSuccess() {
      return success;
    }

    /**
     * Returns true if an unrecoverable error (such as an HTTP 401 error, implying that the
     * SDK key is invalid) means the SDK should permanently stop trying to send events
     * 
     * @return true if event delivery should shut down
     */
    public boolean isMustShutDown() {
      return mustShutDown;
    }

    /**
     * Returns the parsed value of an HTTP Date header received from the remote server, if any. This
     * is used to compensate for differences between the application's time and server time.
     * 
     * @return a date value or null
     */
    public Date getTimeFromServer() {
      return timeFromServer;
    }
  }
}
