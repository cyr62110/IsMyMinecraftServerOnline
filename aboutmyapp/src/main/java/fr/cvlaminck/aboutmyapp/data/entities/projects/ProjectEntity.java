package fr.cvlaminck.aboutmyapp.data.entities.projects;

/**
 * Object representation of a project. This can be your application or a dependency of it.
 * This representation aggregates all information about the project : name, version, developers, etc.
 *
 * @since 0.1
 */
public class ProjectEntity {

    /**
     * Name of the project
     */
    private String name;

    /**
     * Version of the project
     */
    private String version;

    /**
     * A small description of the project
     */
    private String description;

    /**
     *
     */
    private OrganisationEntity organisation;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
