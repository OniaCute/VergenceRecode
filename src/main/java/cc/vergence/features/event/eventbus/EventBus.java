package cc.vergence.features.event.eventbus;

import java.lang.annotation.*;
import java.lang.invoke.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public final class EventBus {
    private final Map<Class<?>, CopyOnWriteArrayList<Listener>> map = new HashMap<>();

    public void post(Object event) {
        List<Listener> list = map.get(event.getClass());
        if (list == null) return;
        if (event instanceof ICancellable) ((ICancellable) event).setCancelled(false);
        for (Listener l : list) {
            l.consumer.accept(event);
            if (event instanceof ICancellable && ((ICancellable) event).isCancelled()) break;
        }
    }

    public void subscribe(Class<?> clazz) {
        subscribe(clazz, null);
    }

    public void subscribe(Object obj) {
        subscribe(obj.getClass(), obj);
    }

    private void subscribe(Class<?> clazz, Object instance) {
        for (Method m : clazz.getDeclaredMethods()) {
            Subscribe ann = m.getAnnotation(Subscribe.class);
            if (ann == null || m.getParameterCount() != 1) continue;
            Class<?> type = m.getParameterTypes()[0];
            Listener l = new Listener(type, ann.priority(), instance, m);
            map.computeIfAbsent(type, k -> new CopyOnWriteArrayList<>()).add(l);
            map.get(type).sort(Comparator.comparingInt(a -> a.priority));
        }
    }

    public void unsubscribe(Object obj) {
        map.values().forEach(list -> list.removeIf(l -> l.instance == obj));
    }

    public void unload() {
        map.clear();
    }

    private static final class Listener {
        final Class<?> type;
        final int priority;
        final Object instance;   // null = static
        final Consumer<Object> consumer;

        Listener(Class<?> type, int priority, Object instance, Method m) {
            this.type = type;
            this.priority = priority;
            this.instance = instance;
            this.consumer = LambdaListener.create(instance, m);
        }
    }

    private static final class LambdaListener {
        private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

        @SuppressWarnings("unchecked")
        static Consumer<Object> create(Object instance, Method m) {
            try {
                MethodType mt = MethodType.methodType(void.class, m.getParameterTypes()[0]);
                MethodHandle target;
                boolean isStatic = Modifier.isStatic(m.getModifiers());
                if (isStatic) {
                    target = LOOKUP.findStatic(m.getDeclaringClass(), m.getName(), mt);
                } else {
                    target = LOOKUP.findVirtual(m.getDeclaringClass(), m.getName(), mt);
                }
                CallSite site = LambdaMetafactory.metafactory(
                        LOOKUP,
                        "accept",
                        isStatic ? MethodType.methodType(Consumer.class) : MethodType.methodType(Consumer.class, m.getDeclaringClass()),
                        MethodType.methodType(void.class, Object.class),
                        target,
                        mt);
                MethodHandle factory = site.getTarget();
                return (Consumer<Object>) (isStatic ? factory.invoke() : factory.invoke(instance));
            } catch (Throwable t) {
                throw new RuntimeException("Unable to create lambda for " + m, t);
            }
        }
    }
}