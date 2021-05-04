package dk.mart610k.eggwatch.service;

public interface IEggTimerListener {
     void onCountDown(int timeLeft);

     void  onEggTimerStopped();

}
