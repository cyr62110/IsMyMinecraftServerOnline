package fr.cvlaminck.aboutmyapp.info.providers;

/**
 * Enumeration of priority values that can be used in AppInfoProvider implementation.
 * You can also add your own priority instead of using the one defined in this
 * enumeration. All AppInfoProvider implemented in the core library uses those values.
 */
public enum Priority {
    HIGHEST(0),
    HIGH(Integer.MAX_VALUE / 4),
    MEDIUM(Integer.MAX_VALUE / 2),
    LOW(3 * Integer.MAX_VALUE / 4),
    LOWEST(Integer.MAX_VALUE);

    private int priority;

    private Priority(int priority) {
        this.priority = priority;
    }

    public int value() {
        return priority;
    }
}
