package fi.otavanopisto.restfulptv.server.schedulers;

public interface IdUpdater {
  
  public void startTimer();
  
  public void stopTimer();
  
  public String getName();
  
}
