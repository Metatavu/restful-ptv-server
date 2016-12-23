package fi.otavanopisto.restfulptv.server.schedulers;

@SuppressWarnings("squid:S1610")
public abstract class EntityUpdater {
  
  public abstract void startTimer();
  
  public abstract void stopTimer();
  
  public abstract String getName();
  
}
