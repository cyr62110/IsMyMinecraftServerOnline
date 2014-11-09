package fr.cvlaminck.aboutmyapp.data.entities.licences;

/**
 * Object representation of a licence
 *
 * @since 0.1
 */
public class LicenceEntity {

    /**
     * Unique identifier for this licence. This allow developers to refer to this licence in
     * their appinfo.json file.
     */
    private String id;

    /**
     * Name of the licence.
     * This value is always the one displayed to the end-user on the library UI.
     */
    private String name;

    /**
     * Other name that can be used to refer to this licence.
     * Those values will be checked if a licence is referenced by name.
     */
    private String[] aliases;

    //TODO : Reference to the actual licence file.


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getAliases() {
        return aliases;
    }

    public void setAliases(String[] aliases) {
        this.aliases = aliases;
    }
}
