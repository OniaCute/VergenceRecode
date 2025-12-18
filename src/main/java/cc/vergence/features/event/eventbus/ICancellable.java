package cc.vergence.features.event.eventbus;

public interface ICancellable {
    boolean isCancelled();
    void setCancelled(boolean b);
}