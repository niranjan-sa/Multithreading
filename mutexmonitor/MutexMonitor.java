package mutexmonitor;
/*
 * Monitor = Mutex + condition variable (one or more)
 * 
 * Monitors in java are implmented with combination of 
 * wait() & notifiy() methods in tandem with synchronized keyword.
 * wait() when called will release the lock that was acquired
 * 
 * Monitors ensure atomic aquisition & check of a specific conditions. 
 * 
 * 
 * Semaphores vs Monitors can be used interchangebly. 
 * However with semaphores the onus of aquiring and releasing locks is on dev
 * 
 * 
 * 
 * 
 *  After the above discussion, we can now realize that 
    a monitor is made
    up of a mutex and one or more condition variables. A single monitor
    can have multiple condition variables but not vice versa. Theoretically,
    another way to think about a monitor is to consider it as an entity having
    two queues or sets where threads can be placed. One is the entry set and
    the other is the wait set. When a thread A enters a monitor it is placed
    into the entry set. If no other thread owns the monitor, which is
    equivalent of saying no thread is actively executing within the monitor
    section, then thread A will acquire the monitor and is said to own it too.
    Thread A will continue to execute within the monitor section till it exits
    the monitor or calls 
    wait() on an associated condition variable and be
    placed into the wait set. While thread A owns the monitor no other
    thread will be able to execute any of the critical sections protected by the
    monitor. New threads requesting ownership of the monitor get placed
    into the entry set.
    Continuing with our hypothetical example, say another thread B comes
    along and gets placed in the entry set, while thread A sits in the wait set.
    Since no other thread owns the monitor, thread B successfully acquires
    the monitor and continues execution. If thread B exits the monitor section
    without calling 
    notify() on the condition variable, then thread A will
    remain waiting in the wait set. Thread B can also invoke 
    wait() and be
    placed in the wait set along with thread A. This then would require a
    third thread to come along and call 
    notify() on the condition variable on
    which both threads A and B are waiting. Note that only a single thread
    will be able to own the monitor at any given point in time and will have
    exclusive access to data structures or critical sections protected by the
    monitor.
    Practically, 
    in Java each object is a monitor and implicitly has a lock and is
    a condition variable too. You can think of a monitor as a mutex with a
    wait set. Monitors allow threads to exercise mutual exclusion as well as
    cooperation by allowing them to wait and signal on conditions.

    Java's monitors are Mesa monitors. 
 */
public class MutexMonitor {
    
}
